package Client.View;

import javafx.scene.Scene;

public interface View {
    void parse(String message);

    Scene getScene();

}
