/**
 * @author Nathan Jones
 * @version 1
 */
//Copyright Nathan Jones, All rights reserved

package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;

public class Downloader
{
	private static URL url = null;
	private static String source = "";
	private static HttpURLConnection con;
	private static HttpsURLConnection secureCon;
	private static int temp_index = 0;
	
	/**
	 * Reads in the source of the InputStream and turns it into a String with a line break after each valid line of the source
	 * Note: this will stop once the String is 100,000 bytes or has reached the end, what ever comes first.
	 * @param is An inputStream for a valid web site or other valid stream of data
	 * @return A String that has attempted to keep the original formatting of the original source
	 */
	private static String getWebSource(InputStream is)
	{
		InputStreamReader reader = new InputStreamReader(is);
		BufferedReader in = new BufferedReader(reader);
		StringBuilder responseContent = new StringBuilder();
		String inputLine;
		
		try
		{
			while ((inputLine = in.readLine()) != null)
			{
				if (responseContent.toString().getBytes().length < 100000)
				{
					responseContent.append(inputLine + "\n");
				}
				else
				{
					break;
				}
			}
		}
		catch (IOException e)
		{
			Library.reportError("getWebSourceError", url + " : " + e.toString());
		}
		
		return responseContent.toString();
	}

	/**
	 * Gets the source of a web site and attempts to handle redirects and forwards correctly without getting stuck in a loop
	 * @param URL A valid URL that starts with https:// or http:// to get the source from
	 * @return A String representation of the source of the URL that was entered in for URL
	 */
	
	public static String download(String URL)
	{
		try
		{
			try
			{
				url = new URL(URL);
				
				if (URL.toLowerCase().startsWith("http://"))
				{
					con = (HttpURLConnection) url.openConnection();
					con.setDoInput(true);
					con.setInstanceFollowRedirects(false);
					
					con.setRequestProperty("User-Agent", Library.userAgent);
					// "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
					InputStream is = null;
					temp_index = 1;
					switch ((int) con.getResponseCode() / 100)
					{
						case (1):
							//todo
							break;
						case (2):
							is = con.getInputStream();
							source = Downloader.getWebSource(is);
							break;
						case (3):

							if ((Library.stripUrl(con.getHeaderField("Location"))).equals(Library.stripUrl(URL)))
							{
								if (temp_index == 3)
								{
									break;
								}
								source = Downloader.download(con
										.getHeaderField("Location"));
								
								temp_index++;

								break;

							}
							else
							{
								source = "<a href=\""
										+ con.getHeaderField("Location")
										+ "\"></a>";
								break;
							}
						case (4):
							is = con.getErrorStream();
							source = "Client Error";
							Library.reportError("Client Error",
									url + Downloader.getWebSource(is));
							break;
						case (5):
							source = "Server Error";
							break;
						default:
							source = "Unknown Error";
							break;
					}
					
					con.disconnect();
				}
				else
				{
					if (URL.toLowerCase().startsWith("https://"))
					{
						try
						{
							secureCon = (HttpsURLConnection) url
									.openConnection();
						}
						catch (UnknownHostException e)
						{
							source = "null - NotReal";
						}
						
						secureCon.setDoInput(true);
						secureCon.setInstanceFollowRedirects(false);
				
						secureCon.setRequestProperty("User-Agent",
								Library.userAgent);
						// "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
						InputStream is = null;
						switch ((int) secureCon.getResponseCode() / 100)
						{
							case (1):
								//todo
								break;
							case (2):
								is = secureCon.getInputStream();
								source = Downloader.getWebSource(is);
								break;
							case (3):
								
								if ((Library.stripUrl(secureCon.getHeaderField("Location"))).equals(Library.stripUrl(URL)))
								{
									if (temp_index == 4)
									{
										break;
									}
									
									source = Downloader.download(secureCon
											.getHeaderField("Location"));
									temp_index++;
									break;
								}
								else
								{
									source = "<a href=\""
											+ secureCon
													.getHeaderField("Location")
											+ "\"></a>";
									break;
								}
							case (4):
								is = secureCon.getErrorStream();
								source = "Client Error";
								Library.reportError("Client Error",
										Downloader.getWebSource(is));
								break;
							case (5):
								source = "Server Error";
								break;
							default:
								source = "Unknown Error";
								break;
						}
						secureCon.disconnect();
					}
					else
					{
						download("http://" + URL);
					}
				}
			}
			catch (UnknownHostException e)
			{
				source = "null - NotReal";

			}
		}
		catch (IOException e)
		{
			Library.reportError("IOException-Downloader",
					url + " : " + e.toString());
			source = "ERROR: IOException.Download.download";

		}
		
		return source;
	}
}
