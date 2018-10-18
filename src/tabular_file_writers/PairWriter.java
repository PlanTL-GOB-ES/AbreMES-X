package tabular_file_writers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import elements.Pair;
import tabular_file_readers.PreprocessedFileReader;

public class PairWriter {
	
	private String preprocessedFile;
	private String outputDirectory;
	
	private Map<Integer, Pair> pairs;
	
	public PairWriter(String preprocessedFile, String outputDirectory)
	{
		this.preprocessedFile = preprocessedFile;
		this.outputDirectory = outputDirectory;
		
		pairs = new HashMap<Integer, Pair>();
	}

	public void loadPairs() throws IOException
	{
		PreprocessedFileReader preprocessed = new PreprocessedFileReader(preprocessedFile);
		preprocessed.reader();
		pairs = preprocessed.organizePairs();	
	}
	
	public void writePairs() throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectory + File.separator + "pairs.tsv"));
		
		writer.write("# Pair ID" + "\t" + "Abbreviation ID" + "\t" + "Definition ID" + "\t" + "Frequency" + "\t" + "Abbreviation"
						+ "\t" + "Definition" + "\t" + "Appears on" + "\n");
		
		Iterator<Integer> iter = pairs.keySet().iterator();
		while (iter.hasNext())
		{
			int ID = iter.next();
			Pair pair = pairs.get(ID);
			
			int abbreviationID = pair.getAbbreviationID();
			int definitionID = pair.getDefinitionID();
			int freq = pair.getFrequency();
			String abbreviation = pair.getAbbreviation();
			String definition = pair.getDefinition();
			List<String> appearsOn = pair.getAppearsOn();
			
			String appears = new String();
			for (int i = 0; i < appearsOn.size(); i++)
			{
				String doc = appearsOn.get(i);
				appears = appears + "," + doc;
			}
			appears = appears.substring(1);
			
			writer.write(ID + "\t" + abbreviationID + "\t" + definitionID + "\t" + freq + "\t" + abbreviation + "\t" + definition + "\t" + appears + "\n");
		}
		
		writer.close();
	}
}
