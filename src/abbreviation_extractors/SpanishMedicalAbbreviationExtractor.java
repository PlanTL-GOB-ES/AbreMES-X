package abbreviation_extractors;

import java.text.Collator;
import java.util.*;

/**
 * Copyright (c) 2003, Regents of the University of California
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the
 *   distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
**/

/**
 * The ExtractAbbrev class implements a simple algorithm for
 * extraction of abbreviations and their definitions from biomedical text.
 * Abbreviations (short forms) are extracted from the input file, and those abbreviations
 * for which a definition (long form) is found are printed out, along with that definition,
 * one per line.
 *
 * A file consisting of short-form/long-form pairs (tab separated) can be specified
 * in tandem with the -testlist option for the purposes of evaluating the algorithm.
 *
 * @see <a href="http://biotext.berkeley.edu/papers/psb03.pdf">A Simple Algorithm for Identifying Abbreviation Definitions in Biomedical Text</a> 
 * A.S. Schwartz, M.A. Hearst; Pacific Symposium on Biocomputing 8:451-462(2003) 
 * for a detailed description of the algorithm.  
 *
 * @author Ariel Schwartz
 * @version 03/12/03
 * 
 * @updated 07/20/16 by Marti Hearst to include BSD License.
 */
public class SpanishMedicalAbbreviationExtractor {

	private String sentence;
	private Map<String, Integer> geonames;
	
	public SpanishMedicalAbbreviationExtractor(String sentence, Map<String, Integer> geonames)
	{
		this.sentence = sentence;
		this.geonames = geonames;
	}
	
