/**
 * @author Nathan
 * @version 1
 */

package utils;

import java.util.ArrayList;

public class RobotsDotTxt
{
	private String robotsTXT;
	private String[] blocked;
	private ArrayList<String> parsedBlocked = new ArrayList<String>();
	private ArrayList<String> parsedAllowedLinks = new ArrayList<String>();
	
	/**
	 * Creates and parses the robots.txt that is passed as a valid String	
	 * @param robotsTXT The robots.txt from a website
	 */
	RobotsDotTxt(String robotsTXT)
	{
		this.robotsTXT = robotsTXT;
		parse();
	}
	
	/**
	 * Gets the parsed Blocked Links
	 * @return The ArrayList<String> of blocked Links, in raw format 
	 */
	public ArrayList<String> getParsedBlocked()
	{
		return parsedBlocked;
	}
	
	/**
	 * Turns the ArrayList<String> of blocked links into a String array
	 * @return String Array of blocked links
	 */
	public String[] getNormalArrayParsedBlocked()
	{
		return parsedBlocked.toArray(new String[parsedBlocked.size()]);
	}
	
	/**
	 * If the robots.txt contains allowed links, this returns an ArrayList<String> of those links
	 * @return The ArrayList<String> of allowed links, in raw format
	 */
	public ArrayList<String> getParsedAllowedLinks()
	{
		return parsedAllowedLinks;
	}
	
	/**
	 * Extracts all the disallow and allow that apply to this specific user agent or all user agents
	 */
	private void parse()
	{
		blocked = robotsTXT.split("\n");
		String temp_string;
		int index;
		
		for (int i = 0; i < blocked.length; i++)
		{
			index = i;
			temp_string = blocked[i];
			
			if (temp_string.toLowerCase().startsWith("user-agent"))
			{
				temp_string = temp_string.substring(temp_string.indexOf(":") + 1);
				
				if (temp_string.equalsIgnoreCase(Library.userAgent)
						|| temp_string.contains("*"))
				{
					index++;
					
					while (!(blocked[index].toLowerCase().startsWith("user-agent"))
							&& index < blocked.length - 1)
					{
						if (blocked[index].toLowerCase().startsWith("disallow"))
						{
							temp_string = blocked[index]
									.substring(blocked[index].indexOf(":") + 1);
							temp_string = temp_string.trim();
							parsedBlocked.add(temp_string);
						}
						
						if (blocked[index].toLowerCase().startsWith("allow"))
						{
							temp_string = blocked[index]
									.substring(blocked[index].indexOf(":") + 1);
							temp_string = temp_string.trim();
							parsedAllowedLinks.add(temp_string);
						}
						
						index++;
					}
				}
			}
			
			i = index;
		}
		for(int z = 0; z < parsedBlocked.size(); z++)
		{
			if( parsedBlocked.get(z).contains("*"))
			{
				char[] tmp = parsedBlocked.get(z).toCharArray();
				String escaped = "";
				for(int p = 0; p < tmp.length; p ++)
				{
					if(tmp[p] == '*')
					{
						escaped += ".*.";
					}
					else
						if(tmp[p]== '?' || tmp[p] =='\\' || tmp[p] == '/')
						{
							escaped += ("\\"+ tmp[p]);
						}
					else
					{
						escaped += tmp[p];
					}
				}
				
				parsedBlocked.set(z,escaped);
			}
		}
	}

	/**
	 * Checks against the robots.txt to make sure it is not blocked
	 * @param extension the extension that will be added to the domain or a full domain to compare
	 * @return true if it is blocked and false if it is not blocked
	 */
	public boolean isBlocked(String extension)
	{
		String temp_string1 = "";
	
		
		if ((extension.startsWith("/")) && !(extension.contains("://")))
		{
			temp_string1 = extension;
		}
		else if (extension.contains("://"))
		{
			temp_string1 = Library.uRLExtension(extension);
		}
		
		if (parsedBlocked.contains(extension))
		{
			return true;
		}
		
		for (int i = 0; i < parsedBlocked.size();i++)
		{
			if (parsedBlocked.get(i).startsWith(extension) && parsedBlocked.get(i).endsWith("$"))
			{
				
				return true;
			}
			else if (parsedBlocked.get(i).contains("*"))
			{
				
				//System.out.println(temp_string1 + " " + temp_string2);
				
	
				temp_string1 = extension.replaceFirst(parsedBlocked.get(i), "[]???{}");

				
				

				
				if (temp_string1.startsWith("[]???{}"))
				{
					return true;
				}
			}
			else if (!(parsedBlocked.get(i).endsWith("/")))
			{
				if (extension.startsWith(parsedBlocked.get(i)))
				{
					return true;
				}
			}
			else if (parsedBlocked.get(i).endsWith("/"))
			{
				if (extension.startsWith(parsedBlocked.get(i)))
				{
					return true;
				}
			}
		}
		
		return false;
	}
}
