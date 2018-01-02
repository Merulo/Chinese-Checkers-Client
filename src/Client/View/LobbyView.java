package Client.View;

import Client.Network.Connection;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.FALSE;

public class LobbyView implements View {

    private final Connection connection;
    private int playerCount = 0;
    private boolean usersSettings = TRUE;
    private boolean master = FALSE;
    private int position = -1;

    private final double [][]colors = new double[6][3];

    private final GridPane gridPaneHubLayout = new GridPane();

    private final Label[] lPlayers = new Label[6];
    private final Label[] lNumbers = new Label[6];
    private final Label lGameName = new Label("");
    private final TextField tChat = new TextField();
    private final TextArea tChatShow = new TextArea();
    private final ChoiceBox<String> choiceBox = new ChoiceBox<>();
    private final Label lChoice = new Label("Ilość graczy: ");
    private final TextField tSize = new TextField();
    private final Label lSize = new Label("Wielkosć planszy: ");
    private final Button bStart = new Button("Start");
    private final Button bLeave = new Button("Wyjdź");
    private final Button bKick = new Button ("Wyrzuć");
    private final ChoiceBox<String> cKick = new ChoiceBox<>();
    private final CheckBox[] cRules = new CheckBox[3];
    private final Button bAddBot = new Button("Dodaj bota");

    LobbyView(Connection connection){
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
            cRules[i].selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (cRules[finalI].isSelected()) {
                    if( usersSettings){
                        String msg = "Settings;RuleOn;";
                        msg = msg.concat(cRules[finalI].getText());
                        connection.send(msg);
                    }
                } else {
                    if(usersSettings){
                        String msg = "Settings;RuleOff;";
                        msg = msg.concat(cRules[finalI].getText());
                        connection.send(msg);
                    }
                }
            });
        }
        tChatShow.setEditable(FALSE);
        tChatShow.setFocusTraversable(FALSE);
        tChatShow.textProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> tChatShow.setScrollTop(Double.MAX_VALUE));
    }

    @Override
    public void parse(String message){
        String[] tmp = message.split(";");
        switch (tmp[0]) {
            case "GameDetailedData":
                usersSettings = FALSE;
                parseGameData(tmp);
                break;
            case "PlayerList":
                parsePlayerList(tmp);
                break;
            case "Remove": {
                int k = -1;
                playerCount--;
                try {
                    k = Integer.parseInt(tmp[1]);
                } catch (Exception e) {
                    System.out.println("Parse problem occured");
                }
                while (k < 5) {
                    lPlayers[k].setText(lPlayers[k+1].getText());
                    lPlayers[k].setTextFill(lPlayers[k+1].getTextFill());
                    k++;
                }
                lPlayers[5].setText("");
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
                    Stage stageTheLabelBelongs = (Stage) lGameName.getScene().getWindow();
                    //create new view and prepare connection
                    View next = new InGameView(connection, Integer.parseInt(tSize.getText()), playerCount, colors);
                    connection.setView(next);

                    //crate javafx-friendly thread which will call the change
                    stageTheLabelBelongs.setScene(next.getScene());
                    //run the javafx-friendly thread
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
                break;
            }
            case "Master":
                master = TRUE;
                break;
        }
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
                Stage stageTheLabelBelongs = (Stage) lGameName.getScene().getWindow();
                //create new view and prepare connection
                View next = new HubView(this.connection);
                connection.setView(next);

                //crate javafx-friendly thread which will call the change

                stageTheLabelBelongs.setScene(next.getScene());
                //run the javafx-friendly thread
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
                String num = tSize.getText();
                if(num.matches("-?\\d+(\\.\\d+)?")){
                    try {
                        message = message.concat(num);
                        connection.send(message);
                    } catch (Exception ignored) {

                    }
                }
                else{
                    try {
                        num="5";
                        message = message.concat(num);
                        connection.send(message);
                    } catch (Exception ignored) {

                    }
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
        gridPaneHubLayout.setMinSize(300, 300);
        gridPaneHubLayout.setPadding(new Insets(10, 10, 10, 10));

        gridPaneHubLayout.setVgap(10);
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
        return new Scene(gridPaneHubLayout);
    }

    private void parseGameData(String[] data){
        lGameName.setText(data[1]);
        choiceBox.setValue(data[3]);
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
                    try{
                        if(!cRules[Integer.parseInt(data[i])].isSelected())
                            cRules[Integer.parseInt(data[i])].setSelected(TRUE);
                    }catch(Exception ignored){

                    }
                }
                else if(off){
                    try{
                        if(cRules[Integer.parseInt(data[i])].isSelected())
                            cRules[Integer.parseInt(data[i])].setSelected(FALSE);
                    }catch(Exception ignored){

                    }
                }
            }
        usersSettings = TRUE;
    }

    private void parsePlayerList(String[] data) {
        try {
            double r = Double.parseDouble(data[2]);
            double g = Double.parseDouble(data[3]);
            double b = Double.parseDouble(data[4]);

            lPlayers[playerCount].setText(data[1]);
            lPlayers[playerCount].setTextFill(new Color(r, g, b, 0.5));

            colors[playerCount][0]=r;
            colors[playerCount][1]=g;
            colors[playerCount][2]=b;

            playerCount++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
