////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// File:          Main.java
// Description:   Launches Connect Four application
// Author:        Thomas Wang
// Last modified: 01 Jun 2020
// Changelist:
//   01 Jun 2020: Removed default focus from TextField
//   31 May 2020: Autocreated by IntelliJ, set window name
// References:
//   https://stackoverflow.com/a/38374747 - remove default focus from TextField JavaFX

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;

/// Main // Superclass: Application ////////////////////////////////////////////
/// Description: Launches JavaFX application
public class Main extends Application {

    /// start //////////////////////////////////////////////////////////////////////
    /// Description: Launches JavaFX application
    /// Inputs: Stage stage - Stage to launch to
    /// Output: none
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));
        Platform.runLater(root::requestFocus);
        stage.setTitle("Connect Four!");
        stage.setScene(new Scene(root, 850, 750, false, SceneAntialiasing.BALANCED));
        stage.show();
    }

    /// main ///////////////////////////////////////////////////////////////////////
    /// Description: Launches JavaFX application
    /// Inputs: String[] args - arguments, not used
    /// Output: none
    public static void main(String[] args) {
        launch(args);
    }
}
