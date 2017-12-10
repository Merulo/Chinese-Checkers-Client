package Client;

import Client.Network.Connection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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


    private Stage window;
    private Scene hub;
    //TODO: lobby Scene
    private Scene lobby;
    //TODO: in_game Scene
    private Scene in_game;

    //TODO: ASSIGN THIS TO LOBBY SCENE
    /*private Parent createContent(){
        TextArea messages = new TextArea();
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

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;

        //creates the connection on address and ip
        Connection connection = new Connection("localhost", 5555);
        //starts the connection
        connection.start();


        Label[] lGames = new Label[10];
        Button[] bEnterGame = new Button[10];
        for(int i=0; i<10; i++) {
            lGames[i] = new Label("");
            bEnterGame[i] = new Button("Dołącz do gry");
            //TODO: Specify messages sending to the server.
            String s="!";
            s=s.concat(String.valueOf(i));
            String finalS = s;
            bEnterGame[i].setOnAction(e -> connection.send(finalS));
        }

        //Layout
        //instantiatig the GridPane class*/
        GridPane gridPaneHubLayout = new GridPane();
        gridPaneHubLayout.setMinSize(300, 300);
        gridPaneHubLayout.setPadding(new Insets(10, 10, 10, 10));

        gridPaneHubLayout.setVgap(10);
        //gridPaneHubLayout.setHgap(10);

        gridPaneHubLayout.setAlignment(Pos.TOP_LEFT);

        //Setting nodes
        for(int i=0; i<10; i++) {
            gridPaneHubLayout.add(lGames[i], 0, i);
            gridPaneHubLayout.add(bEnterGame[i], 1, i);
        }

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
