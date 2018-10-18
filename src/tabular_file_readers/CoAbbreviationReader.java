package tabular_file_readers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import elements.CoAbbreviation;

public class CoAbbreviationReader {

	private String file;
	
	public CoAbbreviationReader(String file)
	{
		this.file = file;
	}
	
	public Map<Integer, CoAbbreviation> getCoAbbreviations() throws IOException
	{
		Map<Integer, CoAbbreviation> coAbbreviations = new HashMap<Integer, CoAbbreviation>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "";
		while ((line = reader.readLine()) != null)
		{
			if (!line.startsWith("#"))
			{
				int abbreviationID = Integer.parseInt(line.split("\t")[0]);
				String abbreviation = line.split("\t")[1];
				int abbreviationFrequency = Integer.parseInt(line.split("\t")[2]);
				int publicationFrequency = Integer.parseInt(line.split("\t")[3]);				
				String coAbbreviationArray[] = line.split("\t")[2].split(",");
				String publications[] = line.split("\t")[3].split(",");

				
				List<Integer> coAbbreviationList = new ArrayList<Integer>();
				for (int i = 0; i < coAbbreviationArray.length; i++)
				{
					int soAbbreviationID = Integer.parseInt(coAbbreviationArray[i]);
					coAbbreviationList.add(soAbbreviationID);
				}
				
				List<String> publicationList = new ArrayList<String>();
				for (int i = 0; i < publications.length; i++)
				{
					String publicationID = publications[i];
					publicationList.add(publicationID);
				}
								
				CoAbbreviation coAbbreviation = new CoAbbreviation(abbreviationID, abbreviation, abbreviationFrequency, publicationFrequency, coAbbreviationList, publicationList);
				coAbbreviations.put(abbreviationID, coAbbreviation);
			}			
		}
		reader.close();
		
		return coAbbreviations;
	}
}
