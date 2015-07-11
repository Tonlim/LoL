package iohandling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import data.Constants;

/*
 * returns the APIkey to connect to RIOT servers
 * uses a local key if found, otherwise uses a default key
 */
public class APIkey {
	public static String getKey(){
		try {
			BufferedReader in = new BufferedReader(new FileReader(Constants.APIfile));
			String res = in.readLine();
			in.close();
			return res;
		} catch(IOException e){
			return Constants.defaultAPIkey;
		}
	}
}
