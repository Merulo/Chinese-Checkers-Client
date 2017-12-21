package Client.View;

import Client.Map.Map;
import Client.Network.Connection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class InGameView implements View {

    private Connection connection;
    private Scene lobby;
    private Map mapa = new Map(5);


    GridPane gridPaneHubLayout = new GridPane();


    TextField tChat = new TextField();
    TextArea tChatShow = new TextArea();

    public InGameView(Connection connection){
        this.connection = connection;
        for(int i=0; i<10; i++) {

        }
        tChatShow.setEditable(FALSE);
        tChatShow.setMouseTransparent(TRUE);
        tChatShow.setFocusTraversable(FALSE);
    }

    public void parse(String message){
        String[] tmp = message.split(";");
        if(tmp[0].equals("Msg")){
            if(tChatShow.getText().equals(""))
                tChatShow.setText(tmp[1]+"\n");
            else
                tChatShow.setText(tChatShow.getText()+tmp[1]+"\n");
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

        Canvas canvas = new Canvas(540, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        mapa.display(gc);


        //Layout
        //instantiatig the GridPane class*/
        //GridPane gridPaneHubLayout = new GridPane();
        gridPaneHubLayout.setMinSize(400, 400);
        gridPaneHubLayout.setPadding(new Insets(10, 10, 10, 10));

        gridPaneHubLayout.setVgap(10);
        //gridPaneHubLayout.setHgap(10);

        gridPaneHubLayout.setAlignment(Pos.TOP_LEFT);

        gridPaneHubLayout.add(tChatShow, 1,0);
        gridPaneHubLayout.add(tChat, 1,1);
        gridPaneHubLayout.add(canvas, 0, 0);


        //Setting a scene obect;
        lobby=new Scene(gridPaneHubLayout);
        return lobby;
    }
}
