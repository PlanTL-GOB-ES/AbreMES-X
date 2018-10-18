package elements;

import java.util.ArrayList;
import java.util.List;

public class Abbreviation {

	private String abbreviation;
	private int frequency;
	private int abbreviationID;
	private List<Integer> definitions;
	private List<String> appearsOn;
	
	public Abbreviation()
	{
		abbreviation = new String();
		frequency = 0;
		abbreviationID = -1;
		definitions = new ArrayList<Integer>();
		appearsOn = new ArrayList<String>();
	}
	
	public Abbreviation(String abbreviation, int frequency, int abbreviationID, List<Integer> definitions, List<String> appearsOn)
	{
		this.abbreviation = abbreviation;
		this.frequency = frequency;
		this.abbreviationID = abbreviationID;
		this.definitions = definitions;
		this.appearsOn = appearsOn;
	}
	
	public void setAbbreviation(String abbreviation)
	{
		this.abbreviation = abbreviation;
	}
	
	public String getAbbreviation()
	{
		return abbreviation;
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
	
	public void setDefinitions(List<Integer> definitions)
	{
		this.definitions = definitions;
	}
	
	public List<Integer> getDefinitions()
	{
		return definitions;
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
