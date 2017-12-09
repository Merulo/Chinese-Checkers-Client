package Client;

import Client.Network.Connection;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    //TODO: CREATE HUB SCENE
    Scene hub;
    //TODO: CREATE LOBBY SCENE
    Scene lobby;
    //TODO: CREATE IN_GAME SCENE
    Scene in_game;

    //TODO: MOVE TO LOBBY SCENE
    private TextArea messages = new TextArea();

    //TODO: ASSIGN THIS TO LOBBY SCENE
    private Parent createContent(){
        messages.setPrefHeight(550);
        TextField input = new TextField();

        //creates the connection on adress and ip
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
    }



    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chinese Checkers");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();

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
