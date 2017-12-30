package com.wynprice.discord.first;

import java.io.File;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

public class Main 
{	
	
	//https://discordapp.com/oauth2/authorize?&client_id=<CLIENT ID>&scope=bot&permissions=0
	
	public static final IDiscordClient CLIENT = createClient(StartUp.args[0], true);
	
	public static final File BASE_LOCATION = new File(StartUp.args[1]);
	
	public static final Gui GUI = new Gui();
	
	public static IDiscordClient createClient(String token, boolean login) 
	{ 
        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(token);
        try {
            if (login) {
                return clientBuilder.login();
            } else {	
                return clientBuilder.build();
            }
        } catch (DiscordException e) {
            e.printStackTrace();
            return null;
        }
    }	
}
