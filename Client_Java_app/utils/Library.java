
package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

public class Library
{
	public static final String userAgent = "Bravo Agent(Beta V2.04)";
	
	/**
	 * A simple method to write an error to a text file to be examined later
	 * @param name This will be the file name and should be unique
	 * @param error The thrown error or a custom error that will be written to the file
	 */
	public static void reportError(String name, String error)
	{
		String file = name + ".txt";
		File check = new File("./" + file);
		int i = 1;

		while (check.exists())
		{
			i++;
			file = name + "-" + i + ".txt";
			check = new File("./" + file);
		}
		
		PrintWriter out = null;

		try
		{
			out = new PrintWriter(new FileWriter(file), true);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		out.write(error);
		out.close();
	}

	/**
	 * Finds all the Html A tags in the source and adds the Href part to an ArrayList
	 * @param source A String that contains valid Html A tags
	 * @return An ArrayList of strings that contains the Href part of the A tag in Html
	 */
	public static ArrayList<String> findHrefTags(String source)
	{
		Reader reader = new StringReader(source);
		HTMLEditorKit.Parser parser = new ParserDelegator();
		final ArrayList<String> links = new ArrayList<String>();

		try
		{
			parser.parse(reader, new HTMLEditorKit.ParserCallback()
			{
				public void handleStartTag(HTML.Tag t, MutableAttributeSet a,
						int pos)
				{
					if (t == HTML.Tag.A)
					{

						Object link = a.getAttribute(HTML.Attribute.HREF);
						if (link != null)
						{

							links.add(String.valueOf(link));
						}
					}
					// else if(t == HTML.Tag.LI)
					// {
					// Object link = a.getAttribute("a");
					// if(link != null)
					// {
					// System.out.println(link);
					// }
					// }

				}
			}, true);
		}
		catch (IOException e)
		{
			Library.reportError("hrefParser", e.toString());
		}
		
		try
		{
			reader.close();
		}
		catch (IOException e)
		{
			Library.reportError("hrefReaderClose", e.toString());
		}
		// System.out.println(links);

		return links;
	}
	
	/**
	 * Finds The next Occurrence or index of an item
	 * @param source the String that contains the elements looking for
	 * @param currentOccurrence the current index
	 * @param find the element that is being looked for
	 * @return the next index of the element
	 * @deprecated Should not be used as not fully implemented and tested
	 */
	public static int fNOO(String source, int currentOccurrence, String find)
	{
		return (source.substring(currentOccurrence)).indexOf(find);
	}
	
	/**
	 * Checks the see if a URL links to an actual web site that is reachable on the Internet 
	 * @param URL the URL to be tested
	 * @return True it the URL links to a valid web site, else it is false
	 */
	public static boolean exists(String URL)
	{
		if (URL.toLowerCase().startsWith("http://"))
		{
			return Library.normalexists(URL);
		}
		else
		{
			return Library.secureExists(URL);
		}
	}
	
	/**
	 * @deprecated Use Library.exists(String URL)
	 * @param URL the URL to be tested
	 * @return @see Library.exists(String URL)
	 */
	public static boolean secureExists(String URL)
	{
		try
		{
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con = (HttpURLConnection) new URL(URL)
					.openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	/**
	 * @deprecated Use Library.exists(String URL)
	 * @param URL the URL to be tested
	 * @return @see Library.exists(String URL)
	 */
	public static boolean normalexists(String URL)
	{
		try
		{
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con = (HttpURLConnection) new URL(URL)
					.openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	/**
	 * Merges two ArrayList and doesn't add duplicates, this add the elements of listTwo to listOne
	 * @param listOne An ArrayList of elements similar to listTwo
	 * @param listTwo An ArrayList of elements similar to listOne
	 * @return An array that has merged listOne and listTwo into one ArrayList
	 */
	public static ArrayList<String> merge(ArrayList<String> listOne,
			ArrayList<String> listTwo)
	{
		for (int i = 0; i < listTwo.size(); i++)
		{
			if (!(listOne.contains(listTwo.get(i))))
			{
				listOne.add(listTwo.get(i));
			}
		}
		
		return listOne;
	}

	/**
	 * Merges a valid String representation of a URL with a valid extension of the URL
	 * @param url A valid String representation of a URL
	 * @param extension A valid extension to a URL either directory or file
	 * @return A merged String representation of the URL
	 */
	public static String mergeUrl(String url, String extension)
	{
		if (url.endsWith("/") && extension.startsWith("/"))
		{
			return url + extension.substring(extension.indexOf("/") + 1);
		}
		else
		{
			if (((!(url.endsWith("/")) && extension.startsWith("/")))
					|| ((url.endsWith("/") && !(extension.startsWith("/")))))
			{
				return url + extension;
			}
			else
			{
				return url + "/" + extension;
			}
		}
	}
	
	/**
	 * Removes the leading protocol of a valid String representation of a URL
	 * Note: Only used to remove http:// and https://, else it returns a String of Null
	 * @param string A valid String representation of a URL
	 * @return A String with no leading protocol
	 */
	public static String stripProtocol(String string)
	{
		if(string.contains("://") || string.startsWith("//"))
		{
			return string.substring((string.indexOf("//")+2));
		}
		else
		{
			return string;
		}
	}

	/**
	 * Removes the protocol and leading www. from the valid String representation of a URL
	 * @param string A valid String representation of a URL
	 * @return The base String representation of the URL without www. and it's protocol 
	 */
	public static String stripUrl(String string)
	{
		String temp_string;
		String test = string.substring(string.indexOf("//") + 2);
		if ((test.toLowerCase()).startsWith("www.")) //||(string.substring(string.indexOf("://")) + 3).toLowerCase().startsWith("www."))
		{
			temp_string = string
					.substring(string.toLowerCase().indexOf("www.") + 4);
		
			if (temp_string.contains("/"))
			{
				return temp_string.substring(0, temp_string.indexOf("/"));
			}
			else if(temp_string.contains(":"))
			{
				return temp_string.substring(0, temp_string.indexOf(":"));
			}
			else if(temp_string.contains("?"))
			{
				return temp_string.substring(0, temp_string.indexOf("?"));
			}
			else 
			{
				return temp_string;
			}
		}
		else
		{
			temp_string = Library.stripProtocol(string);
			if (temp_string.contains("/"))
			{
				return temp_string.substring(0, temp_string.indexOf("/"));
			}
			else if(temp_string.contains(":"))
			{
				return temp_string.substring(0, temp_string.indexOf(":"));
			}
			else if(temp_string.contains("?"))
			{
				return temp_string.substring(0, temp_string.indexOf("?"));
			}
			else
			{
				return temp_string;
			}
		}
	}
	
	/**
	 * Created as a proof of concept before I knew that String.lastIndexOf existed
	 * @deprecated use String method lastIndexOF
	 * @param source
	 * @param lookingFor
	 * @return
	 */
	public static int lastIndexOf(String source, String lookingFor)
	{
		int index = 0;
		String string = source;
		
		while(string.contains(lookingFor))
		{
			index += (string.indexOf(lookingFor) + 1);
			string = source.substring(index);
		}
		
		if(index == 0)
		{
			return -1;
		}
		else
		{
			return index;
		}
	}

	/**
	 * A critical method for application to work, allows a URL without a leading forward slash to be parsed into a valid String representation of the URL
	 * @param url A valid String representation of a URL
	 * @param extra A leading extension without a leading forward slash
	 * @return A valid string representation of a URL
	 */
	public static String dumbURL(String url,String extra)
	{
		String tempURL = new String(url);
		int index = Library.lastIndexOf(url,"/");
		//System.out.println("URL:" + url);
		//System.out.println("SUBSTRING:"+ url.substring(0,index));
		//System.out.println("END:" + tmp.get(i));
		if (((url.substring(0, index)).equalsIgnoreCase("http://"))
				|| ((url.substring(0, index).equalsIgnoreCase(("https://")))))
		{
			//System.out.println("TEST");
			tempURL = tempURL + "/";
			index = tempURL.lastIndexOf("/") ;
			//System.out.println(tempURL);
		}
		
		return(Library.mergeUrl(tempURL.substring(0,index), extra));
	}
	
	
	/**
	 * Removes the Domain from the URL and returns the extension of the URL
	 * @param string A valid string representation of a URL
	 * @return The extension of the String
	 */
	public static String uRLExtension(String string)
	{
		if(string.contains("://"))
		{
			if (((string.substring(string.indexOf("://")) + 3).toLowerCase()).startsWith("www."))
			{
				String tmp = string
						.substring(string.toLowerCase().indexOf("www.") + 4);
				if (tmp.contains("/"))
				{
					return tmp.substring(tmp.indexOf("/"));
				}
			}
			else
			{
				String tmp = Library.stripProtocol(string);
				
				if (tmp.contains("/"))
				{
					return tmp.substring(tmp.indexOf("/"));
				}
			}
		}
		else if(string.startsWith("//"))
		{
			if ((string.substring(string.indexOf("//")) + 2).toLowerCase().startsWith("www."))
			{
				String tmp = string
						.substring(string.toLowerCase().indexOf("www.") + 4);
				
				if (tmp.contains("/"))
				{
					return tmp.substring(tmp.indexOf("/"));
				}
			}
			else
			{
				String tmp = Library.stripProtocol(string);
				
				if (tmp.contains("/"))
				{
					return tmp.substring(tmp.indexOf("/"));
				}
			}
		}
		
		return string;
	}

}
