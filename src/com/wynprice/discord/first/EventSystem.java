package com.wynprice.discord.first;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage.Attachment;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class EventSystem 
{
	
	private static final List<String> ACCEPTED_FILETYPES = Arrays.asList("png", "jpg", "gif", "jpeg");
	    
	private HashMap<Long, ArrayList<Long>> can_upload;
	
	private HashMap<Long, ArrayList<Long>> uploading = new HashMap<>();
	
	@EventSubscriber
	public void onReady(ReadyEvent event)
	{
        Main.CLIENT.changePlayingText("Google");

	}
	
	@SuppressWarnings("unchecked")
	@EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) 
    {
		if(can_upload == null)
			can_upload = (HashMap<Long, ArrayList<Long>>) Utils.loadObject(new File(Main.BASE_LOCATION, "permissions.wyn"));
    	String[] args = event.getMessage().getContent().split(" ");
    	if(uploading.get(event.getChannel().getLongID()) != null && uploading.get(event.getChannel().getLongID()).contains(event.getAuthor().getLongID())
    		&& !event.getMessage().getAttachments().isEmpty() && event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(Permissions.MANAGE_SERVER))
    		downloadAttachment(event.getMessage().getAttachments(), event.getGuild(), event.getChannel());
    	if(args.length > 0 && args[0].equals("!beesh"))
    	{    			
    		if(args.length > 1 && args[1].equals("amount"))
    		{
    			int local_total = 0;
    			int local_size = 0;
    			for(File file : new File(Main.BASE_LOCATION, event.getGuild().getStringID()).listFiles())
    				if(file.isFile() && ACCEPTED_FILETYPES.contains(FilenameUtils.getExtension(file.getAbsolutePath()).toLowerCase()))
    				{
    					local_total++;
    					local_size += file.length();
    				}
    			int total = 0;
    			int total_size = 0;
    			for(File dir : Main.BASE_LOCATION.listFiles())
    				if(dir.isDirectory())
	    				for(File file : dir.listFiles())
	        				if(file.isFile() && ACCEPTED_FILETYPES.contains(FilenameUtils.getExtension(file.getAbsolutePath()).toLowerCase()))
	        				{
	        					total++;
	        					total_size += file.length();
	        				}
    			event.getChannel().sendMessage("There are a total of " + String.valueOf(local_total) + " files connected to this guild (" + Utils.bytesToFileString(local_size) + ")\nThere are " + String.valueOf(total) + " files connected to this bot, in " + Main.CLIENT.getGuilds().size() + " guild" + (Main.CLIENT.getGuilds().size() != 1 ? "s" : "") + " (" + Utils.bytesToFileString(total_size) + ")");
    		}
    		else if(args.length > 1 && args[1].equals("upload"))
    		{
    			if(args.length > 2 && args[2].equals("start"))
					if(Utils.getList(uploading, event.getChannel().getLongID()).contains(event.getAuthor().getLongID()))
						event.getChannel().sendMessage("Already uploading");
					else
					{
						Utils.getList(uploading, event.getChannel().getLongID()).add(event.getAuthor().getLongID());
						event.getChannel().sendMessage("Upload mode turned on");
					}
    			else if(args.length > 2 && args[2].equals("stop"))
					if(Utils.getList(uploading, event.getChannel().getLongID()).contains(event.getAuthor().getLongID()))
					{
						Utils.getList(uploading, event.getChannel().getLongID()).remove(event.getAuthor().getLongID());
						event.getChannel().sendMessage("Upload mode turned off");
					}
					else
						event.getChannel().sendMessage("Upload mode isnt on");
				else if(hasPermission(event.getAuthor(), event.getGuild(), event.getChannel()))
    				if(event.getMessage().getAttachments().isEmpty())
        				event.getChannel().sendMessage("There was nothing attached with the message.");
    				else
    					downloadAttachment(event.getMessage().getAttachments(), event.getGuild(), event.getChannel());
    			else
    				event.getChannel().sendMessage("You don't have the required permissions");
    		}
    		else if(args.length > 2 && args[1].equals("perms") && hasPermission(event.getAuthor(), event.getGuild(), event.getChannel()))
    		{
    			if(args[2].equals("add"))
	    			for(IUser user : event.getMessage().getMentions())
	    			{
	    				if(hasPermission(user, event.getGuild(), event.getChannel()))
	    				{
	        				event.getChannel().sendMessage("User: " + user.getDisplayName(event.getGuild()) + " already has permission to upload");
	    					continue;
	    				}
		    			Utils.getList(can_upload, event.getGuild().getLongID()).add(user.getLongID());
	    				event.getChannel().sendMessage("User: " + user.getDisplayName(event.getGuild()) + " was given permission to upload files. Be carefull!");
	    			}
    			else if(args[2].equals("remove"))
    				for(IUser user : event.getMessage().getMentions())
	    			{
	    				if(!hasPermission(user, event.getGuild(), event.getChannel()))
	    				{
	        				event.getChannel().sendMessage("User: " + user.getDisplayName(event.getGuild()) + " already dosn't have the permission");
	    					continue;
	    				}
		    			Utils.getList(can_upload, event.getGuild().getLongID()).remove(user.getLongID());
	    				event.getChannel().sendMessage("User: " + user.getDisplayName(event.getGuild()) + " had their permissions stripped from then");
	    			}
    		}
    		else
    		{
    			ArrayList<File> acceptedFiles = new ArrayList<>();
    			
    			for(File file : new File(Main.BASE_LOCATION, event.getGuild().getStringID()).listFiles())
    				if(file.isFile() && ACCEPTED_FILETYPES.contains(FilenameUtils.getExtension(file.getAbsolutePath()).toLowerCase()))
    					acceptedFiles.add(file); 
    			try 
    			{
					event.getChannel().sendFile(acceptedFiles.get(new Random().nextInt(acceptedFiles.size())));
				} 
    			catch (FileNotFoundException e) 
    			{
					e.printStackTrace();
				}
    		}
    		Utils.saveObject(can_upload, new File(Main.BASE_LOCATION, "permissions.wyn"));
    	}
    }
	
	private boolean hasPermission(IUser user, IGuild guild, IChannel channel)
	{
		if(can_upload.get(guild.getLongID()) == null)
			can_upload.put(guild.getLongID(), new ArrayList<>());
		return user.getPermissionsForGuild(guild).contains(Permissions.MANAGE_SERVER) || can_upload.get(guild.getLongID()).contains(user.getLongID());
	}
	
	private void downloadAttachment(List<Attachment> list, IGuild guild, IChannel channel)
	{
		for(Attachment attach : list)
		{
			if(!ACCEPTED_FILETYPES.contains(attach.getFilename().split("\\.")[attach.getFilename().split("\\.").length - 1].toLowerCase()))
			{
				String filetypeout = "";
				for(int i = 0; i < ACCEPTED_FILETYPES.size(); i++)
					if(i == ACCEPTED_FILETYPES.size() - 2)
						filetypeout += ACCEPTED_FILETYPES.get(i) + " or ";
					else
						filetypeout += ACCEPTED_FILETYPES.get(i) + ", ";
				channel.sendMessage("Error, file type not accepted. Please use: " + filetypeout.substring(0, filetypeout.length() - 2));
				continue;
			}
		    byte[] fromBytes = Utils.copyFileFromDiscord(attach.getUrl());
		    if(fromBytes != null)
				try {
					FileUtils.writeByteArrayToFile(new File(Main.BASE_LOCATION + "/" + guild.getStringID(), attach.getFilename()), fromBytes);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					channel.sendMessage("Thank you for your contribution to " + guild.getName());
				}
		}
	}
}
