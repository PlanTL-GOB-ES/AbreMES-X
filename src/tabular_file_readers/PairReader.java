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

import elements.Pair;

public class PairReader {

	private String outputDirectory;
	
	public PairReader(String outputDirectory)
	{
		this.outputDirectory = outputDirectory;
	}
	
	public Map<Integer, Pair> getPairs() throws IOException
	{
		Map<Integer, Pair> pairs = new HashMap<Integer, Pair>();
		
		BufferedReader reader = new BufferedReader(new FileReader(outputDirectory + File.separator + "pairs.tsv"));
		String line = "";
		while ((line = reader.readLine()) != null)
		{
			if (!line.startsWith("#"))
			{
				int pairID = Integer.parseInt(line.split("\t")[0]);
				int abbreviationID = Integer.parseInt(line.split("\t")[1]);
				int definitionID = Integer.parseInt(line.split("\t")[2]);
				int frequency = Integer.parseInt(line.split("\t")[3]);
				String abbreviation = line.split("\t")[4];
				String definition = line.split("\t")[5];
				String appearances[] = line.split("\t")[6].split(",");
				
				List<String> appearsOn = new ArrayList<String>(Arrays.asList(appearances));
				
				Pair pair = new Pair(pairID, abbreviation, definition, frequency, abbreviationID, definitionID, appearsOn);
				pairs.put(pairID, pair);
			}			
		}
		reader.close();
		
		return pairs;
	}
}
