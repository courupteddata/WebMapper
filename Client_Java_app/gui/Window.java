package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

import net.NetHelper;
import utils.Library;
import utils.Website;
import utils.WebsiteThread;

public class Window
{
	private JFrame frame;
	private JMenuBar menuBar;
	private JTextArea scanConsole;
	private DefaultCaret caret;
	private JProgressBar progressBar;
	public static PrintStream output;
	
	private JScrollPane scrollPane;
	private JPanel scanPanel;
	private JPanel buttonPanel;
	private JButton[] buttons;
	private WebsiteThread websiteThread;
	
	private byte state;
	// 0: never started; idle
	// 1: running
	// 2: paused; idle
	
	// start:
	//     0 -> 1
	// resume:
	//     2 -> 1
	// pause:
	//     1 -> 2
	// stop:
	//     1 -> 0
	//     2 -> 0
	
	public Window()
	{
		frame = new JFrame();
		menuBar = new JMenuBar();
		scanConsole = new JTextArea();
		caret = (DefaultCaret) scanConsole.getCaret();
		progressBar = new JProgressBar();
		output = new PrintStream(new TextAreaOutputStream(scanConsole));
		
		scrollPane = new JScrollPane();
		scanPanel = new JPanel();
		buttonPanel = new JPanel();
		buttons = new JButton[] 
			{
				new JButton("Start"),
				//new JButton("Resume"),
				//new JButton("Pause"),
				new JButton("Stop")
			};
		
		initialize();
		
		frame.setTitle("Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	

	
	private void initialize()
	{
		GridBagLayout gbl_scanPanel = GridBagHelp.makeLayout(new int[] {0, 0}, new int[] {0, 0, 0, 0},
				new double[] {1.0, Double.MIN_VALUE}, new double[] {1.0, 0.0, 0.0, Double.MIN_VALUE});
		
		GridBagConstraints gbc_scrollPanel = GridBagHelp.makeConstraints(0, 0,
				GridBagConstraints.BOTH, new Insets(0, 0, 5, 0));
		GridBagConstraints gbc_progressBar = GridBagHelp.makeConstraints(0, 1,
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 2, 0));
		GridBagConstraints gbc_buttonPanel = GridBagHelp.makeConstraints(0, 2,
				GridBagConstraints.BOTH, new Insets(5, 0, 0, 0));
		
		makeScanPanel(gbl_scanPanel, gbc_scrollPanel, gbc_progressBar, gbc_buttonPanel);
		makeScrollPane();
		makeButtonPanel();
		
		frame.add(scanPanel);
		frame.pack();
		
		scanPanel.setSize(scanPanel.getWidth() * 2, scanPanel.getHeight());
		
		//System.out.println(scanPanel.getWidth());
		//System.out.println(scanPanel.getHeight());
		
		//frame.pack();
		
		//System.out.println(scanPanel.getWidth());
		//System.out.println(scanPanel.getHeight());
		
		int width = frame.getInsets().left + frame.getInsets().right + scanPanel.getWidth();
		int height = frame.getInsets().top + frame.getInsets().bottom + scanPanel.getHeight();
		frame.setMinimumSize(new Dimension(width, height));
	}
	
	private void makeScanPanel(LayoutManager lay_scanPanel,
			GridBagConstraints gbc_scrollPane, GridBagConstraints gbc_progressBar,
			GridBagConstraints gbc_buttonPanel)
	{
		Border border = new TitledBorder(null, "Scan", TitledBorder.LEADING,
				TitledBorder.TOP, null, null);
		Border padding = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		CompoundBorder compoundBorder2 = new CompoundBorder(new CompoundBorder(padding, border), padding);
		
		scanPanel.setBorder(compoundBorder2);
		scanPanel.setLayout(lay_scanPanel);
		
		scanPanel.add(scrollPane, gbc_scrollPane);
		scanPanel.add(progressBar, gbc_progressBar);
		scanPanel.add(buttonPanel, gbc_buttonPanel);
	}
	
	private void makeMenuBar()
	{
		JMenu menus[] =
			{
				new JMenu("File"), 				// 0
				new JMenu("Edit"), 				// 1
				new JMenu("Debug"), 			// 2
				new JMenu("Help") 				// 3
			};
		
		JMenuItem fileOptions[] =
			{
				new JMenuItem("New"), 			// 0
				new JMenuItem("Open"), 			// 1
				new JMenuItem("Save"), 			// 2
				new JMenuItem("Save As"), 		// 3
				new JMenuItem("Preferences"), 	// 4
				new JMenuItem("Exit") 			// 5
			};
		
		JMenuItem editOptions[] = {};
		
		JMenuItem debugOptions[] =
			{
				new JMenuItem("Performance") 	// 0
			};
		
		JMenuItem helpOptions[] =
			{
				new JMenuItem("About") 			// 0
			};
		
		// fill in the menu bar
		for (int i = 0; i < menus.length; i++)
		{
			menuBar.add(menus[i]); // add the menu to the menu bar
		}
		
		// fill in file menu
		for (int i = 0; i < fileOptions.length; i++)
		{
			switch (i)
			{
				case 2: // separator before 3rd option
					menus[0].add(new JSeparator());
					break;
				case 4: // separator before 5th option
					menus[0].add(new JSeparator());
					break;
				case 5: // separator before 6th option
					menus[0].add(new JSeparator());
					break;
			}
			menus[0].add(fileOptions[i]); // add the option to the menu
		}
		
		// fill in the edit menu
		for (int i = 0; i < editOptions.length; i++)
		{
			switch (i) { }
			menus[1].add(editOptions[i]); // add the option to the menu
		}
		
		// fill in the debug menu
		for (int i = 0; i < debugOptions.length; i++)
		{
			switch (i) { }
			menus[2].add(debugOptions[i]); // add the option to the menu
		}
		
		// fill in the help menu
		for (int i = 0; i < helpOptions.length; i++)
		{
			switch (i) { }
			menus[3].add(helpOptions[i]); // add the option to the menu
		}
	}
	
	private void makeScrollPane()
	{
		scanConsole.setRows(4);
		scanConsole.setColumns(4);
		scanConsole.setEditable(false);
		
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(scanConsole);
		
		//System.setOut(output);
		System.setErr(output);
	}
	
	private void makeButtonPanel()
	{
		state = 0;
		buttonPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		for (int i = 0; i < buttons.length; i++)
		{
			buttonPanel.add(buttons[i]); // add the button to the panel
			//frame.pack();
			//buttons[i].setSize(buttons[i].getWidth() * 2, buttons[i].getHeight());
		}
		
		setButtons();
		handleActions();
	}
	
	private void setButtons()
	{
		switch (state)
		{
			case 0: // never started; idle
				/*buttons[0].setEnabled(true); // start
		        buttons[1].setEnabled(false); // resume
		        buttons[2].setEnabled(false); // pause
		        buttons[3].setEnabled(false); // stop*/
				buttons[0].setEnabled(true); // start
		        buttons[1].setEnabled(false); // stop
				break;
			case 1: // running
				/*buttons[0].setEnabled(false); // start
		        buttons[1].setEnabled(false); // resume
		        buttons[2].setEnabled(true); // pause
		        buttons[3].setEnabled(true); // stop*/
				buttons[0].setEnabled(false); // start
		        buttons[1].setEnabled(true); // stop
				break;
			case 2: // paused; idle
				/*buttons[0].setEnabled(false); // start
		        buttons[1].setEnabled(true); // resume
		        buttons[2].setEnabled(false); // pause
		        buttons[3].setEnabled(true); // stop*/
				break;
		}
	}
	
	private void handleActions()
	{
		buttons[0].addActionListener(new ActionListener() // Start
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	try {
					actionStart();
				} catch (IOException e1) {
				    e1.printStackTrace();
				}
		    }
		});
		
