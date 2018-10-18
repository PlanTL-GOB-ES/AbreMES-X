package elements;

import java.util.ArrayList;
import java.util.List;

public class Publication {

	private String ID;
	private String title;
	private String summary;
	private String date;
	private String publisher;
	private String ISSN;
	private String source;
	private List<String> JST;	
	private List<Integer> abbreviations;
	private List<Integer> definitions;
	private List<Integer> pairs;
	
	public Publication()
	{
		ID = new String();
		title = new String();
		summary = new String();
		date = new String();
		publisher = new String();
		ISSN = new String();
		source = new String();
		JST = new ArrayList<String>();
		abbreviations = new ArrayList<Integer>();
		definitions = new ArrayList<Integer>();
		pairs = new ArrayList<Integer>();
	}
	
	public Publication(String ID, String title, String summary, String date, String publisher, String ISSN, String source, List<String> JST, 
			List<Integer> abbreviations, List<Integer> definitions, List<Integer> pairs)
	{
		this.ID = ID;
		this.title = title;
		this.summary = summary;
		this.date = date;
		this.publisher = publisher;
		this.ISSN = ISSN;
		this.source = source;
		this.JST = JST;
		this.abbreviations = abbreviations;
		this.definitions = definitions;
		this.pairs = pairs;
	}
	
	public void setID(String ID)
	{
		this.ID = ID;
	}
	
	public String getID()
	{
		return ID;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
	
	public String getSummary()
	{
		return summary;
	}
	
	public void setDate(String date)
	{
		this.date = date;
	}
	
	public String getDate()
	{
		return date;
	}
	
	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}
	
	public String getPublisher()
	{
		return publisher;
	}
	
	public void setISSN(String ISSN)
	{
		this.ISSN = ISSN;
	}
	
	public String getISSN()
	{
		return ISSN;
	}
	
	public void setSource(String source)
	{
		this.source = source;
	}
	
	public String getSource()
	{
		return source;
	}
	
	public void setJST(List<String> JST)
	{
		this.JST = JST;
	}
	
	public List<String> getJST()
	{
		return JST;
	}
	
	public void setAbbreviations(List<Integer> abbreviations)
	{
		this.abbreviations = abbreviations;
	}
	
	public List<Integer> getAbbreviations()
	{
		return abbreviations;
	}
	
	public void setDefinitions(List<Integer> definitions)
	{
		this.definitions = definitions;
	}
	
	public List<Integer> getDefinitions()
	{
		return definitions;
	}
	
	public void setPairs(List<Integer> pairs)
	{
		this.pairs = pairs;
	}
	
	public List<Integer> getPairs()
	{
		return pairs;
	}
}
