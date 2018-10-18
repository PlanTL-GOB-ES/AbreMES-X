package elements;

import java.util.ArrayList;
import java.util.List;

public class Pair {

	private int pairID;
	private String abbreviation;
	private String definition;
	private int frequency;
	private int abbreviationID;
	private int definitionID;
	private List<String> appearsOn;
	
	public Pair()
	{
		pairID = -1;
		abbreviation = new String();
		definition = new String();
		frequency = 0;
		abbreviationID = -1;
		definitionID = -1;
		appearsOn = new ArrayList<String>();
	}
	
	public Pair(int pairID, String abbreviation, String definition, int frequency, int abbreviationID, int definitionID, List<String> appearsOn)
	{
		this.pairID = pairID;
		this.abbreviation = abbreviation;
		this.definition = definition;
		this.frequency = frequency;
		this.abbreviationID = abbreviationID;
		this.definitionID = definitionID;
		this.appearsOn = appearsOn;
	}
	
	public void setPairID(int pairID)
	{
		this.pairID = pairID;
	}
	
	public int getPairID()
	{
		return pairID;
	}
	
	public void setAbbreviation(String abbreviation)
	{
		this.abbreviation = abbreviation;
	}
	
	public String getAbbreviation()
	{
		return abbreviation;
	}
	
	public void setDefinition(String definition)
	{
		this.definition = definition;
	}
	
	public String getDefinition()
	{
		return definition;
	}
	
	public void setFrequency(int frequency)
	{
		this.frequency = frequency;
	}
	
	public int getFrequency()
	{
		return frequency;
	}
	
	public void setAbbreviationID(int abbreviationID)
	{
		this.abbreviationID = abbreviationID;
	}
	
	public int getAbbreviationID()
	{
		return abbreviationID;
	}
	
	public void setDefinitionID(int definitionID)
	{
		this.definitionID = definitionID;
	}
	
	public int getDefinitionID()
	{
		return definitionID;
	}
	
	public void setAppearsOn(List<String> appearsOn)
	{
		this.appearsOn = appearsOn;
	}
	
	public List<String> getAppearsOn()
	{
		return appearsOn;
	}
}
