package Client.View;

import Client.Network.Connection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.Random;

public class LobbyView implements View {

    private Scene lobby;
    private Connection connection;

    Label[] lPlayers = new Label[6];
    Label lGameName = new Label("");
    TextField tChat = new TextField();
    TextArea tChatShow = new TextArea();

    public LobbyView(Connection connection){
        this.connection = connection;
        for(int i=0; i<6; i++)
            lPlayers[i] = new Label("");
    }



    @Override
    public void parse(String message){

        //Incoming messages parser
        String[] tmp = message.split(";");
        lGameName.setText("Gra: 0");
    }
    @Override
    public Scene getScene(){

        tChat.setPromptText("Write...");
            //TODO: Specify messages sending to the server.

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

        //Setting a scene obect;
        lobby=new Scene(gridPaneHubLayout);
        return lobby;
    }


}
