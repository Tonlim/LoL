package iohandling;

import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;

import constant.Region;
import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;
import dto.Static.Champion;

public class Database {
	private static Database database;
	private final String filename = "database.dat";
	private ArrayList<Champion> champions;
	private RiotApi api;
	
	/*
	 * enforce singleton
	 */
	private Database(){
		
	}
	
	/*
	 * access point to class
	 */
	public static Database getDatabase(){
		if (database == null){
			database = new Database();
			database.api = new RiotApi(APIkey.getKey());
			database.api.setRegion(Region.EUW);
		}
		return database;
	}
	
	
	/*
	 * reads the file to ArrayList
	 */
	@SuppressWarnings("unchecked")
	public synchronized void open() {
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
			champions = (ArrayList<Champion>) in.readObject();
			in.close();
		} catch (FileNotFoundException e1){
			firstTimeSetUp();
			open();
		} catch (IOException | ClassNotFoundException e) {
			
		}
		

	}
	
	/*
	 * getter for ArrayList
	 */
	public ArrayList<Champion> getChampions() {
		if(champions == null){
			open();
		}
		return champions;
	}
	

	/*
	 * saves all changes to the database
	 */
	public synchronized void save(){
		try {
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
			out.writeObject(champions);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * makes a new database
	 */
	private void firstTimeSetUp(){
		try {
			Map<String, dto.Static.Champion> championsmap = api.getDataChampionList().getData();
			champions = new ArrayList<Champion>();
			champions.addAll(championsmap.values());
			save();
		} catch(RiotApiException e){
			e.printStackTrace();
		}
	}
	

}
