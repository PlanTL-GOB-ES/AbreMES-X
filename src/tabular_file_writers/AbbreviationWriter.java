package tabular_file_writers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import elements.Abbreviation;
import tabular_file_readers.PreprocessedFileReader;

public class AbbreviationWriter {
	
	private String preprocessedFile;
	private String outputDirectory;
	
	private Map<Integer, Abbreviation> abbreviations;
	
	public AbbreviationWriter(String preprocessedFile, String outputDirectory)
	{
		this.preprocessedFile = preprocessedFile;
		this.outputDirectory = outputDirectory;
		
		abbreviations = new HashMap<Integer, Abbreviation>();
	}

	public void loadAbbreviations() throws IOException
	{
		PreprocessedFileReader preprocessed = new PreprocessedFileReader(preprocessedFile);
		preprocessed.reader();
		abbreviations = preprocessed.organizeAbbreviations();	
	}
	
	public void writeAbbreviations() throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectory + File.separator + "abbreviations.tsv"));
		
		writer.write("# Abbreviation ID" + "\t" + "Frequency" + "\t" + "Abbreviation"
						+ "\t" + "Definitions" + "\t" + "Appears on" + "\n");
		
		Iterator<Integer> iter = abbreviations.keySet().iterator();
		while (iter.hasNext())
		{
			int ID = iter.next();
			Abbreviation abbreviation = abbreviations.get(ID);
			
			int freq = abbreviation.getFrequency();
			String SF = abbreviation.getAbbreviation();
			List<Integer> definitions = abbreviation.getDefinitions();
			List<String> appearsOn = abbreviation.getAppearsOn();
			
			String LFs = new String();
			for (int i = 0; i < definitions.size(); i++)
			{
				int LF = definitions.get(i);
				LFs = LFs + "," + LF;
			}
			LFs = LFs.substring(1);
			
			String appears = new String();
			for (int i = 0; i < appearsOn.size(); i++)
			{
				String doc = appearsOn.get(i);
				appears = appears + "," + doc;
			}
			appears = appears.substring(1);
			
			writer.write(ID + "\t" + freq + "\t" + SF + "\t" + LFs + "\t" + appears + "\n");
		}
		
		writer.close();
	}
}
