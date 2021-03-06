package Client.View;

import Client.Network.Connection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class HubView implements View {

    private Connection connection;

    private Label[] lGames = new Label[10];
    private Button[] bEnterGame = new Button[10];
    private TextField tNickname = new TextField();

    public HubView(Connection connection){
        this.connection = connection;
        for(int i=0; i<10; i++) {
            lGames[i] = new Label("");
            bEnterGame[i] = new Button("Dołącz do gry");
        }
    }



    @Override
    public void parse(String message){
        String[] tmp = message.split(";");

        if(tmp[0].equals("GameData")){
            if(tmp.length==6){
                int game = Integer.parseInt(tmp[1]);
                String info = tmp[2];
                info = info.concat("\t\t");
                info = info.concat(tmp[3]+"/"+tmp[4]);

                lGames[game].setText(info);

                switch (tmp[5]) {
                    case "Open":
                        lGames[game].setTextFill(Color.GREEN);
                        break;
                    case "Playing":
                        lGames[game].setTextFill(Color.RED);
                        break;
                    case "Ready to start":
                        lGames[game].setTextFill(Color.YELLOW);
                        break;
                    case "Restarting":
                        lGames[game].setTextFill(Color.BLUE);
                        break;
                    case "Full":
                        lGames[game].setTextFill(Color.ORANGE);
                        break;
                }
            }
        }
        else if(tmp[0].equals("GameDetailedData")){
            Stage stageTheLabelBelongs = (Stage) tNickname.getScene().getWindow();
            View next = new LobbyView(connection);
            connection.setView(next);

            stageTheLabelBelongs.setScene(next.getScene());
            next.parse(message);
        }
    }
    @Override
    public Scene getScene(){

        tNickname.setPromptText("Nickname");
        for(int i=0; i<10; i++) {
            int finalI = i;
            bEnterGame[i].setOnAction(e -> {
                String k = "Nick;";
                if(tNickname.getText().length()>0){
                    k = k.concat(tNickname.getText().replaceAll(";", ":"));
                    String finalK = k;
                    connection.send(finalK);

                    String s = "Join;";
                    s = s.concat(String.valueOf(finalI));
                    String finalS = s;
                    connection.send(finalS);

                }
                else{
                    k = k.concat("Player");
                    Random gen = new Random();
                    int tmp  = gen.nextInt(100);
                    k = k.concat(String.valueOf(tmp));
                    String finalK = k;
                    connection.send(finalK);

                    String s = "Join;";
                    s = s.concat(String.valueOf(finalI));
                    String finalS = s;
                    connection.send(finalS);
                }

            });
        }

        //Layout
        //instantiatig the GridPane class*/
        GridPane gridPaneHubLayout = new GridPane();
        gridPaneHubLayout.setMinSize(300, 300);
        gridPaneHubLayout.setPadding(new Insets(10, 10, 10, 10));

        gridPaneHubLayout.setVgap(10);

        gridPaneHubLayout.setAlignment(Pos.TOP_LEFT);

        //Setting nodes
        for(int i=0; i<10; i++) {
            gridPaneHubLayout.add(lGames[i], 0, i);
            gridPaneHubLayout.add(bEnterGame[i], 1, i);
        }

        gridPaneHubLayout.add(tNickname, 0, 11);

        //Setting a scene obect;
        Scene hub = new Scene(gridPaneHubLayout);
        return hub;
    }
}
