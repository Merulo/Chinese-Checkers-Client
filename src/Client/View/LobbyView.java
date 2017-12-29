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
import static java.lang.Thread.sleep;

public class LobbyView implements View {

    private Scene lobby;
    private Connection connection;
    private int playerCount = 0;
    private boolean usersSettings = TRUE;
    private boolean master = FALSE;
    private int position = -1;
    private double cR;
    private double cG;
    private double cB;

    private double [][]colors = new double[6][3];


    private GridPane gridPaneHubLayout = new GridPane();

    private Label[] lPlayers = new Label[6];
    private Label[] lNumbers = new Label[6];
    private Label lGameName = new Label("");
    private TextField tChat = new TextField();
    private TextArea tChatShow = new TextArea();
    private ChoiceBox<String> choiceBox = new ChoiceBox<>();
    private Label lChoice = new Label("Ilość graczy: ");
    private TextField tSize = new TextField();
    private Label lSize = new Label("Wielkosć planszy: ");
    private Button bStart = new Button("Start");
    private Button bLeave = new Button("Wyjdź");
    private Button bKick = new Button ("Wyrzuć");
    private ChoiceBox<String> cKick = new ChoiceBox<>();
    private CheckBox[] cRules = new CheckBox[3];
    private Button bAddBot = new Button("Dodaj bota");
    //Alert alert = new Alert(Alert.AlertType.INFORMATION);

