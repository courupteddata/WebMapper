
package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;


import net.NetHelper;
import gui.Window;

public class Application
{
	private static Window window;
	
	private final static String hostName = "127.0.0.1";
	private final static int port = 49156;
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			 public void run()
			 {
				try
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
				window = new Window();
				
				window.println("Message: This is an alpha version not meant for public release");
				window.println("Action: Attempting to find server...");
				
				if (NetHelper.touch(hostName, port) == false)
				{
					window.println("Response: Unable to find server");
				}
				else
				{
					window.println("Response: Server found");
				}
			}
		});
	}
	/*public static void main(String[] args)
	{
		while (true)
		{
			try
			{
				Socket server = new Socket(hostName, port);
				PrintWriter out = new PrintWriter(server.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
				String URL;
				
				out.println("link?");
				while ((URL = in.readLine()) != null)
				{
					break;
				}
				
				if (URL.equals("none") || URL.equals("DNT"))
				{
					break;
				}
				
				print("Connection success!");
				if (URL.equals("ALF"))
				{
					System.out.println("All links were found!! Thank you so much!!");
					out.println("msg");
					String msg;
					while ((msg = in.readLine()) != null)
					{
						break;
					}
					if (!msg.equals("none"))
					{
						System.out.println("msg");
					}
					return;
				}
				
				window.print("\n" + "Action:Scanning " + URL + "...");
				
				String links;
				ArrayList<String> data = (new Downloader()).getLinks(URL);
				if (data == null)
				{
					window.print("Error:Something went wrong"); 
				}
				else
				{
					if(data.get(0).equals("nolinks"))
					{
						links = "nolinks";
					}
					else
					{
						links = Downloader.parseLinks(data);
					}
					print("Success, sending links back to server...");
					out.println(links + "\n");
					print("Success! Closing all connections...");
					server.close();
					in.close();
					out.close();
					if (GUI.exit())
					{
						return;
					}
					if (GUI.pause())
					{
						while (!GUI.resume())
						{
							if(GUI.exit)
							{
								return;
							}
						}
					}
					Thread.sleep(250);
					print("Success!\nAwaiting connection to server...");
					GUI.println("Awaiting connection to server...");
				}
			}
			catch(SocketException E)
			{
				System.out.println("The server did not respond...");
				GUI.println("The server is not responding.");
				GUI.sendData("404");
				try
				{
					Thread.sleep(5000);
				}
				catch (InterruptedException e)
				{
					System.out.println("Fatal Error:Thread returned InterruptedException");
					e.printStackTrace();
					return;
				}
			}
			catch(IOException E)
			{
				System.out.println("Error:Caught IOException; ignoring");
			}
		}
		window.actionFinish();
		System.out.println("All links were found!! Thank you so much!!");
	}*/
}
