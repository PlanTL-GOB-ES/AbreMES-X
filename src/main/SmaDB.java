package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import abbreviation_extractors.SchwartzHearstAlgorithm;
import corpora_readers.CorpusReader;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import elements.Publication;
import tabular_file_writers.AbbreviationWriter;
import tabular_file_writers.CoAbbreviationWriter;
import tabular_file_writers.DefinitionWriter;
import tabular_file_writers.PairWriter;
import tabular_file_writers.PublicationWriter;

public class SmaDB {
	
	private String mainDirectory;
	private String outputDirectory;
	
	private Map<String, String> corpora;
	private List<String> JSTs;
	private List<Publication> publications;
	private Map<String, Publication> allPublications;
	
	private Map<String, Integer> abbreviationIDs;
	private Map<String, Integer> definitionIDs;
	
	private Map<String, Map<String, String>> publicationsAndPairs;
	
	private int abbreviationCounter = 0;
	private int definitionCounter = 0;
	
	private Properties props;
	private StanfordCoreNLP pipeline;
	
	public SmaDB(String mainDirectory)
	{
		this.mainDirectory = mainDirectory;
		outputDirectory = mainDirectory + File.separator + "output";
		File theDir = new File(outputDirectory);
		if (!theDir.exists()) 
		{
			theDir.mkdir();
		}
		
		corpora = new HashMap<String, String>();
		JSTs = new ArrayList<String>();
		publications = new ArrayList<Publication>();
		allPublications = new HashMap<String, Publication>();
		
		publicationsAndPairs = new HashMap<String, Map<String, String>>();
		
		abbreviationIDs = new HashMap<String, Integer>();
		definitionIDs = new HashMap<String, Integer>();
		
		props = new Properties();
		props.put("annotators", "tokenize, ssplit");
		pipeline = new StanfordCoreNLP(props);
	}

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		// TODO Auto-generated method stub
		String mainDirectory = args[0];
		