	public Map<String, String> extractPairs()
	{
		Map<String, String> pairs = new HashMap<String, String>();
		
		String tmpStr, longForm = "", shortForm = "";		
		int openParenIndex, closeParenIndex = -1, sentenceEnd, newCloseParenIndex, tmpIndex = -1;
		StringTokenizer shortTokenizer;
		
		sentence += " ";
		openParenIndex = sentence.indexOf(" (");
		
		do 
		{
		    if (openParenIndex > -1)
		    	openParenIndex++;
		    
		    sentenceEnd = sentence.length();
		    
		    if ((openParenIndex == -1) && (sentenceEnd == -1)) 
		    {
		    	//Do nothing
		    }
/*		    else if (openParenIndex == -1) 
		    {
				sentence = sentence.substring(sentenceEnd + 2);
			} */
		    else if ((closeParenIndex = sentence.indexOf(')',openParenIndex)) > -1)
		    {
		    	sentenceEnd = Math.max(sentence.lastIndexOf(". ", openParenIndex), sentence.lastIndexOf(", ", openParenIndex));
			
				if (sentenceEnd == -1)
				    sentenceEnd = -2;
				
				if (openParenIndex == -1 && closeParenIndex > 0)
				{
					// Los efectos de: a) tiempo hasta recurrencia; b) uso de coils, (...)
					// do nothing
				}
				else
				{
					try
					{
						longForm = sentence.substring(sentenceEnd + 2, openParenIndex);
						shortForm = sentence.substring(openParenIndex + 1, closeParenIndex);
					}
					catch (Exception e)
					{
						// do nothing
					}
				}
				
			}
		    
		    if (shortForm.length() > 0 || longForm.length() > 0) 
		    {
				if (shortForm.length() > 1 && longForm.length() > 1) 
				{
				    if ((shortForm.indexOf('(') > -1) && 
					((newCloseParenIndex = sentence.indexOf(')', closeParenIndex + 1)) > -1))
				    {
				    	shortForm = sentence.substring(openParenIndex + 1, newCloseParenIndex);
				    	closeParenIndex = newCloseParenIndex;
				    }
				    
				    if ((tmpIndex = shortForm.indexOf(", ")) > -1)
				    	shortForm = shortForm.substring(0, tmpIndex);
				    
				    if ((tmpIndex = shortForm.indexOf("; ")) > -1)
				    	shortForm = shortForm.substring(0, tmpIndex);
				    
				    shortTokenizer = new StringTokenizer(shortForm);
				    
				    if (shortTokenizer.countTokens() > 2 || shortForm.length() > longForm.length()) 
				    {
				    	// Long form in ( )
				    	tmpIndex = sentence.lastIndexOf(" ", openParenIndex - 2);
				    	tmpStr = sentence.substring(tmpIndex + 1, openParenIndex - 1);
				    	longForm = shortForm;
				    	shortForm = tmpStr;
				    	
				    	if (! hasCapital(shortForm))
				    		shortForm = "";
				    }
				    				    
				    if (isValidShortForm(shortForm))
				    {
				    	String pair = extractAbbrPair(shortForm.trim(), longForm.trim());
				    	if (pair != null)
				    	{
				    		String SF = pair.split("\t")[0];
				    		 
				    		/*
				    		 * Here we check if the potential abbreviation is a geophysical location. If so, we discard it. 
				    		 */
				    		if (!geonames.containsKey(SF))
				    		{
				    			/*
				    			 * We may find the acronym AU (author) in the end of some articles. 
				    			 * We discard these acronyms only if we are not in the last sentence of the abstract.
				    			 */
				    			if (!SF.equalsIgnoreCase("AU"))
					    		{
				    				/* 
				    				 * Some other detected "abbreviations" are not exactly abbreviations. They specify patients groups.
				    				 * We discard these mentions as well.  
				    				 */
				    				if (!SF.startsWith("Grupo") && !SF.startsWith("grupo"))
				    				{
				    					String LF = pair.split("\t")[1];
							    		
							    		LF = LF.trim().replaceAll(" +", " ");
							    		
							    		boolean allLowerCase = testAllLowerCase(SF);
							    		
							    		if (!allLowerCase)
							    		{
							    			/*
							    			 * Some detected abbreviations and definitions contain incorrectly encoded characters. 
							    			 * The following lines replace those characters with their corresponding ones.
							    			 */
							    			SF = SF.replace("&#945;", "α").replace("", "•");
									    	LF = LF.replace("", "'").replace("", "'").replace("", "'").replace("&#945;", "α").replace("", "û");
									    		
									    	pairs.put(SF, LF);				    			
							    		}		
				    				}				    				
					    		}	
				    			else
				    			{				    				
			    					String LF = pair.split("\t")[1];
						    		
						    		LF = LF.trim().replaceAll(" +", " ");
						    		
						    		boolean allLowerCase = testAllLowerCase(SF);
						    		
						    		if (!allLowerCase)
						    		{
						    			/*
						    			 * Some detected abbreviations and definitions contain incorrectly encoded characters. 
						    			 * The following lines replace those characters with their corresponding ones.
						    			 */
						    			SF = SF.replace("&#945;", "α").replace("", "•");
								    	LF = LF.replace("", "'").replace("", "'").replace("", "'").replace("&#945;", "α");
								    		
								    	pairs.put(SF, LF);				    			
						    		}						    							    				
				    			}
				    		}
				    		else
				    		{
				    			if (SF.length() > 3)
				    			{
				    				System.out.println("Detected abbreviation is a geophysical location: " + SF + ". This mention will be excluded from the database.");
				    			}
				    			else
				    			{
				    				/*
				    				 * Some short abbreviations, with less than 3 characters, may have coincidence with geophyisical 
				    				 * abbreviations. We keep these abbreviations because we did not find any issues with them, 
				    				 * but we still recommend reviewing them just in case.
				    				 *
				    				 * Uncomment the following line to display these abbreviations.
				    				 */
			//	    				System.out.println("Possible geophysical location detected, please check: " + SF);
				    				if (!SF.equals("AU") && !SF.equals("au"))
						    		{
						    			String LF = pair.split("\t")[1];
							    		
							    		LF = LF.trim().replaceAll(" +", " ");
							    		
							    		boolean allLowerCase = testAllLowerCase(SF);
							    		
							    		if (!allLowerCase)
							    		{
							    			SF = SF.replace("&#945;", "α").replace("", "•");
									    	LF = LF.replace("", "'").replace("", "'").replace("", "'").replace("&#945;", "α");
									    		
									    	pairs.put(SF, LF);				    			
							    		}		
						    		}	
				    			}
				    		}
				    	}
				    }
				}
				sentence = sentence.substring(closeParenIndex + 1);
			} 
		    else if (openParenIndex > -1) 
		    {
				if ((sentence.length() - openParenIndex) > 200)
				    // Matching close paren was not found
					sentence = sentence.substring(openParenIndex + 1);
				
				break; // Read next line
			}	
		    
		    shortForm = "";
		    longForm = "";
		}
		while ((openParenIndex =  sentence.indexOf(" (")) > -1);
		
		return pairs;
	}
	
