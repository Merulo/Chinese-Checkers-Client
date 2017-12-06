package Client;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chinese Checkers");
        Group root = new Group();
        Canvas canvas = new Canvas(1024, 768);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Map map = new Map(10);
        map.display(gc);

        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }

    public static String addStrings(String a, String b){
        return a + b;
    }

}
