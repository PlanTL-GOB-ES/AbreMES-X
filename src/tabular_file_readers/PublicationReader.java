package tabular_file_readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import elements.Publication;

public class PublicationReader {

	private String outputDirectory;
	
	public PublicationReader(String outputDirectory)
	{
		this.outputDirectory = outputDirectory;
	}
	
	public Map<String, Publication> getPublications() throws IOException
	{
		Map<String, Publication> publications = new HashMap<String, Publication>();
		
		BufferedReader reader = new BufferedReader(new FileReader(outputDirectory + File.separator + "publications.tsv"));
		String line = "";
		while ((line = reader.readLine()) != null)
		{
			if (!line.startsWith("#"))
			{
				String publicationID = line.split("\t")[0];
				String source = line.split("\t")[1];
				String date = line.split("\t")[2];
				String publisher = line.split("\t")[3];
				String ISSN = line.split("\t")[4];
				String JSTs[]= line.split("\t")[5].split(",");
				String abbreviations[] = line.split("\t")[6].split(",");
				String definitions[] = line.split("\t")[7].split(",");
				String pairs[] = line.split("\t")[8].split(",");
				
				List<Integer> abbreviationList = new ArrayList<Integer>();
				for (int i = 0; i < abbreviations.length; i++)
				{
					int abbreviationID = Integer.parseInt(abbreviations[i]);
					abbreviationList.add(abbreviationID);
				}
				
				List<Integer> definitionList = new ArrayList<Integer>();
				for (int i = 0; i < definitions.length; i++)
				{
					int definitionID = Integer.parseInt(definitions[i]);
					definitionList.add(definitionID);
				}
				
				List<Integer> pairList = new ArrayList<Integer>();
				for (int i = 0; i < pairs.length; i++)
				{
					int pairID = Integer.parseInt(pairs[i]);
					pairList.add(pairID);
				}
				
				List<String> JSTlist = new ArrayList<String>();
				for (int i = 0; i < JSTs.length; i++)
				{
					String JST = JSTs[i];
					JSTlist.add(JST);
				}
				
				Publication publication = new Publication(publicationID, null, null, date, publisher, source, ISSN, JSTlist, abbreviationList, definitionList, pairList);
				publications.put(publicationID, publication);
			}			
		}
		reader.close();
		
		return publications;
	}
}
