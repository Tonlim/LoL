package application;
	
import iohandling.APIkey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;
import constant.PlatformId;
import constant.Region;
import data.CustomLeague;
import data.CustomPlayer;
import dto.CurrentGame.CurrentGameInfo;
import dto.CurrentGame.Participant;
import dto.League.League;
import dto.Summoner.Summoner;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			RiotApi api = new RiotApi(APIkey.getKey());
			api.setRegion(Region.EUW);
			Map<String, Summoner> summoners = api.getSummonersByName("tonlim, elite cart, wolftooth1, zerpee");
			Summoner summoner = summoners.get("tonlim");
			long id = summoner.getId();
			long level = summoner.getSummonerLevel();
			
			/*
			ChampionList freetoplay = api.getFreeToPlayChampions();
			for (Champion i: freetoplay.getChampions()){
				dto.Static.Champion data = api.getDataChampion((int) i.getId());
				System.out.println(data.getName());
			}
			

			
			System.out.println("----------------------");
			
			PlayerHistory history = api.getMatchHistory(id);
			List<MatchSummary> matches = history.getMatches();
			System.out.println(matches.get(0).getQueueType());
			
			Text idText = new Text(""+id);
			Text levelText = new Text(""+level);
			*/
			try {
				CurrentGameInfo currentGame = api.getCurrentGameInfo(PlatformId.EUW, id);
				List<Participant> participants = currentGame.getParticipants();

				Map<String, Long> teamids = new HashMap<String, Long>();
				Map<String, Long> champids = new HashMap<String, Long>();
				
				long[] idds = new long[10];
				for(int i=0;i<10;i++){
					idds[i] = participants.get(i).getSummonerId();
					teamids.put(""+participants.get(i).getSummonerId(), participants.get(i).getTeamId());
					champids.put(""+participants.get(i).getSummonerId(), participants.get(i).getChampionId());
				}
				
				
				//TODO: make local database for all champs

				
				Map<String, Summoner> people = api.getSummonersById(idds);
				ArrayList<String> sumids = new ArrayList<String>(people.keySet());

				Map<String, List<League>> mapleague = api.getLeagueEntryBySummoners(idds);
				
				
				for(String i : sumids){
					System.out.println(teamids.get(i)+ " " + people.get(i).getName()+ "   ");
					System.out.println(champids.get(i));
					if(mapleague.get(i) != null){
						for(League j : mapleague.get(i)){
							System.out.print(j.getTier() + " in " + j.getQueue() + "   ");
							System.out.println(j.getEntries().get(0).getDivision());
						}
					}
					System.out.println("");
				}
				
				/*
				System.out.println(arraylistleague.size());
				
				for(List<League> i : arraylistleague){
					for(League j : i){
						System.out.print(j.getTier() + " in " + j.getQueue() + "   ");
					}
					System.out.println("");
				}
				*/
				//RankedStats stats = api.getRankedStats(id);
				//api.getPlayerStatsSummary(id);
				
			} catch(RiotApiException e){
				System.out.println("No match found");
				//e.printStackTrace();
			}
			
			
			//api.getSummonersById();
			
			//300px is about 26 chars
			
			//TESTING CODE for CustomPlayer
			CustomPlayer testy = new CustomPlayer(summoner.getId(),0,summoner.getName(),0);
			testy.addLeague(new CustomLeague("GOLD","V","SOLO_RANKED_5X5"));
			
			
			
			BorderPane root = new BorderPane();
			root.setCenter(testy.getGui());

			
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
