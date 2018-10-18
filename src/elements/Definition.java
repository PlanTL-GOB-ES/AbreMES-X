package elements;

import java.util.ArrayList;
import java.util.List;

public class Definition {
	
	private String definition;
	private int frequency;
	private int definitionID;
	private List<Integer> abbreviations;
	private List<String> appearsOn;
	
	public Definition()
	{
		definition = new String();
		frequency = 0;
		definitionID = -1;
		abbreviations = new ArrayList<Integer>();
		appearsOn = new ArrayList<String>();
	}
	
	public Definition(String definition, int frequency, int definitionID, List<Integer> abbreviations, List<String> appearsOn)
	{
		this.definition = definition;
		this.frequency = frequency;
		this.definitionID = definitionID;
		this.abbreviations = abbreviations;
		this.appearsOn = appearsOn;
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
	
	public void setDefinitionID(int definitionID)
	{
		this.definitionID = definitionID;
	}
	
	public int getLongFormID()
	{
		return definitionID;
	}
	
	public void setAbbreviations(List<Integer> abbreviations)
	{
		this.abbreviations = abbreviations;
	}
	
	public List<Integer> getAbbreviations()
	{
		return abbreviations;
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
