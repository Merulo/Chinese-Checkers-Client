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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class InGameView implements View {

    private Connection connection;
    private Scene lobby;
    private Map mapa/* = new Map(5, connection)*/;


    Group group = new Group();
    Pane gridPaneHubLayout = new Pane(group);


    TextField tChat = new TextField();
    TextArea tChatShow = new TextArea();
    Button bMoves = new Button("Wyślij ruchy");

    public InGameView(Connection connection, int s, int counter, double [][]colors){
        this.connection = connection;
        for(int i=0; i<10; i++) {

        }
        int tmp = s*(s-1)/2;
        tChatShow.setEditable(FALSE);
        //tChatShow.setMouseTransparent(TRUE);
        tChatShow.setFocusTraversable(FALSE);
        tChatShow.textProperty().addListener(new ChangeListener<Object>(){
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue){
                tChatShow.setScrollTop(Double.MAX_VALUE);
            }
        });
        mapa = new Map(tmp, connection, gridPaneHubLayout, counter, colors);

    }

    public void parse(String message){
        System.out.println(message);
        String[] tmp = message.split(";");
        if(tmp[0].equals("Msg")){
            if(tChatShow.getText().equals("")) {
                tChatShow.setText(tmp[1] + "\n");
                tChatShow.selectPositionCaret(tChatShow.getLength());
                tChatShow.deselect();
            }
            else {
                tChatShow.setText(tChatShow.getText() + tmp[1] + "\n");
                tChatShow.selectPositionCaret(tChatShow.getLength());
                tChatShow.deselect();
            }
        }
        if(tmp[0].equals("Move")){
            String[] tmpA = tmp[1].split(",");
            String[] tmpB = tmp[2].split(",");
            mapa.makeMove(Integer.parseInt(tmpA[0]), Integer.parseInt(tmpA[1]), Integer.parseInt(tmpB[0]), Integer.parseInt(tmpB[1]));
        }
    }

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
        bMoves.setOnAction(e ->{
            String msg = "Moves;";
            for(int i=0; i<mapa.getMove(); i++){
                msg = msg.concat(String.valueOf(mapa.getX(i)));
                msg = msg.concat(",");
                msg = msg.concat(String.valueOf(mapa.getY(i)));
                msg = msg.concat(";");
            }
            try {
                connection.send(msg);
                mapa.clearMoves();
            }
            catch (Exception ex){

            }
        });

        Canvas canvas = new Canvas(800, 800);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        mapa.display(gc);


        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle (MouseEvent mouseEvent) {
                System.out.println("X: " + mouseEvent.getX() + " Y: " + mouseEvent.getY());
            }
        });

        //Layout
        //instantiatig the GridPane class*/
        //GridPane gridPaneHubLayout = new GridPane();
        gridPaneHubLayout.setMinSize(400, 400);
        gridPaneHubLayout.setPadding(new Insets(10, 10, 10, 10));

        //gridPaneHubLayout.setVgap(10);
        //gridPaneHubLayout.setHgap(10);

        //gridPaneHubLayout.setAlignment(Pos.TOP_LEFT);

        gridPaneHubLayout.getChildren().add(tChatShow);
        tChatShow.setLayoutX(1000);
        tChatShow.setLayoutY(0);
        gridPaneHubLayout.getChildren().add(tChat);
        tChat.setLayoutX(1000);
        tChat.setLayoutY(500);
        tChatShow.setMinHeight(500);
        tChatShow.setMinWidth(200);
        tChat.setMinWidth(200);
        gridPaneHubLayout.getChildren().add(bMoves);
        bMoves.setLayoutX(1000);
        bMoves.setLayoutY(530);
        //gridPaneHubLayout.getChildren().add(canvas);


        //Setting a scene obect;
        lobby=new Scene(gridPaneHubLayout);
        return lobby;
    }
}
