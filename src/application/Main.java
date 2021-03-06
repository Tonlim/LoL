package application;
	
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout.Alignment;

import constant.PlatformId;
import constant.PlatformId;
import constant.Region;
import constant.Region;
import dto.CurrentGame.CurrentGameInfo;
import dto.CurrentGame.CurrentGameInfo;
import dto.CurrentGame.Participant;
import dto.League.League;
import dto.CurrentGame.Participant;
import dto.Summoner.Summoner;
import dto.Summoner.Summoner;
import iohandling.APIkey;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.Stage;
import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;
import main.java.riotapi.RiotApiException;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		testingApiNick(primaryStage);
		
		//uncomment the following line for the old main
		//oldMain(primaryStage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void oldMain(Stage primaryStage){
		//below: code from commit Kwinten 11-07-15, 15u
				
				try {
					RiotApi api = new RiotApi(APIkey.getKey());
					api.setRegion(Region.EUW);
					Map<String, Summoner> summoners = api.getSummonersByName("tonlim, elite cart, wolftooth1, zerpee, disney fired me,unrez, colotol");
					Summoner summoner = summoners.get("colotol");
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
						
						
						GridPane root = constructRootPane(5,2,id);
						
						Scene scene = new Scene(root,1250,400);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setScene(scene);
						primaryStage.show();
						
						//test code (old code below)
						
						/*long[] idds = new long[10];
						for(int i=0;i<10;i++){
							idds[i] = participants.get(i).getSummonerId();
							teamids.put(""+participants.get(i).getSummonerId(), participants.get(i).getTeamId());
							champids.put(""+participants.get(i).getSummonerId(), participants.get(i).getChampionId());
						}
						*/
						

						/*
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
			
					/*
					//TESTING CODE for CustomPlayer
					CustomPlayer testy = new CustomPlayer(summoner.getId(),0,summoner.getName(),0);
					testy.addLeague(new CustomLeague("GOLD","V","SOLO_RANKED_5X5"));
					
					//TESTING CODE for Database
					for(Champion i : Database.getDatabase().getChampions()){
						System.out.println(i.getName());
					}
					
					
					
					BorderPane root = new BorderPane();
					root.setCenter(testy.getGui());
					
					Scene scene = new Scene(root,400,400);
					scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					primaryStage.setScene(scene);
					primaryStage.show();
					*/

				} catch(Exception e) {
					e.printStackTrace();
				}
				
	}
	
	private GridPane constructRootPane(int columns,int rows,long id) throws RiotApiException{
		RiotApi api = new RiotApi(APIkey.getKey());
		CurrentGameInfo currentGame = api.getCurrentGameInfo(PlatformId.EUW, id);
		List<Participant> participants = currentGame.getParticipants();
		Map<String, List<League>> mapleague = api.getLeagueEntryBySummoners(id);
		GridPane result = new GridPane();
		for(int i = 0; i < columns;i++){
			result.getColumnConstraints().add(new ColumnConstraints(250));
		}
		//colindex must be indexed with the row number contains the (vertical) grid index where the node should be added
		int[] colindex= new int[rows];
		for(Participant p : participants){
			int row = (int)(p.getTeamId()/100);
			int column = colindex[row-1];
			colindex[row-1]++;
			VBox vb = new VBox();
			vb.setAlignment(Pos.CENTER);
			vb.getStyleClass().add("championpanel");
			Text summonerName= new Text(p.getSummonerName());
			vb.getChildren().add(summonerName);
			Summoner summoner = api.getSummonerById(p.getSummonerId());
			//summoner.
			result.add(vb, column, row);
		}
		
		return result;
	}
	
	private void testingApiNick(Stage primaryStage){
		BorderPane root = new BorderPane();
		root.setCenter(CurrentGameLookUp.getGui("tonlim"));
		Scene scene = new Scene(root,600,700);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
}
