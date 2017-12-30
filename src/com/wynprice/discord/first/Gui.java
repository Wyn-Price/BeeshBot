package com.wynprice.discord.first;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class Gui
{	
	public void setup()
	{
		JFrame frame = new JFrame("OofBot");
		frame.setVisible(true);
		frame.pack();
		frame.setSize(new Dimension(300, 300));
		
		frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) 
			{
				System.exit(-1);
			}
			
			@Override
			public void windowClosed(WindowEvent e) 
			{
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				
			}
		});
	}
	
	
}
