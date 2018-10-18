package tabular_file_writers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import elements.CoAbbreviation;
import managers.CoAbbreviationManager;

public class CoAbbreviationWriter {

	private String outputDirectory;
	
	private Map<Integer, CoAbbreviation> coAbbreviations;
	
	public CoAbbreviationWriter(String outputDirectory)
	{
		this.outputDirectory = outputDirectory;
		
		coAbbreviations = new HashMap<Integer, CoAbbreviation>();
	}

	public void getPublicationInfo() throws IOException
	{
		CoAbbreviationManager coOrg = new CoAbbreviationManager(outputDirectory);
		coOrg.loadBasicInfo();
		coOrg.organizeAll();
		coAbbreviations = coOrg.returnCoAbbreviationInfo();
	}
	
	public void printCoAbbreviationInfo() throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectory + File.separator + "CoAbbreviations.tsv"));
		writer.write("# Abbreviation ID" + "\t" + "Abbreviation" + "\t" + "Co-Abbr frequency" + "\t" + "Pub. frequency" + "\t" + "Co-abbreviations" + "\t" + "Appears on" + "\n");
		
		Iterator<Integer> iter = coAbbreviations.keySet().iterator();
		while (iter.hasNext())
		{
			int abbreviationID = iter.next();
			
			if (abbreviationID != 0)
			{
				CoAbbreviation coAbbreviation = coAbbreviations.get(abbreviationID);
				
				String abbreviation = coAbbreviation.getAbbreviation();
				int abbreviationFrequency = coAbbreviation.getAbbreviationFrequency();
				int publicationFrequency = coAbbreviation.getPublicationFrequency();
				List<Integer> coAbbreviations = coAbbreviation.getCoAbbreviations();
				List<String> publications = coAbbreviation.getPublications();
							
				String shorts = new String();
				if (coAbbreviations == null)
				{
					shorts = "-";
				}
				else
				{
					for (int i = 0; i < coAbbreviations.size(); i++)
					{
						int coSFID = coAbbreviations.get(i);
						shorts = shorts + "," + coSFID;
					}
					shorts = shorts.substring(1);
				}		
				
				String docs = new String();
				for (int i = 0; i < publications.size(); i++)
				{
					String docID = publications.get(i);
					docs = docs + "," + docID;
				}
				docs = docs.substring(1);
				
				writer.write(abbreviationID + "\t" + abbreviation + "\t" + abbreviationFrequency + "\t" + publicationFrequency + "\t" + shorts + "\t" + docs + "\n");			
			}
			
		}
		
		writer.close();
	}
}