		/*buttons[1].addActionListener(new ActionListener() // Pause
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	actionResume();
		    }
		});*/
		
		/*buttons[2].addActionListener(new ActionListener() // Resume
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	actionPause();
		    }
		});*/
		
		buttons[1].addActionListener(new ActionListener() // Stop
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	actionStop();
		    }
		});
	}
	
	public void actionStart() throws IOException
	{
		try
		{
			websiteThread = new WebsiteThread(new URL(NetHelper.getWork()))
			
			{
				public void print(String string)
				{
					println(string);
				}
			};
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		
		// start scan
		state = 1;
		setButtons();
		
		new SwingWorker<String, Object>()
		{
			@Override
			protected String doInBackground() throws Exception
			{

			    long time = 1 * 10 * 1000;
			    Timer timer = new Timer();
			    timer.schedule( new TimerTask(){
			          public void run() {
			                   
			                   websiteThread.stop();
			                 
			                
			           }
			     },  time );
				websiteThread.start();
				
				return "completed";
			}
			
			protected final void done()
			{
				//this is where we add send work or something
				actionStop();
				ArrayList<String> tmp = new ArrayList<String>();
				ArrayList<String> tmp2 = new ArrayList<String>();
				println("starting parse");
				for(int i = 0; i < websiteThread.getExternalLinks().size(); i++)
				{
					tmp.add(utils.Library.stripUrl(websiteThread.getExternalLinks().get(i)));
				}
				/**for(int t = 0; t <tmp.size();t++)
				{
					String tmpHold = tmp.get(t);
					tmp.remove(t);
					if(!(tmp.contains(tmpHold)))
					{
						tmp.add(t, tmpHold);
					}
				}**/
				tmp2 = Library.merge(tmp2, tmp);
				println("sending:");
				int limit = 10;
				for (int i = 0; i < tmp2.size(); i += limit) {
				
	
				    try {
						net.NetHelper.sendResults(Library.stripUrl(websiteThread.getUrl()),new ArrayList<String>(tmp2.subList(i,
						        i + Math.min(limit, tmp2.size() - i))), net.Library.key);
						net.NetHelper.suggestion(new ArrayList<String>(tmp2.subList(i,
						        i + Math.min(limit/2, tmp2.size() - i))));
					} catch (IOException e) {
					
						e.printStackTrace();
					}
				    try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				try {
					actionStart();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}.execute();
	}
	
	public void actionResume()
	{
		// resume scan... this is going to be hard to implement
		state = 1;
		setButtons();
		//websiteThread.resume();
	}
	
	//extremely buggy
	public void actionPause()
	{
		// pause scan... this is going to be hard to implement
		state = 2;
		setButtons();
		//websiteThread.pauseScan();
	}
	
	public void actionStop()
	{
		// stop scan
		state = 0;
		setButtons();
		websiteThread.stop();
		println("STOPPING...");
	}
	
	// if no more links are left, execute finish
	public void actionFinish()
	{
		
	}
	
	/*@Override
	public void run()
	{
		
	}*/
	
	public void println(String text)
	{
		
		System.out.println(text);
		
		try
		{
			scanConsole.setCaretPosition(scanConsole.getSelectionStart());
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
	}
	
	public JFrame getFrame()
	{
		return frame;
	}
	
	public byte getState()
	{
		return state;
	}
}
