package tabular_file_readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import elements.Abbreviation;

public class AbbreviationReader {

	private String outputDirectory;
	
	public AbbreviationReader(String outputDirectory)
	{
		this.outputDirectory = outputDirectory;
	}
	
	public Map<Integer, Abbreviation> getAbbreviations() throws IOException
	{
		Map<Integer, Abbreviation> abbreviations = new HashMap<Integer, Abbreviation>();
		
		BufferedReader reader = new BufferedReader(new FileReader(outputDirectory + File.separator + "abbreviations.tsv"));
		String line = "";
		while ((line = reader.readLine()) != null)
		{
			if (!line.startsWith("# Abbreviation ID"))
			{
				int abbreviationID = Integer.parseInt(line.split("\t")[0]);
				int frequency = Integer.parseInt(line.split("\t")[1]);
				String abbreviation = line.split("\t")[2];
				String definitions[] = line.split("\t")[3].split(",");
				String appearances[] = line.split("\t")[4].split(",");
				
				List<String> appearsOn = new ArrayList<String>(Arrays.asList(appearances));
				
				List<Integer> definitionList = new ArrayList<Integer>();
				for (int i = 0; i < definitions.length; i++)
				{
					int definitionID = Integer.parseInt(definitions[i]);
					definitionList.add(definitionID);
				}
				
				Abbreviation abbr = new Abbreviation(abbreviation, frequency, abbreviationID, definitionList, appearsOn);
				abbreviations.put(abbreviationID, abbr);
			}			
		}
		reader.close();
		
		return abbreviations;
	}
}