	private boolean hasCapital(String str) 
	{
		for (int i=0; i < str.length() ; i++)
		    if (Character.isUpperCase(str.charAt(i)))
		    	return true;
		
		return false;
	}
	
	private boolean testAllLowerCase(String str)
	{		
		for(int i=0; i<str.length(); i++){
			char c = str.charAt(i);
			if(c < 97 || c > 122) {
				return false;
			}
		}
		//str.charAt(index)
		return true;
	}
	
	private boolean testFirstUpperNextLowers(String str)
	{
		boolean firstUpperNextLowers = false;
		
		char first = str.charAt(0);
		if (first >= 65 && first <= 90)
		{
			firstUpperNextLowers = true;
		}
		
		if (firstUpperNextLowers)
		{
			for(int i=1; i<str.length(); i++){
				char c = str.charAt(i);
				if(c >= 97 && c <= 122) {
					firstUpperNextLowers = true;
				}
			}
			
			return firstUpperNextLowers;
		}
		else
		{
			return firstUpperNextLowers;
		}		
	}
	
	private boolean isValidShortForm(String str) 
	{
		return (hasLetter(str) && (Character.isLetterOrDigit(str.charAt(0)) || (str.charAt(0) == '(')));
	}
	
	private boolean hasLetter(String str) 
	{
		for (int i=0; i < str.length() ; i++)
		    if (Character.isLetter(str.charAt(i)))
		    	return true;
		
		return false;
	}
	
	private String extractAbbrPair(String shortForm, String longForm) 
	{
		String bestLongForm;
		StringTokenizer tokenizer;
		int longFormSize, shortFormSize;

		if (shortForm.length() == 1)
		    return null;
		
		bestLongForm = findBestLongForm(shortForm, longForm);
		if (bestLongForm == null)
		    return null;
		
		tokenizer = new StringTokenizer(bestLongForm, " \t\n\r\f-");
		longFormSize = tokenizer.countTokens();
		shortFormSize = shortForm.length();
		
		for (int i=shortFormSize - 1; i >= 0; i--)
		    if (!Character.isLetterOrDigit(shortForm.charAt(i)))
		    	shortFormSize--;
		
		if (bestLongForm.length() < shortForm.length() || 
		    bestLongForm.indexOf(shortForm + " ") > -1 ||
		    bestLongForm.endsWith(shortForm) ||
		    longFormSize > shortFormSize * 2 ||
		    longFormSize > shortFormSize + 5 ||
		    shortFormSize > 10)
		    	return null;

		return shortForm + "\t" + bestLongForm;
	}
	
	private String findBestLongForm(String shortForm, String longForm) 
	{
		int sIndex;
		int lIndex;
		char currChar;

		sIndex = shortForm.length() - 1;
		lIndex = longForm.length() - 1;
		for ( ; sIndex >= 0; sIndex--) 
		{
		    currChar = Character.toLowerCase(shortForm.charAt(sIndex));
		    
		    if (!Character.isLetterOrDigit(currChar))
		    	continue;
		    
		    Collator instance = Collator.getInstance();
		    instance.setStrength(Collator.NO_DECOMPOSITION);
		    
		    while (((lIndex >= 0) && ((Character.toLowerCase(longForm.charAt(lIndex)) != currChar))) ||
			   ((sIndex == 0) && (lIndex > 0) && (Character.isLetterOrDigit(longForm.charAt(lIndex - 1)))))
		    {
		    	if (instance.compare(longForm.substring(lIndex, lIndex + 1), "" + currChar) == 0)
		    	{
		    		break;
		    	}
		    	else
		    	{
		    		lIndex--;
		    	}		    	
		    }
		    
		    if (lIndex < 0)
		    	return null;
		    
		    lIndex--;
		}
		lIndex = longForm.lastIndexOf(" ", lIndex) + 1;
		return longForm.substring(lIndex);
	}
}


