package com.wynprice.discord.first;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;

public class Utils 
{
	public static byte[] copyFileFromDiscord(String url)
	{
        URLConnection openConnection;
		try 
		{
			openConnection = new URL(url).openConnection();
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
			return null;
		}

        try 
        {

            openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            openConnection.connect();
            InputStream in = new BufferedInputStream(openConnection.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf)))
                out.write(buf, 0, n);
            out.close();
            in.close();
            return out.toByteArray();
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
    }
	
	public static void saveObject(Serializable object, File file)
	{
		try {
			FileUtils.writeByteArrayToFile(file, SerializationUtils.serialize(object));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String bytesToFileString(int total_bytes)
	{
		String[] suffixs = {"Bytes", "MB", "GB","TB"};
		for(int i = 0; i <= 4; i++)
		{
			if(String.valueOf(total_bytes / Math.pow(1024, i)).startsWith("0."))
			{
				return i == 0 ? String.valueOf(Math.round(total_bytes * 100f) / 100f) + suffixs[0] : String.valueOf(Math.round((total_bytes / Math.pow(1024, i - 1)) * 100f) / 100f) + suffixs[i - 2];
			}
		}
		return String.valueOf(Math.round((total_bytes / Math.pow(1024, 3)) * 100f) / 100f) + suffixs[3];
	}
	
	public static ArrayList<Long> getList(HashMap<Long, ArrayList<Long>> list, long carrierID)
	{
		ArrayList<Long> current_list = list.get(carrierID);
		if(current_list == null)
		{
			current_list = new ArrayList<>();
			list.put(carrierID, current_list);
		}
		return current_list;
	}
	
	public static Object loadObject(File file)
	{
		try {
			return SerializationUtils.deserialize(Files.readAllBytes(Paths.get(file.toURI())));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
