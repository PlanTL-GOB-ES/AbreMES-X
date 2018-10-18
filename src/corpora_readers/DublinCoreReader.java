package corpora_readers;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import elements.Publication;

public class DublinCoreReader {

	private String file;	
	private String corpusName;
	
	private List<String> JSTs;
	
	private Publication publication;
	
	public DublinCoreReader(String file, String corpusName, List<String> JSTs)
	{
		this.file = file;
		this.corpusName = corpusName;
		this.JSTs = JSTs;
		publication = new Publication();
		
		publication.setSource(corpusName);
	}
	
	public void readFile() throws ParserConfigurationException, SAXException, IOException
	{		
		File dcFile = new File(file);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		dBuilder.setEntityResolver(new EntityResolver() {

            @Override
            public InputSource resolveEntity(String publicId, String systemId)
                    throws SAXException, IOException {
      //          System.out.println("Ignoring " + publicId + ", " + systemId);
                return new InputSource(new StringReader(""));
            }
        });
		Document doc = dBuilder.parse(dcFile);		
		doc.getDocumentElement().normalize();
		
		publication.setSource(corpusName);
		
		Node IDnode = doc.getElementsByTagName("dc:identifier").item(0);
		publication.setID(IDnode.getTextContent());
		
		Node ISSNnode = doc.getElementsByTagName("setSpec").item(0);
		if (ISSNnode == null)
		{		
			try
			{
				String ID = IDnode.getTextContent().split("=")[1];
				String ISSN = ID.substring(1, 10);
				publication.setISSN(ISSN);
			}
			catch (Exception e)
			{
	//			System.out.println(IDnode.getTextContent());
	//			e.printStackTrace();
				publication.setISSN("<null>");
			}
		}
		else
		{
			publication.setISSN(ISSNnode.getTextContent());
		}		
		
		Node dateNode = doc.getElementsByTagName("dc:date").item(0);
		if (dateNode == null)
		{
			publication.setDate("<null>");
		}
		else
		{
			publication.setDate(dateNode.getTextContent());
		}
		
		Node publisherNode = doc.getElementsByTagName("dc:publisher").item(0);
		if (publisherNode == null)
		{
			publication.setPublisher("<null>");
		}
		else
		{
			publication.setPublisher(publisherNode.getTextContent());
		}		
		
		List<String> subjects =  new ArrayList<String>();		
		NodeList subjectNodes = doc.getElementsByTagName("dc:subject");
		for (int i = 0; i < subjectNodes.getLength(); i++)
		{
			String subject = subjectNodes.item(i).getTextContent();
			subjects.add(subject);
		}
		List<String> publicationJSTs = getJST(subjects);
		publication.setJST(publicationJSTs);
		
		NodeList titleNodes = doc.getElementsByTagName("dc:title");
		for (int i = 0; i < titleNodes.getLength(); i++)
		{
			if (titleNodes.item(i).hasAttributes())
			{
				String language = titleNodes.item(i).getAttributes().getNamedItem("xml:lang").getTextContent();
				if (language.equals("es"))
				{
					publication.setTitle(titleNodes.item(i).getTextContent());
				}
			}
			else
			{
				publication.setTitle(titleNodes.item(i).getTextContent());
			}
		}
		
		NodeList summaryNodes = doc.getElementsByTagName("dc:description");
		for (int i = 0; i < summaryNodes.getLength(); i++)
		{
			if (summaryNodes.item(i).hasAttributes())
			{
				String language = summaryNodes.item(i).getAttributes().getNamedItem("xml:lang").getTextContent();
				if (language.equals("es"))
				{
					publication.setSummary(summaryNodes.item(i).getTextContent());
				}
			}
			else
			{
				publication.setSummary(summaryNodes.item(i).getTextContent());
			}
		}
	}
	
	public List<String> getJST(List<String> subjects)
	{
		List<String> publicationJSTs = new ArrayList<String>();
		for (int i = 0; i < subjects.size(); i++)
		{
			String subject = subjects.get(i);
			if (JSTs.contains(subject))
			{
				publicationJSTs.add(subject);
			}
		}
		return publicationJSTs;
	}
	
	public Publication returnPublicationInfo()
	{
		return publication;
	}
}
