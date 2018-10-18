package managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import elements.Abbreviation;
import elements.CoAbbreviation;
import elements.Publication;
import tabular_file_readers.AbbreviationReader;
import tabular_file_readers.PublicationReader;

public class CoAbbreviationManager {

	private String outputDirectory;
	
	private Map<Integer, Abbreviation> abbreviationInfo;
	private Map<String, Publication> publicationInfo;
	
	private Map<Integer, CoAbbreviation> coAbbreviations;
	
	public CoAbbreviationManager(String outputDirectory)
	{		
		this.outputDirectory = outputDirectory;
		
		abbreviationInfo = new HashMap<Integer, Abbreviation>();
		publicationInfo = new HashMap<String, Publication>();
		
		coAbbreviations = new HashMap<Integer, CoAbbreviation>();
	}
	
	public void loadBasicInfo() throws IOException
	{		
		AbbreviationReader abbreviationReader = new AbbreviationReader(outputDirectory);
		abbreviationInfo = abbreviationReader.getAbbreviations();
		
		PublicationReader publicationReader = new PublicationReader(outputDirectory);
		publicationInfo = publicationReader.getPublications();
	}
	
	public Map<Integer, CoAbbreviation> returnCoAbbreviationInfo()
	{
		return coAbbreviations;
	}
	
	public void organizeAll()
	{		
		organizeAbbreviationInfo();
		organizePublicationInfo();
	}

	public void organizeAbbreviationInfo()
	{
		if (coAbbreviations.isEmpty())
		{
			Iterator<Integer> iter = abbreviationInfo.keySet().iterator();
			while (iter.hasNext())
			{
				int abbreviationID = iter.next();
				Abbreviation abbreviation = abbreviationInfo.get(abbreviationID);
				
				String SF = abbreviation.getAbbreviation();
				List<String> appearsOn = abbreviation.getAppearsOn();
				
				CoAbbreviation coAbbreviation = new CoAbbreviation(abbreviationID, SF, 0, 0, null, null);
				if(coAbbreviations.containsKey(abbreviationID))
				{
					coAbbreviation = coAbbreviations.get(abbreviationID);
				}
				
				List<String> allPublications = new ArrayList<String>();
				if (coAbbreviation.getCoAbbreviations() != null)
				{
					allPublications = coAbbreviation.getPublications();
				}			
				
				for (int i = 0; i < appearsOn.size(); i++)
				{
					String publicationID = appearsOn.get(i);
					if (!allPublications.contains(publicationID))
					{
						allPublications.add(publicationID);
					}
				}			
				Collections.sort(allPublications);
				
				coAbbreviation.setPublications(allPublications);
				coAbbreviation.setPublicationFrequency(allPublications.size());
				
				coAbbreviations.put(abbreviationID, coAbbreviation);
			}
		}	
	}
	
	private void organizePublicationInfo() 
	{
		if (coAbbreviations.isEmpty())
		{
			organizeAbbreviationInfo();
		}
		
		// TODO Auto-generated method stub
		Iterator<String> iter = publicationInfo.keySet().iterator();
		while (iter.hasNext())
		{
			String publicationID = iter.next();
			Publication publication = publicationInfo.get(publicationID);
			
			List<Integer> abbreviations = publication.getAbbreviations();
			
			for (int i = 0; i < abbreviations.size(); i++)
			{
				int abbreviationID = abbreviations.get(i);
				
				CoAbbreviation coAbbreviation = coAbbreviations.get(abbreviationID);
				
				for (int j = 0; j < abbreviations.size(); j++)
				{
					if (i != j)
					{
						int coAbbreviationID = abbreviations.get(j);
						
						List<Integer> coAbbreviations = new ArrayList<Integer>();
						if (coAbbreviation.getCoAbbreviations() != null)
						{
							coAbbreviations = coAbbreviation.getCoAbbreviations();
						}
						if (!coAbbreviations.contains(coAbbreviationID))
						{
							coAbbreviations.add(coAbbreviationID);
						}
						Collections.sort(coAbbreviations);
						
						coAbbreviation.setCoAbbreviations(coAbbreviations);
						coAbbreviation.setAbbreviationFrequency(coAbbreviations.size());
					}
				}
				
				coAbbreviations.put(abbreviationID, coAbbreviation);
			}
		}
	}
	
}
