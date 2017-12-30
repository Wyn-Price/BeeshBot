package com.wynprice.discord.first;

public class StartUp 
{
	
	public static String[] args;
	
	public static void main(String[] args) 
	{
		StartUp.args = args;
		if(args.length < 2)
			System.exit(1);
        Main.CLIENT.getDispatcher().registerListener(new EventSystem());
        Main.GUI.setup();
    }
}
