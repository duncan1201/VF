package com.gas.qblast.core.remote;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WebCommand {
	public void sendCommand() throws Exception
	{
		String host = "http://www.ncbi.nlm.nih.gov/blast/Blast.cgi?" ;
		
		String urlParams = "cmd=web&PAGE=proteins&database=swissprot" ;
		
		URL url = new URL(host+urlParams);
		URLConnection connection = url.openConnection();
		
		InputStream inputStream = connection.getInputStream();
			
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
		String output = "";
		String line = reader.readLine();
		
		while(line != null)
		{
			output += line;
			line = reader.readLine();
		}
		System.out.println(output);
	}
	
	public static void main(String args[]) throws Exception{
		WebCommand command = new WebCommand();
		command.sendCommand();
	}
}
