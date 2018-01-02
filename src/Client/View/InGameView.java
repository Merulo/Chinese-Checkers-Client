package Client.View;

import Client.Map.Map;
import Client.Network.Connection;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class InGameView implements View {

    private final Connection connection;
    private final Map mapa;


    private final Group group = new Group();
    private final Pane gridPaneHubLayout = new Pane(group);


    private final TextField tChat = new TextField();
    private final TextArea tChatShow = new TextArea();
    private final Button bMoves = new Button("Wyślij ruchy");
    private final Button bAbort = new Button("Anuluj ruch");
    private final Button bBackToHub = new Button("Wróc do Hub");
    private final Button bSkip = new Button("Pomiń ruch");

    public InGameView(Connection connection, int s, int counter, double[][] colors){
        this.connection = connection;
        int tmp = s*(s-1)/2;
        tChatShow.setEditable(FALSE);
        tChatShow.setFocusTraversable(FALSE);
        tChatShow.textProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> tChatShow.setScrollTop(Double.MAX_VALUE));
        mapa = new Map(tmp, connection, gridPaneHubLayout, counter, colors);

    }

    public void parse(String message){
        String[] tmp = message.split(";");
        switch (tmp[0]) {
            case "Msg":
                if (tChatShow.getText().equals("")) {
                    tChatShow.setText(tmp[1] + "\n");
                    tChatShow.selectPositionCaret(tChatShow.getLength());
                    tChatShow.deselect();
                } else {
                    tChatShow.setText(tChatShow.getText() + tmp[1] + "\n");
                    tChatShow.selectPositionCaret(tChatShow.getLength());
                    tChatShow.deselect();
                }
                break;
            case "Move":
                String[] tmpA = tmp[1].split(",");
                String[] tmpB = tmp[2].split(",");
                mapa.makeMove(Integer.parseInt(tmpA[0]), Integer.parseInt(tmpA[1]), Integer.parseInt(tmpB[0]), Integer.parseInt(tmpB[1]));
                break;
            case "YourTurn":
                mapa.setSent(FALSE);
                mapa.underlineColor(TRUE);
                break;
            case "IncorrectMove":
                mapa.setSent(FALSE);
                mapa.underlineColor(TRUE);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Błędny rcuh!");
                alert.setHeaderText(null);
                alert.setContentText("Popraw ruch.");

                alert.showAndWait();
                break;
            case "YourColor":
                double cR = Double.parseDouble(tmp[3]);
                double cG = Double.parseDouble(tmp[4]);
                double cB = Double.parseDouble(tmp[5]);
                mapa.display(cR, cG, cB);
                break;
        }
    }

    public Scene getScene(){

        tChat.setPromptText("Write...");
        tChat.setOnAction(e ->{
            String message = "Msg;";
            message=message.concat(tChat.getText());
            tChat.clear();
            try {
                connection.send(message);
            }
            catch (Exception ex){
                System.out.println("Sending problems occured.");
            }
        });
        bMoves.setOnAction(e ->{
            String msg = "Moves;";
            for(int i = 0; i< Map.getMove(); i++){
                msg = msg.concat(String.valueOf(mapa.getX(i)));
                msg = msg.concat(",");
                msg = msg.concat(String.valueOf(mapa.getY(i)));
                msg = msg.concat(";");
            }
            try {
                connection.send(msg);
                mapa.clearMoves();
                mapa.underlineColor(FALSE);
            }
            catch (Exception ex){
                System.out.println("Sending problems occured.");
            }
        });
        bAbort.setOnAction(e ->{
            mapa.clearMoves();
            mapa.setSent(FALSE);
        });
        bBackToHub.setOnAction(e ->{
            String message="Leave;";
            try{
                Stage stageTheLabelBelongs = (Stage) tChat.getScene().getWindow();
                //create new view and prepare connection
                View next = new HubView(connection);
                connection.setView(next);

                //crate javafx-friendly thread which will call the change
                stageTheLabelBelongs.setScene(next.getScene());
                //run the javafx-friendly thread
                next.parse(message);
                connection.send(message);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        });
        bSkip.setOnAction(e ->{
            mapa.clearMoves();
            mapa.setSent(FALSE);
            try{
                connection.send("Skip;");
                mapa.underlineColor(FALSE);
            }catch (Exception ignored){

            }
        });

        //Layout
        //instantiatig the GridPane class*/
        gridPaneHubLayout.setMinSize(400, 400);
        gridPaneHubLayout.setPadding(new Insets(10, 10, 10, 10));

        gridPaneHubLayout.getChildren().add(tChatShow);
        tChatShow.setLayoutX(700);
        tChatShow.setLayoutY(0);
        gridPaneHubLayout.getChildren().add(tChat);
        tChat.setLayoutX(700);
        tChat.setLayoutY(500);
        tChatShow.setMinHeight(500);
        tChatShow.setMaxWidth(200);
        tChat.setMinWidth(200);
        gridPaneHubLayout.getChildren().add(bMoves);
        bMoves.setLayoutX(700);
        bMoves.setLayoutY(530);
        gridPaneHubLayout.getChildren().add(bAbort);
        bAbort.setLayoutX(800);
        bAbort.setLayoutY(530);
        gridPaneHubLayout.getChildren().add(bBackToHub);
        bBackToHub.setLayoutX(800);
        bBackToHub.setLayoutY(560);
        gridPaneHubLayout.getChildren().add(bSkip);
        bSkip.setLayoutX(700);
        bSkip.setLayoutY(560);

        //Setting a scene obect;
        return new Scene(gridPaneHubLayout);
    }
}
