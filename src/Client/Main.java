package Client;

import Client.Network.Connection;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    //TODO: CREATE HUB SCENE
    //Scene hub;
    //TODO: CREATE LOBBY SCENE
    //Scene lobby;
    //TODO: CREATE IN_GAME SCENE
    //Scene in_game;

    //TODO: MOVE TO LOBBY SCENE
   // private TextArea messages = new TextArea();

    //TODO: ASSIGN THIS TO LOBBY SCENE
    /*private Parent createContent(){
        messages.setPrefHeight(550);
        TextField input = new TextField();

        //creates the connection on address and ip
        Connection connection = new Connection("localhost", 5555);
        //starts the connection
        connection.start();
        //TODO: SET PROPER MESSAGE PARSER
        connection.addMessageParser(messages);

        //event handler
        input.setOnAction(event ->{
            String message = input.getText();
            input.clear();
            try {
                connection.send(message);
            }
            catch (Exception e){

            }
        });


        VBox root = new VBox(20, messages, input);
        root.setPrefSize(600, 600);
        return root;
    }*/



    private Stage window;
    private Scene hub;
    private Scene lobby;
    private Scene in_game;
    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;

        //lGame1.setText("text");
        Label lGame1 = new Label("Gra nr: 1\tAktywnych graczy: 4\t");
        Label lGame2 = new Label("Gra nr: 2\tAktywnych graczy: 4\t");
        Label lGame3 = new Label("Gra nr: 3\tAktywnych graczy: 4\t");
        Label lGame4 = new Label("Gra nr: 4\tAktywnych graczy: 4\t");
        Label lGame5 = new Label("Gra nr: 5\tAktywnych graczy: 4\t");
        Label lGame6 = new Label("Gra nr: 6\tAktywnych graczy: 4\t");
        Label lGame7 = new Label("Gra nr: 7\tAktywnych graczy: 4\t");
        Label lGame8 = new Label("Gra nr: 8\tAktywnych graczy: 4\t");
        Label lGame9 = new Label("Gra nr: 9\tAktywnych graczy: 4\t");
        Label lGame10 = new Label("Gra nr:10\tAktywnych graczy: 4\t");

        Button bGame1 = new Button("Dołącz");
        Button bGame2 = new Button("Dołącz");
        Button bGame3 = new Button("Dołącz");
        Button bGame4 = new Button("Dołącz");
        Button bGame5 = new Button("Dołącz");
        Button bGame6 = new Button("Dołącz");
        Button bGame7 = new Button("Dołącz");
        Button bGame8 = new Button("Dołącz");
        Button bGame9 = new Button("Dołącz");
        Button bGame10 = new Button("Dołącz");

        //lambda expression
        bGame1.setOnAction(e -> System.out.println("Enter the game"));
        bGame2.setOnAction(e -> System.out.println("Enter the game"));
        bGame3.setOnAction(e -> System.out.println("Enter the game"));
        bGame4.setOnAction(e -> System.out.println("Enter the game"));
        bGame5.setOnAction(e -> System.out.println("Enter the game"));
        bGame6.setOnAction(e -> System.out.println("Enter the game"));
        bGame7.setOnAction(e -> System.out.println("Enter the game"));
        bGame8.setOnAction(e -> System.out.println("Enter the game"));
        bGame9.setOnAction(e -> System.out.println("Enter the game"));
        bGame10.setOnAction(e -> System.out.println("Enter the game"));

        //Layout
        //instantiatig the GridPane class
        GridPane gridPaneHubLayout = new GridPane();
        gridPaneHubLayout.setMinSize(300, 300);
        gridPaneHubLayout.setPadding(new Insets(10, 10, 10, 10));

        //gridPaneHubLayout.setVgap(10);
        //gridPaneHubLayout.setHgap(10);

        gridPaneHubLayout.setAlignment(Pos.TOP_LEFT);

        //Setting nodes
        gridPaneHubLayout.add(lGame1, 0, 0);
        gridPaneHubLayout.add(bGame1, 1, 0);
        gridPaneHubLayout.add(lGame2, 0, 1);
        gridPaneHubLayout.add(bGame2, 1, 1);
        gridPaneHubLayout.add(lGame3, 0, 2);
        gridPaneHubLayout.add(bGame3, 1, 2);
        gridPaneHubLayout.add(lGame4, 0, 3);
        gridPaneHubLayout.add(bGame4, 1, 3);
        gridPaneHubLayout.add(lGame5, 0, 4);
        gridPaneHubLayout.add(bGame5, 1, 4);
        gridPaneHubLayout.add(lGame6, 0, 5);
        gridPaneHubLayout.add(bGame6, 1, 5);
        gridPaneHubLayout.add(lGame7, 0, 6);
        gridPaneHubLayout.add(bGame7, 1, 6);
        gridPaneHubLayout.add(lGame8, 0, 7);
        gridPaneHubLayout.add(bGame8, 1, 7);
        gridPaneHubLayout.add(lGame9, 0, 8);
        gridPaneHubLayout.add(bGame9, 1, 8);
        gridPaneHubLayout.add(lGame10, 0, 9);
        gridPaneHubLayout.add(bGame10, 1, 9);

        //Setting a scene obect;
        hub=new Scene(gridPaneHubLayout);


        //Swithing the scene: window.setScene( nazwa_sceny );
        window.setTitle("Chinese Checkers");
        //primaryStage.setScene(new Scene(createContent()));
        window.setScene(hub);
        window.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}









    /*    Group root = new Group();
        Canvas canvas = new Canvas(1024, 768);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Map map = new Map(6);
        map.display(gc);

        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();*/
