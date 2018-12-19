package abbreviation_extractors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Example {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String sentence = "Se le diagnosticó fibrosis pulmonar idiopática (FPI) y neumonía organizativa criptogenética (NOC).";
		
		Map<String, Integer> geonames = readGeonamesFile();
		
		// Extract all abbreviation from the input sentence
		SpanishMedicalAbbreviationExtractor SH = new SpanishMedicalAbbreviationExtractor(sentence, geonames);
		Map<String, String> pairs = SH.extractPairs();
		
		Iterator<String> iter = pairs.keySet().iterator();
		while (iter.hasNext())
		{
			String abbreviation = iter.next();
			String definition = pairs.get(abbreviation);
			System.out.println(abbreviation + "\t" + definition);
		}
	}

	/*
	 * Loads geographical information, in order to detect extracted abbreviations that belong to places around the world.
	 */
	public static Map<String, Integer> readGeonamesFile() throws IOException
	{
		System.out.println("Loading geographical names file...");
		Map<String, Integer> geonames = new HashMap<String, Integer>();
		
		
		ZipFile zipFile = new ZipFile("geonames.zip");
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
		return geonames;
	}
}
