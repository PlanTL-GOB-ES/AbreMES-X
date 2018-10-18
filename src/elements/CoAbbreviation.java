package elements;

import java.util.ArrayList;
import java.util.List;

public class CoAbbreviation {
	
	private int abbreviationID;	
	private String abbreviation;
	private int abbreviationFrequency;
	private int publicationFrequency;
	private List<Integer> coAbbreviations;
	private List<String> publications;
	
	public CoAbbreviation()
	{
		abbreviation = new String();
		abbreviationID = -1;
		abbreviationFrequency = 0;
		publicationFrequency = 0;
		coAbbreviations = new ArrayList<Integer>();
		publications = new ArrayList<String>();
	}
	
	public CoAbbreviation(int abbreviationID, String abbreviation, int abbreviationFrequency, int publicationFrequency, 
			List<Integer> coAbbreviations, List<String> publications)
	{
		this.abbreviationID = abbreviationID;
		this.abbreviation = abbreviation;
		this.abbreviationFrequency = abbreviationFrequency;
		this.publicationFrequency = publicationFrequency;
		this.coAbbreviations = coAbbreviations;
		this.publications = publications;
	}
	
	public void setAbbreviationID(int abbreviationID)
	{
		this.abbreviationID = abbreviationID;
	}
	
	public int getAbbreviationID()
	{
		return abbreviationID;
	}
		
	public void setAbbreviation(String abbreviation)
	{
		this.abbreviation = abbreviation;
	}
	
	public String getAbbreviation()
	{
		return abbreviation;
	}
	
	public void setAbbreviationFrequency(int abbreviationFrequency)
	{
		this.abbreviationFrequency = abbreviationFrequency;
	}
	
	public int getAbbreviationFrequency()
	{
		return abbreviationFrequency;
	}
	
	public void setPublicationFrequency(int publicationFrequency)
	{
		this.publicationFrequency = publicationFrequency;
	}
	
	public int getPublicationFrequency()
	{
		return publicationFrequency;
	}
	
	public void setCoAbbreviations(List<Integer> coAbbreviations)
	{
		this.coAbbreviations = coAbbreviations;
	}
	
	public List<Integer> getCoAbbreviations()
	{
		return coAbbreviations;
	}
	
	public void setPublications(List<String> publications)
	{
		this.publications = publications;
	}
	
	public List<String> getPublications()
	{
		return publications;
	}
}
