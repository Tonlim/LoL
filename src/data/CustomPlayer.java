package data;

import java.util.ArrayList;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class CustomPlayer {
	long id;
	long teamid;
	String name;
	long championid;
	String championName;
	ArrayList<CustomLeague> leagues = new ArrayList<CustomLeague>();
	
	
	public CustomPlayer(long id, long teamid, String name, long championid){
		this.id = id;
		this.teamid = teamid;
		this.name = name;
		this.championid = championid;
		//TODO: lookup championName using championid
	}
	
	public void addLeague(CustomLeague toadd){
		leagues.add(toadd);
	}
	
	public long getTeamid(){
		return teamid;
	}
	
	public Pane getGui(){
		GridPane res = new GridPane();

		Text nameText = new Text(name);
		nameText.setWrappingWidth(Constants.nameWidth);
		Text championNameText = new Text(championName);
		championNameText.setWrappingWidth(Constants.championNameWidth);
		
		GridPane leaguesPane = new GridPane();
		for(int i = 0;i<leagues.size();i++){
			leaguesPane.add(leagues.get(i).getGui(), 0, i);
		}
		
		res.add(nameText, 0, 0);
		res.add(championNameText, 1, 0);
		res.add(leaguesPane, 2, 0);
		
		return res;
	}
	
}