		SmaDB allie = new SmaDB(mainDirectory);
		allie.start();
	}

	public void start() throws IOException, ParserConfigurationException, SAXException
	{
		System.out.println("Loading Journal Subject Terms...");
		loadJSTs();
		System.out.println("Done.");
		
		System.out.println("Reading corpora list...");
		readCorporaList();
		System.out.println("Done.");
		
		System.out.println("Loading corpora...");
		loadCorpora(); // read corpus, get titles and abstracts, get abbreviations, save abbreviations
		System.out.println("Done.");
		
		System.out.println("Creating preprocessed file...");
		createPreprocessedFile();
		System.out.println("Done.");
		
		writeBasics();
		
		System.out.println("Writing publications file...");
		writePublicationInfo();
		System.out.println("Done.");
		
		System.out.println("Writing co-abbreviations file...");
		writeCoAbbreviations();
		System.out.println("Done.");
	}
	
	public void loadJSTs() throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(mainDirectory + File.separator + "JST_spa.txt"));
		String line = "";
		while ((line = reader.readLine()) != null)
		{
			JSTs.add(line);
		}
		reader.close();
	}

	public void readCorporaList() throws IOException 
	{
		BufferedReader reader = new BufferedReader(new FileReader(mainDirectory + File.separator + "corpora_list.extended.txt"));
		String line = "";
		while ((line = reader.readLine()) != null)
		{
			String name = line.split("\t")[0];
			String path = line.split("\t")[1];
			corpora.put(name, path);
		}
		reader.close();
	}

	public void loadCorpora() throws ParserConfigurationException, SAXException, IOException 
	{	
		Map<String, Integer> corpusDocuments = new HashMap<String, Integer>();
		int numPublications = 0;
		
		Iterator<String> iter = corpora.keySet().iterator();
		while (iter.hasNext())
		{
			String corpusName = iter.next();
			String path = corpora.get(corpusName);
			
			CorpusReader corpusReader = new CorpusReader(corpusName, path, JSTs);
			corpusReader.getCorpusFiles();
			numPublications = numPublications + corpusReader.returnNumberPublications();
			
			List<Publication> corpusPublications = corpusReader.returnPublications();
			publications.addAll(corpusPublications);
			
			for (int i = 0; i < corpusPublications.size(); i++)
			{
				Publication publication = corpusPublications.get(i);
				String ID = publication.getID();
				allPublications.put(ID, publication);
				
				String source = publication.getSource();
				int freq = 0;
				if (corpusDocuments.containsKey(source))
				{
					freq = corpusDocuments.get(source);
				}
				freq++;
				corpusDocuments.put(source, freq);
			}
		}
		
		System.out.println("Total number of publications loaded: " + numPublications);
		Iterator<String> sourceIter = corpusDocuments.keySet().iterator();
		while (sourceIter.hasNext())
		{
			String source = sourceIter.next();
			int freq = corpusDocuments.get(source);
			System.out.println("\t" + source + "\t" + freq);
		}
	}
	
	public void createPreprocessedFile() throws IOException
	{
		extractAbbreviationDefinitionPairs();
		printPreprocessedDatabase();
	}
	
	public void extractAbbreviationDefinitionPairs() throws IOException
	{
		Map<String, Integer> geonames = readGeonamesFile();
		
		for (int i = 0; i < publications.size(); i++)
		{
			Publication publication = publications.get(i);
		
			String title = publication.getTitle();
			String summary = publication.getSummary();
			
			SchwartzHearstAlgorithm SHtitle;
			if (title.endsWith("(AU)"))
			{
				SHtitle = new SchwartzHearstAlgorithm(title, geonames, true);
			}
			else
			{
				SHtitle = new SchwartzHearstAlgorithm(title, geonames, false);
			}			
			Map<String, String> pairsTitle = SHtitle.extractPairs();			
			organizeIDs(pairsTitle);
			
			Map<String, String> pairs = new HashMap<String, String>();
			pairs.putAll(pairsTitle);
			
			Annotation document = new Annotation(summary);
			pipeline.annotate(document);
			List<CoreMap> sentences = document.get(SentencesAnnotation.class);
			int j = 0;
			for(CoreMap s: sentences) 
			{			
				String sentence = s.toString();
				
				SchwartzHearstAlgorithm SHsummary;
				if (j == sentences.size() - 1)
				{
					SHsummary = new SchwartzHearstAlgorithm(sentence, geonames, true);
				}
				else
				{
					SHsummary = new SchwartzHearstAlgorithm(sentence, geonames, false);
				}
				
				Map<String, String> pairsSummary = SHsummary.extractPairs();			
				organizeIDs(pairsSummary);
				
				pairs.putAll(pairsSummary);
				j++;
			}
			
			String ID = publication.getID();
			publicationsAndPairs.put(ID, pairs);
		}
	}
	
	public void organizeIDs(Map<String, String> pairs) 
	{
		Iterator<String> iter = pairs.keySet().iterator();
		while (iter.hasNext())
		{
			String abbreviation = iter.next();
			String definition = pairs.get(abbreviation);
			
			if (!abbreviationIDs.containsKey(abbreviation))
			{
				abbreviationCounter++;
				abbreviationIDs.put(abbreviation, abbreviationCounter);
			}
			
			if (!definitionIDs.containsKey(definition))
			{
				definitionCounter++;
				definitionIDs.put(definition, definitionCounter);
			}
		}
	}
	
	public Map<String, Integer> readGeonamesFile() throws IOException
	{
		System.out.println("Loading geographical names file...");
		Map<String, Integer> geonames = new HashMap<String, Integer>();
		
		
		ZipFile zipFile = new ZipFile(mainDirectory + File.separator + "geonames.zip");
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
	    while(entries.hasMoreElements())
	    {
	        ZipEntry entry = entries.nextElement();
	        if (entry.getName().equals("geonames.tsv"))
	        {
	        	BufferedReader br = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)));
	        	String line = "";	        	
	        	while ((line = br.readLine()) != null)
	    		{
	  //  			line = line.toLowerCase();
	    			try
	    			{
	    				String first = line.split("\t")[0];
		    			if (!first.equals(first.toUpperCase()))
		    			{
		    				geonames.put(first, 1);
		    			}	    			
		    			String second = line.split("\t")[1];
		    			if (!second.equals(second.toUpperCase()))
		    			{
		    				geonames.put(second, 1);
		    			}	    		
	    			}
	    			catch (Exception e)
	    			{
	    		//		System.out.println("Geonames error: " + line);
	    			}
	    		}
	    		br.close();
	        }
	        
	    }
		
		
	/*	BufferedReader reader = new BufferedReader(new FileReader(mainDirectory + File.separator + "allCountries.txt"));
		String line = "";
		while ((line = reader.readLine()) != null)
		{
			line = line.toLowerCase();
			
			String first = line.split("\t")[1];
			geonames.put(first, 1);
			String second = line.split("\t")[2];
			geonames.put(second, 1);
			String others[] = line.split("\t")[3].split(",");
			for (int i = 0; i < others.length; i++)
			{
				String other = others[i];
				geonames.put(other, 1);
			}
		}
		reader.close();*/
		
		return geonames;
	}

	public void printPreprocessedDatabase() throws IOException 
	{
		int numLine = 1;
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectory + File.separator + "preprocessed.tsv"));
		writer.write("# Line num." + "\t" + "Pub. ID" + "\t" + "Source" + "\t" + "Abbreviation ID" + "\t" + "Definition ID"
					+ "\t" + "Abbreviation" + "\t" + "Definition" + "\n");
		
		Iterator<String> iterID = publicationsAndPairs.keySet().iterator();
		while (iterID.hasNext())
		{
			String publicationID = iterID.next();
			Map<String, String> pairs = publicationsAndPairs.get(publicationID);
			
			String corpusName = allPublications.get(publicationID).getSource();
			
			Iterator<String> iterPair = pairs.keySet().iterator();
			while (iterPair.hasNext())
			{
				String abbreviation = iterPair.next();
				String definition = pairs.get(abbreviation);
				
				int abbreviationID = abbreviationIDs.get(abbreviation);
				int definitionID = definitionIDs.get(definition);
				
				writer.write(numLine + "\t" + publicationID + "\t" + corpusName + "\t" + abbreviationID + "\t" + definitionID + "\t" 
							+ abbreviation + "\t" + definition + "\n");
				
				numLine++;
			}
		}
		
		writer.close();
	}
	
	public void writeBasics() throws IOException
	{
		String preprocessedFile = outputDirectory + File.separator + "preprocessed.tsv";
		
		System.out.println("Writing abbreviations file...");
		AbbreviationWriter abbreviationWriter = new AbbreviationWriter(preprocessedFile, outputDirectory);
		abbreviationWriter.loadAbbreviations();
		abbreviationWriter.writeAbbreviations();
		System.out.println("Done.");
		
		System.out.println("Writing definitions file...");
		DefinitionWriter definitionWriter= new DefinitionWriter(preprocessedFile, outputDirectory);
		definitionWriter.loadDefinitions();
		definitionWriter.writeDefinitions();
		System.out.println("Done.");
		
		System.out.println("Writing pairs file...");
		PairWriter pairWriter= new PairWriter(preprocessedFile, outputDirectory);
		pairWriter.loadPairs();
		pairWriter.writePairs();
		System.out.println("Done.");
	}
	
	public void writePublicationInfo() throws IOException
	{
		PublicationWriter publicationWriter = new PublicationWriter(outputDirectory, allPublications);
		publicationWriter.getPublicationInfo();
		publicationWriter.printPublicationInfo();
	}
	
	public void writeCoAbbreviations() throws IOException
	{
		CoAbbreviationWriter coAbbreviationWriter = new CoAbbreviationWriter(outputDirectory);
		coAbbreviationWriter.getPublicationInfo();
		coAbbreviationWriter.printCoAbbreviationInfo();
	}
}
