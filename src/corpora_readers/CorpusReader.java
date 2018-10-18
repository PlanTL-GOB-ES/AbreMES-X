package corpora_readers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import elements.Publication;

public class CorpusReader {

	private String corpusName;
	private String path;
	private List<String> JSTs;
	
	private int numPublications = 0;
	
	private List<Publication> publications;
	
	public CorpusReader(String corpusName, String path, List<String> JSTs)
	{
		this.corpusName = corpusName;
		this.path = path;
		this.JSTs = JSTs;
		
		publications = new ArrayList<Publication>();
	}
	
	public void getCorpusFiles() throws ParserConfigurationException, SAXException, IOException
	{
		File dir = new File(path);
		File files[] = dir.listFiles();
		
		for (int i = 0; i < files.length; i++)
		{			
			File file = files[i];
			
			if (file.isDirectory())
			{
				File folderFiles[] = file.listFiles();
				for (int j = 0; j < folderFiles.length; j++)
				{
					File subFile = folderFiles[j];
					String name = subFile.getName();
					DublinCoreReader reader = new DublinCoreReader(path + File.separator + file.getName() + File.separator + name, corpusName, JSTs);
					reader.readFile();
					Publication publication = reader.returnPublicationInfo();
					publications.add(publication);
					numPublications++;
				}
			}
			else
			{
				String name = file.getName();
				DublinCoreReader reader = new DublinCoreReader(path + File.separator + name, corpusName, JSTs);
				reader.readFile();
				Publication publication = reader.returnPublicationInfo();
				publications.add(publication);
				numPublications++;
			}			
		}
	}
	
	public List<Publication> returnPublications()
	{
		return publications;
	}
	
	public int returnNumberPublications()
	{
		return numPublications;
	}
}
