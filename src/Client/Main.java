package Client;

import Client.Network.Connection;
import Client.View.HubView;
import Client.View.View;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage window;
    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;

        Connection connection = new Connection("localhost", 5555);
        View curret = new HubView(connection);

        if(!connection.start()){
            System.out.println("CANT ESTABLISH CONNECTION!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Błąd połączenia");
            alert.setHeaderText(null);
            alert.setContentText("Nie można ustanowić połączenia. Spóbuj ponownie później.");
            alert.showAndWait();
            System.exit(0);
        }
        else {
            connection.setView(curret);

            window.setTitle("Chinese Checkers");
            window.setScene(curret.getScene());
            window.show();

            window.setOnCloseRequest(we -> {
                connection.send("Leave");
                System.out.println("Stage is closing");
                System.exit(0);

            });
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}