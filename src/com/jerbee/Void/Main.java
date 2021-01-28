package com.jerbee.Void;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("format.game"));
			Scene scene = new Scene(root,1080,700);
			primaryStage.setScene(scene);
			primaryStage.setWidth(880);
			primaryStage.setHeight(620);
			primaryStage.setResizable(false);
			primaryStage.show();
			primaryStage.setTitle("Infinite Void Game");
		} catch(Exception e) {e.printStackTrace();}
	}
	public static void main(String[] args) {launch(args);}
}
