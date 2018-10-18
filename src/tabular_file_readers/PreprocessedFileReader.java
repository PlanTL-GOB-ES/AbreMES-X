package tabular_file_readers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import elements.Abbreviation;
import elements.Definition;
import elements.Pair;

public class PreprocessedFileReader {
	
	private String preprocessedFile;
	
	private Map<Integer, Map<String, String>> lineInfo;
	
	public PreprocessedFileReader(String preprocessedFile)
	{
		this.preprocessedFile = preprocessedFile;
		lineInfo = new HashMap<Integer, Map<String, String>>();
	}

	public void reader() throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(preprocessedFile));
		String line = "";
		while ((line = reader.readLine()) != null)
		{
			if (!line.startsWith("# Line num."))
			{
				int lineNum = Integer.parseInt(line.split("\t")[0]);
				String publicationID = line.split("\t")[1];
				String corpusName = line.split("\t")[2];
				String abbreviationID = line.split("\t")[3];
				String definitionID = line.split("\t")[4];
				String abbreviation = line.split("\t")[5];
				String definition = line.split("\t")[6];
				
				Map<String, String> info = new HashMap<String, String>();
				info.put("publicationID", publicationID);
				info.put("corpusName", corpusName);
				info.put("abbreviationID", abbreviationID);
				info.put("definitionID", definitionID);
				info.put("abbreviation", abbreviation);
				info.put("definition", definition);
				
				lineInfo.put(lineNum, info);
			}			
		}
		reader.close();
	}
	
	public Map<Integer, Map<String, String>> getLinesInfo()
	{
		return lineInfo;
	}
	
	public Map<Integer, Abbreviation> organizeAbbreviations()
	{
		Map<Integer, Abbreviation> abbreviations = new HashMap<Integer, Abbreviation>();
		
		Iterator<Integer> lineIter = lineInfo.keySet().iterator();
		while (lineIter.hasNext())
		{
			int lineNum = lineIter.next();
			Map<String, String> info = lineInfo.get(lineNum);
			
			String publicationID = info.get("publicationID");
			String SF = info.get("abbreviation");
			int abbreviationID = Integer.parseInt(info.get("abbreviationID"));
			int definitionID = Integer.parseInt(info.get("definitionID"));
			
			Abbreviation abbreviation = new Abbreviation(SF, 0, abbreviationID, null, null);
			
			if (abbreviations.containsKey(abbreviationID))
			{
				abbreviation = abbreviations.get(abbreviationID);
			}			 
			int frequency = abbreviation.getFrequency() + 1;
			List<Integer> definitions = abbreviation.getDefinitions();
			if (definitions == null)
			{
				definitions = new ArrayList<Integer>();
				definitions.add(definitionID);
			}
			else
			{
				if (!definitions.contains(definitionID))
				{
					definitions.add(definitionID);
				}
			}
			
			List<String> appearsOn = abbreviation.getAppearsOn();
			if (appearsOn == null)
			{
				appearsOn = new ArrayList<String>();
				appearsOn.add(publicationID);
			}
			else
			{
				if (!appearsOn.contains(publicationID))
				{
					appearsOn.add(publicationID);
				}
			}
			
			abbreviation.setFrequency(frequency);
			abbreviation.setDefinitions(definitions);
			abbreviation.setAppearsOn(appearsOn);
			
			abbreviations.put(abbreviationID, abbreviation);
		}
		
		return abbreviations;
	}

	public Map<Integer, Definition> organizeDefinitions() 
	{
		Map<Integer, Definition> definitions = new HashMap<Integer, Definition>();
		
		Iterator<Integer> lineIter = lineInfo.keySet().iterator();
		while (lineIter.hasNext())
		{
			int lineNum = lineIter.next();
			Map<String, String> info = lineInfo.get(lineNum);
			
			String publicationID = info.get("publicationID");
			String LF = info.get("definition");
			int abbreviationID = Integer.parseInt(info.get("abbreviationID"));
			int definitionID = Integer.parseInt(info.get("definitionID"));
			
			Definition definition = new Definition(LF, 0, abbreviationID, null, null);
			
			if (definitions.containsKey(definitionID))
			{
				definition = definitions.get(definitionID);
			}			 
			int frequency = definition.getFrequency() + 1;
			List<Integer> shortForms = definition.getAbbreviations();
			if (shortForms == null)
			{
				shortForms = new ArrayList<Integer>();
				shortForms.add(abbreviationID);
			}
			else
			{
				if (!shortForms.contains(abbreviationID))
				{
					shortForms.add(abbreviationID);
				}
			}
			
			List<String> appearsOn = definition.getAppearsOn();
			if (appearsOn == null)
			{
				appearsOn = new ArrayList<String>();
				appearsOn.add(publicationID);
			}
			else
			{
				if (!appearsOn.contains(publicationID))
				{
					appearsOn.add(publicationID);
				}
			}
			
			definition.setFrequency(frequency);
			definition.setAbbreviations(shortForms);
			definition.setAppearsOn(appearsOn);
			
			definitions.put(definitionID, definition);
		}
		
		return definitions;
	}
	
	public Map<Integer, Pair> organizePairs()
	{
		Map<Integer, Pair> pairs = new HashMap<Integer, Pair>();
		Map<String, Integer> pairIDs = new HashMap<String, Integer>();	// <LFID-SFID, pairID>
		
		int incrementalPairID = 1;
		
		Iterator<Integer> lineIter = lineInfo.keySet().iterator();
		while (lineIter.hasNext())
		{
			int lineNum = lineIter.next();
			Map<String, String> info = lineInfo.get(lineNum);
			
			String publicationID = info.get("publicationID");
			String abbreviation = info.get("abbreviation");
			String definition = info.get("definition");
			int abbreviationID = Integer.parseInt(info.get("abbreviationID"));
			int definitionID = Integer.parseInt(info.get("definitionID"));
			
			String thisPair = new String();
			thisPair = abbreviationID + "-" + definitionID;
			
			Pair pair = new Pair();
			if (pairIDs.containsKey(thisPair))
			{
				int pairID = pairIDs.get(thisPair);
				pair = pairs.get(pairID);
				
				int frequency = pair.getFrequency();
				frequency++;
				pair.setFrequency(frequency);
				
				List<String> appearsOn = pair.getAppearsOn();
				if (!appearsOn.contains(publicationID))
				{
					appearsOn.add(publicationID);
					pair.setAppearsOn(appearsOn);
				}
				
				pairs.put(pairID, pair);				
			}
			else
			{
				pair.setFrequency(1);
				pair.setAbbreviation(abbreviation);
				pair.setDefinition(definition);
				pair.setAbbreviationID(abbreviationID);
				pair.setDefinitionID(definitionID);
				
				List<String> appearsOn = new ArrayList<String>();
				appearsOn.add(publicationID);
				pair.setAppearsOn(appearsOn);
				
				pairs.put(incrementalPairID, pair);
				pairIDs.put(thisPair, incrementalPairID);
				incrementalPairID++;
			}
		}
		
		return pairs;
	}
}
