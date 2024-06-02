/*
Guy Bernstein
   i.d: 206558439
    Main application class that loads and displays the ConnectFour's GUI.
*/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ConnectFour extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        Parent root = (Parent) FXMLLoader.load(getClass().getResource("ConnectFour.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("ConnectFour");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
