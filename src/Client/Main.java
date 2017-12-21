package Client;

import Client.Network.Connection;
import Client.View.HubView;
import Client.View.InGameView;
import Client.View.LobbyView;
import Client.View.View;
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
import java.util.Random;
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;

public class Main extends Application {


    private Stage window;
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

    View curret;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;

        //creates the connection on address and ip
        Connection connection = new Connection("localhost", 5555);
        curret = new InGameView(connection);
        //curret = new LobbyView(connection);
        connection.setView(curret);

        //starts the connection
        if(!connection.start()){
            System.out.println("CANT ESTABLISH CONNECTION!");
            //TODO: BRING UP CONNECTING MENU
        }






        //Swithing the scene: window.setScene( nazwa_sceny );
        window.setTitle("Chinese Checkers");
        //primaryStage.setScene(new Scene(createContent()));
        window.setScene(curret.getScene());
        window.show();

        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                connection.send("Leave");
                System.out.println("Stage is closing");
                System.exit(0);

            }
        });
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
