/**
 * @author Nathan
 * @version 1
 */

package utils;

import java.net.URL;

public class WebsiteThread extends Website
{
	volatile Thread thread = new Thread(null, null, "downloader")
	{
		public void run()
		{
			scan();
		}
	};
	
	public WebsiteThread(URL url)
	{
		super(url);
		Thread.yield();
	}
	
	/*public void stop()
	{
		synchronized (this)
		{
			thread.interrupt();
		}
	}*/
	
	public void start()
	{
		synchronized (this)
		{
			thread.run();
		}
	}
	
	public void pauseScan()
	{
		try
		{
			synchronized (this)
			{
				thread.wait();
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public void resume()
	{
		synchronized (this)
		{
			thread.notify();
		}
	}
	
	public void scan()
	{
		super.scan();
		//super.scan(super.getUrl());
	}
}
