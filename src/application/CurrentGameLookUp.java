package application;

import iohandling.APIkey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
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

public class CurrentGameLookUp {
	public static Pane getGui(String name){
		GridPane res = new GridPane();
		RiotApi api = new RiotApi(APIkey.getKey());
		api.setRegion(Region.EUW);
		try {
			//lookup the summonerid of the wanted player
			Map<String, Summoner> summonerRequested = api.getSummonersByName(name);
			long idRequested = summonerRequested.get(name).getId();
			
			//get his currentgame info
			CurrentGameInfo currentGame = api.getCurrentGameInfo(PlatformId.EUW, idRequested);
			List<Participant> participants = currentGame.getParticipants();
			
			//extract summonerids, teamids, championids from the current game
			long[] ids = new long[participants.size()];
			long[] teamids = new long[participants.size()];
			long[] champids = new long[participants.size()];
			for (int i=0;i<participants.size();i++){
				ids[i] = participants.get(i).getSummonerId();
				teamids[i] = participants.get(i).getTeamId();
				champids[i] = participants.get(i).getChampionId();
			}
			
			//lookup all summoners in the game to get their names
			Map<String, Summoner> summonersInGame = api.getSummonersById(ids);
			//lookup the leagues of all summoners in game
			Map<String, List<League>> leagues = api.getLeagueEntryBySummoners(ids);
			
			//the 2 teams
			ArrayList<CustomPlayer> team1 = new ArrayList<CustomPlayer>();
			ArrayList<CustomPlayer> team2 = new ArrayList<CustomPlayer>();
			
			long team1id = teamids[0];
			
			//put all the info together to create 2 teams of 5 CustomPlayers
			for(int i = 0; i<participants.size();i++){
				String localName = summonersInGame.get(""+ids[i]).getName();
				CustomPlayer localPlayer = new CustomPlayer(ids[i],teamids[i], localName, champids[i]);
				if(leagues.get(""+ids[i]) != null){
					for(League j : leagues.get(""+ids[i])){
						String tier = j.getTier();
						String division = j.getEntries().get(0).getDivision();
						String queue = j.getQueue();
						localPlayer.addLeague(new CustomLeague(tier,division,queue));
					}
				}
				if(teamids[i] == team1id){
					team1.add(localPlayer);
				} else {
					team2.add(localPlayer);
				}
			}
			
			
			
			//put everything in a readable GUI
			GridPane team1Pane = new GridPane();
			for(int i=0;i<team1.size();i++){
				team1Pane.add(team1.get(i).getGui(), 0, i);
			}
			GridPane team2Pane = new GridPane();
			for(int i=0;i<team2.size();i++){
				team2Pane.add(team2.get(i).getGui(), 0, i);
			}
			
			res.add(team1Pane, 0, 0);
			Text line = new Text("_____________________________________________");
			res.add(line, 0, 1);
			res.add(team2Pane, 0, 2);
			
		} catch (RiotApiException e) {
			//e.printStackTrace();
			Text errorText = new Text("Error looking up game: "+e.getMessage());
			res.add(errorText, 0, 0);
		}
		
		
		return res;
	}
}
