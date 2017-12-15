package Client.View;

import Client.Network.Connection;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Random;

public class LobbyView implements View {

    private Scene lobby;
    private Connection connection;
    private int playerCount = 0;

    Label[] lPlayers = new Label[6];
    Label lGameName = new Label("");
    TextField tChat = new TextField();
    TextArea tChatShow = new TextArea();
    ChoiceBox<String> choiceBox = new ChoiceBox<>();
    Label lChoice = new Label("Ilość graczy: ");
    Button bStart = new Button("Start");


    public LobbyView(Connection connection){
        this.connection = connection;
        for(int i=0; i<6; i++)
            lPlayers[i] = new Label("");
    }

    @Override
    public void parse(String message){
        String[] tmp = message.split(";");
        if(tmp[0].equals("GameDetailedData")){
            parseGameData(tmp);

        }
        else if(tmp[0].equals("PlayerList")){
            System.out.println("New message");
            parsePlayerList(tmp);
        }
    }
    @Override
    public Scene getScene(){

        tChat.setPromptText("Write...");
            //TODO: Specify messages sending to the server.
        tChat.setOnAction(e ->{
            String message = "Msg;";
            message=message.concat(tChat.getText());
            tChat.clear();
            try {
                connection.send(message);
            }
            catch (Exception ex){

            }
        });


        choiceBox.getItems().addAll("2", "3", "4", "6");

        bStart.setOnAction(e -> {
            String message="Players;";
            message=message.concat(choiceBox.getValue());
            try{
                connection.send(message);
            }
            catch(Exception ex){

            }
        });

        //Layout
        //instantiatig the GridPane class*/
        GridPane gridPaneHubLayout = new GridPane();
        gridPaneHubLayout.setMinSize(300, 300);
        gridPaneHubLayout.setPadding(new Insets(10, 10, 10, 10));

        gridPaneHubLayout.setVgap(10);
        //gridPaneHubLayout.setHgap(10);

        gridPaneHubLayout.setAlignment(Pos.TOP_LEFT);

        //Setting nodes
        for(int i=0; i<6; i++) {
            gridPaneHubLayout.add(lPlayers[i], 0, i);
        }

        gridPaneHubLayout.add(lGameName, 1, 0);
        gridPaneHubLayout.add(tChatShow, 0, 6);
        gridPaneHubLayout.add(tChat, 0, 7);
        gridPaneHubLayout.add(lChoice, 1, 1);
        gridPaneHubLayout.add(choiceBox, 2, 1);
        gridPaneHubLayout.add(bStart, 2, 7);

        //Setting a scene obect;
        lobby=new Scene(gridPaneHubLayout);
        return lobby;
    }

    private void parseGameData(String[] data){
        lGameName.setText(data[1]);
        choiceBox.setValue(data[3]);
    }

    private void parsePlayerList(String[] data) {
        try {
            System.out.println("New message:" + lPlayers[playerCount].getText());
            double r = Double.parseDouble(data[2]);
            double g = Double.parseDouble(data[3]);
            double b = Double.parseDouble(data[4]);
            setTextOnJavaFX(lPlayers[playerCount], data[1], new Color(r, g, b, 0.5));
            playerCount++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //example method to be javafx-friendly
    void setTextOnJavaFX(Labeled labeled, String value, Color color) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        labeled.setText(value);
                        labeled.setTextFill(color);
                    }
                });
                return null;
            }
        };
        task.run();
    }




}