    LobbyView(Connection connection){
        this.connection = connection;
        for(int i=0; i<6; i++) {
            lPlayers[i] = new Label("");
            lNumbers[i] = new Label(String.valueOf(i+1));
        }

        //alert.setTitle("Zaczynamy");
        //alert.setHeaderText(null);
        //alert.setContentText("Rozpoczęto odliczanie do startu");

        cRules[0] = new CheckBox("Ruch na jedno pole obok");
        cRules[1] = new CheckBox("Przeskoczenie jednego, dowolnego pionka");
        cRules[2] = new CheckBox("Przeskoczenie dwoch pustych pol");

        for(int i=0; i<3; i++) {
            int finalI = i;
            cRules[i].selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (cRules[finalI].isSelected()) {
                    if(master && usersSettings){
                        String msg = "Settings;RuleOn;";
                        msg = msg.concat(cRules[finalI].getText());
                        connection.send(msg);
                        System.out.println(msg);
                    }
                    /*else{
                        cRules[finalI].setSelected(FALSE);
                    }*/
                    else if(!master && !usersSettings){
                        cRules[finalI].setSelected(TRUE);
                    }
                } else {
                    if(master && usersSettings){
                        String msg = "Settings;RuleOff;";
                        msg = msg.concat(cRules[finalI].getText());
                        connection.send(msg);
                        System.out.println(msg);
                    }
                    /*else{
                        cRules[finalI].setSelected(TRUE);
                    }*/
                    else if(!master && !usersSettings){
                        cRules[finalI].setSelected(FALSE);
                    }
                }
            });
        }
        tChatShow.setEditable(FALSE);
        //tChatShow.setMouseTransparent(TRUE);
        tChatShow.setFocusTraversable(FALSE);
        tChatShow.textProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> tChatShow.setScrollTop(Double.MAX_VALUE));
    }

    @Override
    public void parse(String message){
        System.out.println(message);
        String[] tmp = message.split(";");
        switch (tmp[0]) {
            case "GameDetailedData":
                usersSettings = FALSE;
                parseGameData(tmp);
                break;
            case "PlayerList":
                System.out.println("New message");
                parsePlayerList(tmp);
                break;
            case "Remove": {
                String t = tmp[1];
                int k = -1;
                try {
                    k = Integer.parseInt(tmp[1]);
                } catch (Exception e) {
                    System.out.println("Parse problem occured");
                }
            /*for(int i=0; i<6; i++){
                if(lPlayers[i].getText().equals(t)) {
                    k = i;
                    playerCount--;
                    i=6;
                }
            }*/
                if (k >= 0) {
                    if (k < 5 && lPlayers[k].getText().equals(t)) {
                        while (k < 5) {
                            lPlayers[k].setText(lPlayers[k + 1].getText());
                            k++;
                        }
                        lPlayers[5].setText("");
                    }
                }
                break;
            }
            case "Msg":
                if (tChatShow.getText().equals("")) {
                    tChatShow.setText(message.substring(4) + "\n");
                    tChatShow.selectPositionCaret(tChatShow.getLength());
                    tChatShow.deselect();
                } else {
                    tChatShow.setText(tChatShow.getText() + tmp[1] + "\n");
                    tChatShow.selectPositionCaret(tChatShow.getLength());
                    tChatShow.deselect();
                }
                break;
            case "Close":
                bStart.setText("Start");
                if (tmp.length > 1) {
                    tChatShow.setText(tChatShow.getText() + tmp[1] + "\n");
                }
                break;
            case "Cancel":
                bStart.setText("Start");
                break;
            case "Start":
                try {
                    //connection.send(message);
                    Stage stageTheLabelBelongs = (Stage) lGameName.getScene().getWindow();
                    //create new view and prepare connection
                    View next = new InGameView(connection, Integer.parseInt(tSize.getText()), playerCount, colors, cR, cG, cB);
                    connection.setView(next);

                    //crate javafx-friendly thread which will call the change
                    stageTheLabelBelongs.setScene(next.getScene());
                    //run the javafx-friendly thread
                    //task.run();
                    next.parse(message);
                    connection.send(message);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case "Countdown": {
                int t = Integer.parseInt(tmp[1]) + 1;
                tChatShow.setText(tChatShow.getText() + "<Serwer> Odliczanie do startu: " + String.valueOf(t) + "\n");
                tChatShow.selectPositionCaret(tChatShow.getLength());
                tChatShow.deselect();
                /*try {
                    //int t = Integer.parseInt(tmp[1])+1;
                    //alert.close();
                    //alert.setContentText(String.valueOf(t));
                    //alert.show();
                    //sleep(1000);
                    //alert.close();
                } catch (Exception e) {
                    try {
                        //alert.show();
                        //sleep(1000);
                        //alert.close();
                    } catch (Exception ex) {

                    }
                }*/
                break;
            }
            case "Master":
                master = TRUE;
                break;
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
        tChat.setOnAction(e ->{
            String message = "Msg;";
            message=message.concat(tChat.getText());
            tChat.clear();
            try {
                connection.send(message);
            }
            catch (Exception ignored){

            }
        });


        choiceBox.getItems().addAll("2", "3", "4", "6");

        bStart.setOnAction(e -> {
            if(bStart.getText().equals("Start")) {
                String message = "Ready;";
                bStart.setText("Anuluj");
                try {
                    connection.send(message);
                } catch (Exception ignored) {

                }
            }
            else if(bStart.getText().equals("Anuluj")) {
                String message = "Cancel;";
                bStart.setText("Start");
                try {
                    connection.send(message);
                } catch (Exception ignored) {

                }
            }
        });

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
            catch(Exception ignored){

            }
        });

        choiceBox.setOnAction(e ->{
            if(usersSettings) {
                String message = "Settings;Players;";
                message = message.concat(choiceBox.getValue());
                try {
                    connection.send(message);
                } catch (Exception ignored) {

                }
            }
        });

        tSize.setOnAction(e ->{
            if(usersSettings) {
                String message = "Settings;Size;";
                message = message.concat(tSize.getText());
                try {
                    connection.send(message);
                } catch (Exception ignored) {

                }
            }
        });

        bKick.setOnAction(e ->{
            String message = "Kick;";
            if(master){
                try {
                    message = message.concat(cKick.getValue());
                    connection.send(message);
                } catch (Exception ignored) {

                }
            }
        });

        bAddBot.setOnAction(e ->{
            String message = "AddBot;";
            if(master){
                try {
                    connection.send(message);
                } catch (Exception ignored) {

                }
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

        gridPaneHubLayout.add(bKick, 3, 3);
        gridPaneHubLayout.add(cKick, 2, 3);
        cKick.getItems().addAll("1", "2", "3", "4", "5", "6");

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
        if(position==-1)
            position = Integer.parseInt(data[2]);
            boolean on = FALSE;
            boolean off = FALSE;
            for(int i=5; i<14; i++){
                if(data[i].equals("RuleOn"))
                    on = TRUE;
                else if(data[i].equals("RuleOff")) {
                    off = TRUE;
                    on = FALSE;
                }
                if(on){
                    System.out.println("On, "+i+" ");
                    try{
                        if(!cRules[Integer.parseInt(data[i])].isSelected())
                            cRules[Integer.parseInt(data[i])].setSelected(TRUE);
                    }catch(Exception ignored){

                    }
                }
                else if(off){
                    System.out.println("Off, "+i+" ");
                    try{
                        if(cRules[Integer.parseInt(data[i])].isSelected())
                            cRules[Integer.parseInt(data[i])].setSelected(FALSE);
                    }catch(Exception ignored){

                    }
                }
            }
        usersSettings = TRUE;
            //if(data[2].equals("0")) {
            //if(master){
                /*gridPaneHubLayout.add(bKick, 3, 3);
                gridPaneHubLayout.add(cKick, 2, 3);
                cKick.getItems().addAll("1", "2", "3", "4", "5", "6");*/
            //}

    }

    private void parsePlayerList(String[] data) {
        try {
            System.out.println("New message:" + lPlayers[playerCount].getText());
            double r = Double.parseDouble(data[2]);
            double g = Double.parseDouble(data[3]);
            double b = Double.parseDouble(data[4]);

            lPlayers[playerCount].setText(data[1]);
            lPlayers[playerCount].setTextFill(new Color(r, g, b, 0.5));

            colors[playerCount][0]=r;
            colors[playerCount][1]=g;
            colors[playerCount][2]=b;

            playerCount++;

            if(position == 0){
                cR = r;
                cG = g;
                cB = b;
                position--;
            }
            else{
                position--;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
