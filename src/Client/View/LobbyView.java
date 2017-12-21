package Client.View;

import Client.Network.Connection;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.Stage;

import java.util.Random;

import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.FALSE;

public class LobbyView implements View {

    private Scene lobby;
    private Connection connection;
    private int playerCount = 0;
    private boolean usersSettings = TRUE;
    private boolean master = FALSE;


    GridPane gridPaneHubLayout = new GridPane();

    Label[] lPlayers = new Label[6];
    Label[] lNumbers = new Label[6];
    Label lGameName = new Label("");
    TextField tChat = new TextField();
    TextArea tChatShow = new TextArea();
    ChoiceBox<String> choiceBox = new ChoiceBox<>();
    Label lChoice = new Label("Ilość graczy: ");
    TextField tSize = new TextField();
    Label lSize = new Label("Wielkosć planszy: ");
    Button bStart = new Button("Start");
    Button bLeave = new Button("Wyjdź");
    Button bKick = new Button ("Wyrzuć");
    ChoiceBox<String> cKick = new ChoiceBox<>();
    CheckBox[] cRules = new CheckBox[3];
    Button bAddBot = new Button("Dodaj bota");

    public LobbyView(Connection connection){
        this.connection = connection;
        for(int i=0; i<6; i++) {
            lPlayers[i] = new Label("");
            lNumbers[i] = new Label(String.valueOf(i+1));
        }

        cRules[0] = new CheckBox("Ruch na jedno pole obok");
        cRules[1] = new CheckBox("Przeskoczenie jednego, dowolnego pionka");
        cRules[2] = new CheckBox("Przeskoczenie dwoch pustych pol");

        for(int i=0; i<3; i++) {
            int finalI = i;
            cRules[i].selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (cRules[finalI].isSelected()) {
                        String msg = "Settings;RuleOn;";
                        msg = msg.concat(cRules[finalI].getText());
                        connection.send(msg);
                        System.out.println(msg);
                    } else {
                        String msg = "Settings;RuleOff;";
                        msg = msg.concat(cRules[finalI].getText());
                        connection.send(msg);
                        System.out.println(msg);
                    }
                }
            });
        }
        tChatShow.setEditable(FALSE);
        tChatShow.setMouseTransparent(TRUE);
        tChatShow.setFocusTraversable(FALSE);
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
                if(lPlayers[i].getText().equals(t)) {
                    k = i;
                    playerCount--;
                }
            }
            if(k>=0) {
                if (k < 5 && lPlayers[k].getText().equals(t)) {
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
        else if(tmp[0].equals("Start")){
            try{
                //connection.send(message);
                Stage stageTheLabelBelongs = (Stage) lGameName.getScene().getWindow();
                //create new view and prepare connection
                View next = new InGameView(connection);
                connection.setView(next);

                //crate javafx-friendly thread which will call the change

                stageTheLabelBelongs.setScene(next.getScene());
                //run the javafx-friendly thread
                //task.run();
                next.parse(message);
                connection.send(message);
            }
            catch(Exception ex){

            }
        }
        /*else if(tmp[0].equals("Size")){
            usersSettings = FALSE;
            tSize.setText(tmp[1]);
            usersSettings = TRUE;
        }
        else if(tmp[0].equals("Players")){
            usersSettings = FALSE;
            choiceBox.setValue(tmp[1]);
            usersSettings = TRUE;
        }*/
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
            if(bStart.getText().equals("Start")) {
                String message = "Ready;";
                bStart.setText("Anuluj");
                try {
                    connection.send(message);
                } catch (Exception ex) {

                }
            }
            else if(bStart.getText().equals("Anuluj")) {
                String message = "Cancel;";
                bStart.setText("Start");
                try {
                    connection.send(message);
                } catch (Exception ex) {

                }
            }
        });

        //TODO: goint back to hub
        bLeave.setOnAction(e -> {
            String message="Leave;";
            try{
                //connection.send(message);
                Stage stageTheLabelBelongs = (Stage) lGameName.getScene().getWindow();
                //create new view and prepare connection
                View next = new HubView(this.connection);
                connection.setView(next);

                //crate javafx-friendly thread which will call the change

                stageTheLabelBelongs.setScene(next.getScene());
                //run the javafx-friendly thread
                //task.run();
                next.parse(message);
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

        bKick.setOnAction(e ->{
            String message = "Kick;";
            try {
                message = message.concat(cKick.getValue());
                connection.send(message);
            } catch (Exception ex) {

            }
        });

        bAddBot.setOnAction(e ->{
            String message = "AddBot;";
            try {
                connection.send(message);
            } catch (Exception ex) {

            }
        });

        //Layout
        //instantiatig the GridPane class*/
        //GridPane gridPaneHubLayout = new GridPane();
        gridPaneHubLayout.setMinSize(300, 300);
        gridPaneHubLayout.setPadding(new Insets(10, 10, 10, 10));

        gridPaneHubLayout.setVgap(10);
        //gridPaneHubLayout.setHgap(10);

        gridPaneHubLayout.setAlignment(Pos.TOP_LEFT);

        tSize.setMaxWidth(40);
        //Setting nodes
        for(int i=0; i<6; i++) {
            gridPaneHubLayout.add(lNumbers[i], 0, i);
            gridPaneHubLayout.add(lPlayers[i], 1, i);
        }

        gridPaneHubLayout.add(lGameName, 2, 0);
        gridPaneHubLayout.add(tChatShow, 1, 7);
        gridPaneHubLayout.add(tChat, 1, 8);
        gridPaneHubLayout.add(lChoice, 2, 1);
        gridPaneHubLayout.add(choiceBox, 3, 1);
        gridPaneHubLayout.add(bStart, 3, 0);
        gridPaneHubLayout.add(bLeave, 3, 8);
        gridPaneHubLayout.add(lSize, 2, 2);
        gridPaneHubLayout.add(tSize, 3, 2);
        gridPaneHubLayout.add(bAddBot, 3, 7);
        for(int i=0; i<3; i++)
            gridPaneHubLayout.add(cRules[i], 2, i+4);


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
            boolean on = FALSE;
            boolean off = FALSE;
            for(int i=5; i<12; i++){
                if(data[i].equals("RuleOn"))
                    on = TRUE;
                else if(data[i].equals("RuleOff")) {
                    off = TRUE;
                    on = FALSE;
                }
                if(on){
                    i++;
                    try{
                        cRules[Integer.parseInt(data[i])].setSelected(TRUE);
                    }catch(Exception e){

                    }
                }
                else if(off){
                    i++;
                    try{
                        cRules[Integer.parseInt(data[i])].setSelected(FALSE);
                    }catch(Exception e){

                    }
                }
            }
        usersSettings = TRUE;
            if(data[2].equals("0")) {
                gridPaneHubLayout.add(bKick, 3, 3);
                gridPaneHubLayout.add(cKick, 2, 3);
                cKick.getItems().addAll("1", "2", "3", "4", "5", "6");
            }

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
