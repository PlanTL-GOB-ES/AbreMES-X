package tabular_file_writers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import elements.Definition;
import tabular_file_readers.PreprocessedFileReader;

public class DefinitionWriter {
	
	private String preprocessedFile;
	private String outputDirectory;
	
	private Map<Integer, Definition> definitions;
	
	public DefinitionWriter(String preprocessedFile, String outputDirectory)
	{
		this.preprocessedFile = preprocessedFile;
		this.outputDirectory = outputDirectory;
		
		definitions = new HashMap<Integer, Definition>();
	}

	public void loadDefinitions() throws IOException
	{
		PreprocessedFileReader preprocessed = new PreprocessedFileReader(preprocessedFile);
		preprocessed.reader();
		definitions = preprocessed.organizeDefinitions();	
	}
	
	public void writeDefinitions() throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectory + File.separator + "definitions.tsv"));
		
		writer.write("# Definition ID" + "\t" + "Frequency" + "\t" + "Definition"
						+ "\t" + "Abbreviations" + "\t" + "Appears on" + "\n");
		
		Iterator<Integer> iter = definitions.keySet().iterator();
		while (iter.hasNext())
		{
			int ID = iter.next();
			Definition definition = definitions.get(ID);
			
			int freq = definition.getFrequency();
			String LF = definition.getDefinition();
			List<Integer> SFlist = definition.getAbbreviations();
			List<String> appearsOn = definition.getAppearsOn();
			
			String SFs = new String();
			for (int i = 0; i < SFlist.size(); i++)
			{
				int SF = SFlist.get(i);
				SFs = SFs + "," + SF;
			}
			SFs = SFs.substring(1);
			
			String appears = new String();
			for (int i = 0; i < appearsOn.size(); i++)
			{
				String doc = appearsOn.get(i);
				appears = appears + "," + doc;
			}
			appears = appears.substring(1);
			
			writer.write(ID + "\t" + freq + "\t" + LF + "\t" + SFs + "\t" + appears + "\n");
		}
		
		writer.close();
	}
}
