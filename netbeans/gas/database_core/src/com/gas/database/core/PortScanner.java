package com.gas.database.core;

import java.net.*;

public class PortScanner{

	public static void scan(int...ports ){
		for(int port : ports){
			portInUse(port);
		}
	}
	
	public static void scan(Integer startPortRange, Integer stopPortRange) {

		for (int i = startPortRange; i <= stopPortRange; i++) {
			portInUse(i);
		}
	}
	
	private static boolean portInUse(int port){
		boolean inUse = false;
		try {
			Socket ServerSok = new Socket("127.0.0.1", port);

			System.out.println("Port in use: " + port);

			inUse = true;
			
			ServerSok.close();
		} catch (Exception e) {
			System.out.println("Port not in use: " + port);
			inUse = false;
		}
		
		return inUse;
		
	}
}
