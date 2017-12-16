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

import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.FALSE;

public class LobbyView implements View {

    private Scene lobby;
    private Connection connection;
    private int playerCount = 0;
    private boolean usersSettings = TRUE;

    Label[] lPlayers = new Label[6];
    Label lGameName = new Label("");
    TextField tChat = new TextField();
    TextArea tChatShow = new TextArea();
    ChoiceBox<String> choiceBox = new ChoiceBox<>();
    Label lChoice = new Label("Ilość graczy: ");
    TextField tSize = new TextField();
    Label lSize = new Label("Wielkosć planszy: ");
    Button bStart = new Button("Start");
    Button bLeave = new Button("Wyjdź");


    public LobbyView(Connection connection){
        this.connection = connection;
        for(int i=0; i<6; i++)
            lPlayers[i] = new Label("");
    }

    @Override
    public void parse(String message){
        String[] tmp = message.split(";");
        if(tmp[0].equals("GameDetailedData")){
            usersSettings = FALSE;
            parseGameData(tmp);
        }
        else if(tmp[0].equals("PlayerList")){
            System.out.println("New message");
            parsePlayerList(tmp);
        }
        else if(tmp[0].equals("Remove")){
            String t=tmp[1];
            int k=-1;
            for(int i=0; i<6; i++){
                if(lPlayers[i].getText().equals(t))
                    k=i;
            }
            if(k>=0) {
                if (k < 6 && lPlayers[k].equals(t)) {
                    while (k < 5) {
                        lPlayers[k].setText(lPlayers[k + 1].getText());
                        k++;
                    }
                    lPlayers[5].setText("");
                }
            }
        }
        else if(tmp[0].equals("Msg")){
            if(tChatShow.getText().equals(""))
                tChatShow.setText(tmp[1]+"\n");
            else
                tChatShow.setText(tChatShow.getText()+tmp[1]+"\n");
        }
        else if(tmp[0].equals("Size")){
            tSize.setText(tmp[1]);
        }
        else if(tmp[0].equals("Players")){
            choiceBox.setValue(tmp[1]);
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
            String message="Start;";
            try{
                connection.send(message);
            }
            catch(Exception ex){

            }
        });

        //TODO: goint back to hub
        bLeave.setOnAction(e -> {
            String message="Leave;";
            try{
                connection.send(message);
            }
            catch(Exception ex){

            }
        });

        choiceBox.setOnAction(e ->{
            if(usersSettings) {
                String message = "Settings;Players;";
                message = message.concat(choiceBox.getValue());
                try {
                    connection.send(message);
                } catch (Exception ex) {

                }
            }
        });

        tSize.setOnAction(e ->{
            if(usersSettings) {
                String message = "Settings;Size;";
                message = message.concat(tSize.getText());
                try {
                    connection.send(message);
                } catch (Exception ex) {

                }
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

        tSize.setMaxWidth(40);
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
        gridPaneHubLayout.add(bLeave, 1, 7);
        gridPaneHubLayout.add(lSize, 1, 2);
        gridPaneHubLayout.add(tSize, 2, 2);

        //Setting a scene obect;
        lobby=new Scene(gridPaneHubLayout);
        return lobby;
    }

    private void parseGameData(String[] data){
        //if(lGameName.getText().equals(data[1]))
            lGameName.setText(data[1]);
        //if(choiceBox.getValue().equals(data[3]))
            choiceBox.setValue(data[3]);
        //if(tSize.getText().equals(data[4]))
            tSize.setText(data[4]);
        usersSettings = TRUE;
    }

    private void parsePlayerList(String[] data) {
        try {
            System.out.println("New message:" + lPlayers[playerCount].getText());
            double r = Double.parseDouble(data[2]);
            double g = Double.parseDouble(data[3]);
            double b = Double.parseDouble(data[4]);

            lPlayers[playerCount].setText(data[1]);
            lPlayers[playerCount].setTextFill(new Color(r, g, b, 0.5));

            playerCount++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
