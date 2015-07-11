package data;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class CustomLeague {
	String tier;
	String division;
	String queue;
	
	
	public CustomLeague(String tier, String division, String queue){
		this.tier = tier;
		this.division = division;
		this.queue = queue;
	}
	
	public String getTier(){
		return tier;
	}
	
	public String getDivision(){
		return division;
	}
	
	public String getQueue(){
		return queue;
	}
	
	public Pane getGui(){
		GridPane res = new GridPane();
		Text tierText = new Text(tier);
		tierText.setWrappingWidth(75);
		Text divisionText = new Text(division);
		divisionText.setWrappingWidth(10);
		Text queueText = new Text(queue);
		queueText.setWrappingWidth(105);
		res.add(tierText, 0, 0);
		res.add(divisionText, 1, 0);
		res.add(queueText, 2, 0);
		res.setHgap(5); 	//horizontal padding
		return res;
	}
	
}
