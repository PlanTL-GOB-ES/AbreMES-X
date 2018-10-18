package tabular_file_writers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import elements.Publication;
import managers.PublicationManager;

public class PublicationWriter {
	
	private String outputDirectory;	
	private Map<String, Publication> publications; 
	
	public PublicationWriter(String outputDirectory, Map<String, Publication> publicationCorpus)
	{
		this.outputDirectory = outputDirectory;
		this.publications = publicationCorpus;
	}

	public void getPublicationInfo() throws IOException
	{
		PublicationManager publicationManager = new PublicationManager(outputDirectory, publications);
		publicationManager.loadBasicInfo();
		publicationManager.organizeAll();
		publications = publicationManager.returnPublicationInfo();
	}
	
	public void printPublicationInfo() throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectory + File.separator + "publications.tsv"));
		writer.write("# Publication ID" + "\t" + "Source" + "\t" + "Date" + "\t" + "Publisher" + "\t" + "ISSN" + "\t" 
				+ "Journal Subject Term" + "\t"	+ "Abbreviations" + "\t" + "Definitions" + "\t" + "Pairs" + "\n");
		
		int publicationsWithAbbreviations = 0;
		
		Iterator<String> iter = publications.keySet().iterator();
		while (iter.hasNext())
		{
			String publicationID = iter.next();
			Publication publication = publications.get(publicationID);
			
			List<Integer> abbreviationList = publication.getAbbreviations();
			List<Integer> definitionList = publication.getDefinitions();
			List<Integer> pairList = publication.getPairs();
			
			String abbreviations = new String("0");
			if (!abbreviationList.isEmpty())
			{
				for (int i = 0; i < abbreviationList.size(); i++)
				{
					int abbreviationID = abbreviationList.get(i);
					abbreviations = abbreviations + "," + abbreviationID;
				}
				abbreviations = abbreviations.substring(2);
			}
			
			
			String definitions = new String("0");
			if (!definitionList.isEmpty())
			{
				for (int i = 0; i < definitionList.size(); i++)
				{
					int LFID = definitionList.get(i);
					definitions = definitions + "," + LFID;
				}
				definitions = definitions.substring(2);
			}
			
			
			String pairs = new String("0");
			if (!pairList.isEmpty())
			{
				for (int i = 0; i < pairList.size(); i++)
				{
					int pairID = pairList.get(i);
					pairs = pairs + "," + pairID;
				}
				pairs = pairs.substring(2);
			}			
			
			String title = publication.getTitle();
			String summary = publication.getSummary();
			String date = publication.getDate();
			String publisher = publication.getPublisher();
			String ISSN = publication.getISSN();
			String source = publication.getSource();
			List<String> JST = publication.getJST();
			
			if (!abbreviations.equals("0") && !definitions.equals("0") && !pairs.equals("0"))
			{
				writer.write(publicationID + "\t" + source + "\t" + date + "\t" + publisher + "\t" + ISSN + "\t" 
						+ JST.toString() + "\t"	+ abbreviations + "\t" + definitions + "\t" + pairs + "\n");
				
				publicationsWithAbbreviations++;
			}			
		}
		
		System.out.println(publicationsWithAbbreviations + " publications found with abbeviations.");
		
		writer.close();
	}
}
