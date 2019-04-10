/**
 * @author Nathan
 * @version 1
 */

package utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Website
{
	private String url;
	
	private ArrayList<String> scannedLinks = new ArrayList<String>();
	private ArrayList<String> externalLinks = new ArrayList<String>();
	private ArrayList<String> internalLinks = new ArrayList<String>();
	
	private String robotsDotTxt;
	private String source = "";
	private RobotsDotTxt parse;
	
	private int tmpNum;
	
	private String strippedUrl = "";
	
	private boolean stop = false;
	
	Website(String url, String[] links)
	{
		this.setUrl(url);
		this.strippedUrl = Library.stripUrl(this.getUrl());
	}
	
	/**
	 * Creates a Website object that can be scanned.
	 * 
	 * @param url
	 *            A valid URL that starts with either https:// or http://
	 */
	public Website(URL url)
	{
		this.setUrl(url.toString());
		this.checkBlockedLinks();
		this.strippedUrl = Library.stripUrl(this.getUrl());
	}
	
	

	/**
	 * Displays the source of a web site after it has been successfully been
	 * downloaded.
	 * 
	 * @return Returns String source
	 */
	public String getSource()
	{
		return source;
	}
	
	/**
	 * In case of special implementation, this allows a source to be edited
	 * locally to increase performance.
	 * 
	 * @param newSource
	 *            The new source to be scanned @see scan
	 */
	protected void setSource(String newSource)
	{
		source = newSource;
	}
	
	/**
	 * Checks the robots.txt of a valid web site and adds blocked links to
	 * ArrayList<String> blockedInternalLinks
	 */
	private void checkBlockedLinks()
	{
		if (getUrl().toLowerCase().startsWith("http://"))
		{
			if (getUrl().endsWith("/"))
			{
				robotsDotTxt = Downloader.download(getUrl() + "robots.txt");
			}
			else
			{
				robotsDotTxt = Downloader.download(getUrl() + "/robots.txt");
			}
			
			parse = new RobotsDotTxt(robotsDotTxt);
		}
		else
		{
			if (getUrl().toLowerCase().startsWith("https://"))
			{
				if (getUrl().endsWith("/"))
				{
					robotsDotTxt = Downloader.download(getUrl() + "robots.txt");
				}
				else
				{
					robotsDotTxt = Downloader.download(getUrl() + "/robots.txt");
				}
				
				parse = new RobotsDotTxt(robotsDotTxt);
			}
		}
	}
	
	/**
	 * Downloads the base source for the URL set in the constructor Just a local
	 * version of the Downloader.download method
	 * 
	 * @see Downloader.download
	 */
	public void download()
	{
		source = Downloader.download(getUrl());
	}
	
	/**
	 * This downloads the source of a web site and then parses it through the
	 * findHrefTags in the Library class to find all the href tags for the
	 * source of the URL.
	 * 
	 * @param url
	 *            a valid URL, starting with either http:// or https://
	 * @return An ArrayList<string> of all the href tags.
	 * @see Library.findHrefTags
	 */
	public ArrayList<String> parse(String url)
	{
		return Library.findHrefTags(Downloader.download(url));
	}
	
	/**
	 * Converts the ArrayList of blockedInternalLinks into a normal String
	 * array.
	 * 
	 * @return A String Array representation of the blockedInternalLinks
	 *         ArrayList
	 */
	public String[] blockedInternalLinks()
	{
		return parse.getParsedBlocked().toArray(
				new String[parse.getParsedBlocked().size()]);
	}
	
	/**
	 * Converts the ArrayList of allowedInternalLinks into a normal String
	 * array.
	 * 
	 * @return A String array representation of the allowedInternalLinks Array
	 */
	public String[] allowedInternalLinks()
	{
		return parse.getParsedAllowedLinks().toArray(
				new String[parse.getParsedAllowedLinks().size()]);
	}
	
	/**
	 * The main method of the class and starts the scan process of URL and
	 * subsequent files and directories and files on that URL.
	 */
	public void scan()
	{
		if (!(parse.getParsedBlocked().contains("/")))
		{
			//System.out.println(parse.getParsedBlocked());
			scan(getUrl());
			//System.out.println(internalLinks);
			for (int x = 0; x < internalLinks.size(); x++)
			{
				if(stop)
				{
					print("STOPPED");
					break;
				}
				scan(internalLinks.get(x));
				scannedLinks.add(internalLinks.get(x));
				tmpNum = x / internalLinks.size();
				//System.out.println(internalLinks);

	
			}
		}
		
		//print(internalLinks.toString());
		//print(externalLinks.toString());
	}
	
	/**
	 * A helper method for the scan method, parses all the href tags and makes
	 * them into valid URL's
	 * 
	 * @param url
	 *            URL to be completely scanned at that current iteration of
	 *            internalLink
	 */
	protected void scan(String url)
	{
		ArrayList<String> temp_list1 = new ArrayList<String>();
		ArrayList<String> temp_list2 = new ArrayList<String>();
		
		temp_list1.addAll(parse(url));
		//System.out.println(temp_list1);
		
		for (int i = 0; i < temp_list1.size(); i++)
		{
			if (stop)
			{
				break;
			}
			temp_list1.set(i, temp_list1.get(i).trim());
			// System.out.println(tmp.get(i));
			if (
					// && !(tmp.get(i).equalsIgnoreCase("/"))
					 !(scannedLinks.contains(temp_list1.get(i)))
					&& !(temp_list1.get(i).toLowerCase().startsWith("mailto:"))
					&& !(temp_list1.get(i).isEmpty())
					// &&!(tmp.get(i).toLowerCase().startsWith("javascript:"))
					&& !(temp_list1.get(i).toLowerCase().startsWith("#"))
					)
			{
				

			if(Library.stripUrl(temp_list1.get(i)).equals(url) || (temp_list1.get(i).startsWith("/") && !(temp_list1.get(i).startsWith("//"))))
					{
						if(parse.isBlocked(temp_list1.get(i)))
						{
							break;
						}
					}
				
				
				if (!(temp_list1.get(i).equalsIgnoreCase("/")))
				{
					print(temp_list1.get(i));
				}
				

				
				// System.out.println(tmpString);
				if (temp_list1.get(i).startsWith("/"))
				{
					if (temp_list1.get(i).startsWith("//"))
					{
						if (!(Library.stripUrl(temp_list1.get(i))
								.equals((strippedUrl))))
						{
							externalLinks.add("http:" + temp_list1.get(i));
							
							if (!(scannedLinks.contains("http:" + temp_list1.get(i))))
							{
								scannedLinks.add("http:" + temp_list1.get(i));
							}
							
							if (!(scannedLinks.contains(temp_list1.get(i))))
							{
								scannedLinks.add(temp_list1.get(i));
							}
						}
						else
						{
							temp_list2.add("http:" + temp_list1.get(i));
							
							if (!scannedLinks.contains("http:" + temp_list1.get(i)))
							{
								scannedLinks.add("http:" + temp_list1.get(i));
							}
							
							if (!(scannedLinks.contains(temp_list1.get(i))))
							{
								scannedLinks.add(temp_list1.get(i));
							}
						}
					}
					else
					{
						if (!(scannedLinks.contains(Library.mergeUrl(this.getUrl().trim(), temp_list1.get(i).trim()))))
						{
							temp_list2.add(Library.mergeUrl(this.getUrl().trim(), temp_list1.get(i).trim()));
							scannedLinks.add(Library.mergeUrl(this.getUrl().trim(), temp_list1.get(i).trim()));
						}
					}
				}
				else
				{
				
					if (temp_list1.get(i).startsWith("../"))
					{
						if (!(scannedLinks.contains(temp_list1.get(i))))
						{
							scannedLinks.add(temp_list1.get(i));
						}
						
						String remove = temp_list1.get(i);

						while (remove.contains("../"))
						{
							remove = remove.substring(remove.indexOf("../") + 3);
						}
						
						if (!(scannedLinks.contains(Library.mergeUrl(this.getUrl(), remove))))
						{
							temp_list2.add(Library.mergeUrl(this.getUrl().trim(), remove.trim()));
							scannedLinks.add(Library.mergeUrl(this.getUrl().trim(), remove.trim()));
						}
					}
					else
					{
						if (!(temp_list1.get(i).startsWith("#")))
						{
							if (temp_list1.get(i).contains("://"))
							{
								// tmpString = tmp.get(i).substring(
								// tmp.get(i).indexOf("://") + 3);
								if ((Library.stripUrl(temp_list1.get(i)).equals((strippedUrl))))
								{
									temp_list2.add(temp_list1.get(i));
									
									if (!(scannedLinks.contains(temp_list1.get(i))))
									{
										scannedLinks.add(temp_list1.get(i));
									}
								}
								else
								{
									
									externalLinks.add(temp_list1.get(i));
									
									if (!(scannedLinks.contains(temp_list1.get(i))))
									{
										scannedLinks.add(temp_list1.get(i));
									}
								}
							}
							else
							{
								
								if (!(temp_list1.get(i).contains("://"))
										&& !(temp_list1.get(i).startsWith("/"))
										&& !(scannedLinks.contains(Library.dumbURL(
												url, temp_list1.get(i))))
										&& !(temp_list1.get(i).contains("javascript:")))
										//&& !(tmp.get(i).contains("/")))
								{
									if (temp_list1.get(i).startsWith("?"))
									{
										
										String tmpURL = url;
										//System.out.println(tmpURL);
										
									
										if(tmpURL.endsWith("?"))
										{
										tmpURL = tmpURL.substring(0, tmpURL.indexOf("?"));
										}
										else
											if(tmpURL.endsWith("/"))
											{
												
											}
											else
											{
												tmpURL += "/";
											}
										
										temp_list2.add(tmpURL + temp_list1.get(i));
										scannedLinks.add(tmpURL + temp_list1.get(i));
										
									}
									else
									{
										
										temp_list2.add(Library.dumbURL(url, temp_list1.get(i)));
										scannedLinks.add(Library.dumbURL(url, temp_list1.get(i)));
									}
								}
							}
						}
						else
						{
							
							if (!(externalLinks.contains(temp_list1.get(i)))
									&& !(scannedLinks.contains(temp_list1.get(i))))
							{
							
								externalLinks.add(temp_list1.get(i));
							}
						}
						
						if (temp_list2.isEmpty())
						{
							
							break;
						}
					}
				}
			}
		}
		// System.out.println("tmp2" + tmp2);
		// System.out.println("External" + externalLinks);
		// System.out.println("Internal" + internalLinks);
		
		internalLinks = Library.merge(internalLinks, temp_list2);
	}
	
	public ArrayList<String> getInternalLinks()
	{
		return this.internalLinks;
	}
	
	public ArrayList<String> getExternalLinks()
	{
		return this.externalLinks;
	}
	
	public int getTest()
	{
		return tmpNum;
	}
	
	public String getUrl()
	{
		return url;
	}
	
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	/**
	 * To be overridden to allow the output to be "easily" added to a GUI
	 * @param string the string to be out put with system.out.println()
	 */
	public void print(String string)
	{
		System.out.println(string);
	}
	
	public void stop()
	{
		stop = true;
	}
	
	public void start()
	{
		stop = false;
	}
	
	/**public static void main(String[] args) throws MalformedURLException
	{
		Website test = new Website(new URL("http://yahoo.com"));
		test.scan();
	}**/
	
}
