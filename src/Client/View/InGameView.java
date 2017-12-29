package Client.View;

import Client.Map.Map;
import Client.Network.Connection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class InGameView implements View {

    private Connection connection;
    private Scene lobby;
    private Map mapa/* = new Map(5, connection)*/;


    private Group group = new Group();
    private Pane gridPaneHubLayout = new Pane(group);


    private TextField tChat = new TextField();
    private TextArea tChatShow = new TextArea();
    private Button bMoves = new Button("Wyślij ruchy");
    private Button bSkip = new Button("Anuluj ruch");
    private Button bBackToHub = new Button("Wróc do Hub");

    InGameView(Connection connection, int s, int counter, double[][] colors, double cR, double cG, double cB){
        this.connection = connection;
        /*for(int i=0; i<10; i++) {

        }*/
        int tmp = s*(s-1)/2;
        tChatShow.setEditable(FALSE);
        //tChatShow.setMouseTransparent(TRUE);
        tChatShow.setFocusTraversable(FALSE);
        tChatShow.textProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> tChatShow.setScrollTop(Double.MAX_VALUE));
        mapa = new Map(tmp, connection, gridPaneHubLayout, counter, colors, cR, cG, cB);

    }

    public void parse(String message){
        System.out.println(message);
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
                Map.setSent(FALSE);
            /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Twój ruch");
            alert.setHeaderText(null);
            alert.setContentText("Wykonaj ruch");

            alert.show();
            try{
                wait(1000);
            }catch(Exception ex){

            }
            alert.close();*/
                mapa.underlineColor(TRUE);
                break;
            case "IncorrectMove":
                Map.setSent(FALSE);
            /*final Popup popup = new Popup();
            popup.setX(300);
            popup.setY(200);
            popup.getContent().addAll(new Label("Błędny ruch. Powtórz"));
            popup.show(gridPaneHubLayout.getScene().getWindow());*/
                mapa.underlineColor(TRUE);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Błędny rcuh!");
                alert.setHeaderText(null);
                alert.setContentText("Popraw ruch.");

                alert.showAndWait();
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
                msg = msg.concat(String.valueOf(Map.getX(i)));
                msg = msg.concat(",");
                msg = msg.concat(String.valueOf(Map.getY(i)));
                msg = msg.concat(";");
            }
            try {
                System.out.println("*******************************");
                System.out.println(msg);
                connection.send(msg);
                Map.clearMoves();
                mapa.underlineColor(FALSE);
            }
            catch (Exception ex){
                System.out.println("Sending problems occured.");
            }
        });
        bSkip.setOnAction(e ->{
            Map.clearMoves();
            Map.setSent(FALSE);
        });
        bBackToHub.setOnAction(e ->{
            String message="Leave;";
            try{
                //connection.send(message);
                Stage stageTheLabelBelongs = (Stage) tChat.getScene().getWindow();
                //create new view and prepare connection
                View next = new HubView(connection);
                connection.setView(next);

                //crate javafx-friendly thread which will call the change
                stageTheLabelBelongs.setScene(next.getScene());
                //run the javafx-friendly thread
                //task.run();
                next.parse(message);
                connection.send(message);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        });

        Canvas canvas = new Canvas(800, 800);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        mapa.display(gc);


        canvas.setOnMousePressed(mouseEvent -> System.out.println("X: " + mouseEvent.getX() + " Y: " + mouseEvent.getY()));

        //Layout
        //instantiatig the GridPane class*/
        //GridPane gridPaneHubLayout = new GridPane();
        gridPaneHubLayout.setMinSize(400, 400);
        gridPaneHubLayout.setPadding(new Insets(10, 10, 10, 10));

        //gridPaneHubLayout.setVgap(10);
        //gridPaneHubLayout.setHgap(10);

        //gridPaneHubLayout.setAlignment(Pos.TOP_LEFT);

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
        gridPaneHubLayout.getChildren().add(bSkip);
        bSkip.setLayoutX(800);
        bSkip.setLayoutY(530);
        gridPaneHubLayout.getChildren().add(bBackToHub);
        bBackToHub.setLayoutX(700);
        bBackToHub.setLayoutX(580);
        //gridPaneHubLayout.getChildren().add(canvas);


        //Setting a scene obect;
        lobby=new Scene(gridPaneHubLayout);
        return lobby;
    }
}
