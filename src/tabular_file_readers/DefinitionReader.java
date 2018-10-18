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

import elements.Definition;

public class DefinitionReader {

	private String outputDirectory;
	
	public DefinitionReader(String outputDirectory)
	{
		this.outputDirectory = outputDirectory;
	}
	
	public Map<Integer, Definition> getDefinitions() throws IOException
	{
		Map<Integer, Definition> definitions = new HashMap<Integer, Definition>();
		
		BufferedReader reader = new BufferedReader(new FileReader(outputDirectory + File.separator + "definitions.tsv"));
		String line = "";
		while ((line = reader.readLine()) != null)
		{
			if (!line.startsWith("#"))
			{
				int definitionID = Integer.parseInt(line.split("\t")[0]);
				int frequency = Integer.parseInt(line.split("\t")[1]);
				String definition = line.split("\t")[2];
				String abbreviations[] = line.split("\t")[3].split(",");
				String appearances[] = line.split("\t")[4].split(",");
				
				List<String> appearsOn = new ArrayList<String>(Arrays.asList(appearances));
				
				List<Integer> abbreviationList = new ArrayList<Integer>();
				for (int i = 0; i < abbreviations.length; i++)
				{
					int abbreviation = Integer.parseInt(abbreviations[i]);
					abbreviationList.add(abbreviation);
				}
				
				Definition def = new Definition(definition, frequency, definitionID, abbreviationList, appearsOn);
				definitions.put(definitionID, def);
			}			
		}
		reader.close();
		
		return definitions;
	}
}
