package managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import elements.Abbreviation;
import elements.Definition;
import elements.Pair;
import elements.Publication;
import tabular_file_readers.AbbreviationReader;
import tabular_file_readers.DefinitionReader;
import tabular_file_readers.PairReader;


public class PublicationManager {

	private String outputDirectory;
	
	private Map<Integer, Pair> pairInfo;
	private Map<Integer, Abbreviation> abbreviationInfo;
	private Map<Integer, Definition> definitionInfo;
	
	private Map<String, Publication> allPublications;
	
	public PublicationManager(String outputDirectory, Map<String, Publication> allPublications)
	{
		this.outputDirectory = outputDirectory;
		this.allPublications = allPublications;
		
		pairInfo = new HashMap<Integer, Pair>();
		abbreviationInfo = new HashMap<Integer, Abbreviation>();
		definitionInfo = new HashMap<Integer, Definition>();
		
		
	}
		
	public void loadBasicInfo() throws IOException
	{
		PairReader pairReader = new PairReader(outputDirectory);
		pairInfo = pairReader.getPairs();
		
		AbbreviationReader abbreviationReader = new AbbreviationReader(outputDirectory);
		abbreviationInfo = abbreviationReader.getAbbreviations();
		
		DefinitionReader definitionReader = new DefinitionReader(outputDirectory);
		definitionInfo = definitionReader.getDefinitions();
	}
	
	public void organizeAll()
	{
		organizePairInfo();
		organizeAbbreviationInfo();
		organizeDefinitionInfo();
	}
	
	public Map<String, Publication> returnPublicationInfo()
	{
		return allPublications;
	}
	
	public void organizePairInfo()
	{
		Iterator<Integer> iter = pairInfo.keySet().iterator();
		while (iter.hasNext())
		{
			int pairID = iter.next();
			Pair pair = pairInfo.get(pairID);
			
			List<String> publications = pair.getAppearsOn();
			for (int i = 0; i < publications.size(); i++)
			{
				String publicationID = publications.get(i);
				
				Publication publication = new Publication(null, null, null, null, null, null, null, null, null, null, null);
				if (allPublications.containsKey(publicationID))
				{
					publication = allPublications.get(publicationID);
				}
				
				List<Integer> pairs = new ArrayList<Integer>();
				if (publication.getPairs() != null)
				{
					pairs = publication.getPairs();
				}
				
				if (!pairs.contains(pairID))
				{
					pairs.add(pairID);
				}
				
				publication.setPairs(pairs);
				
				allPublications.put(publicationID, publication);
			}
		}
	}
	
	public void organizeAbbreviationInfo()
	{
		Iterator<Integer> iter = abbreviationInfo.keySet().iterator();
		while (iter.hasNext())
		{
			int abbreviationID = iter.next();
			Abbreviation abbreviation = abbreviationInfo.get(abbreviationID);
			
			List<String> publications = abbreviation.getAppearsOn();
			for (int i = 0; i < publications.size(); i++)
			{
				String publicationID = publications.get(i);
				
				Publication document = new Publication(null, null, null, null, null, null, null, null, null, null, null);
				if (allPublications.containsKey(publicationID))
				{
					document = allPublications.get(publicationID);
				}
				
				List<Integer> abbreviations = new ArrayList<Integer>();
				if (document.getAbbreviations() != null)
				{
					abbreviations = document.getAbbreviations();
				}
				
				if (!abbreviations.contains(abbreviationID))
				{
					abbreviations.add(abbreviationID);
				}
				
				document.setAbbreviations(abbreviations);
				
				allPublications.put(publicationID, document);
			}
		}
	}
	
	public void organizeDefinitionInfo()
	{
		Iterator<Integer> iter = definitionInfo.keySet().iterator();
		while (iter.hasNext())
		{
			int definitionID = iter.next();
			Definition definition = definitionInfo.get(definitionID);
			
			List<String> publications = definition.getAppearsOn();
			for (int i = 0; i < publications.size(); i++)
			{
				String publicationID = publications.get(i);
				
				Publication publication = new Publication(null, null, null, null, null, null, null, null, null, null, null);
				if (allPublications.containsKey(publicationID))
				{
					publication = allPublications.get(publicationID);
				}
				
				List<Integer> definitions = new ArrayList<Integer>();
				if (publication.getDefinitions() != null)
				{
					definitions = publication.getDefinitions();
				}
				
				if (!definitions.contains(definitionID))
				{
					definitions.add(definitionID);
				}
				
				publication.setDefinitions(definitions);
				
				allPublications.put(publicationID, publication);
			}
		}
	}
	
}
