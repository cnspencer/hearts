import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.*;


/**
 * @author C. N. Spencer
 *
 */

public class Hearts extends Application {
    //Player menu
    @FXML protected TextField svIP;
    @FXML protected Button ipSubmit;
    @FXML protected TextField name;
    @FXML protected TextField pIP;
    //Game window
    private Client connector;

    private String server;
    private Player me;
    private Stage st;

    public void start(Stage st) {
        connector = new Client(this.server, this.me);
        this.st = st;
        Scene playerSc = new Scene(this.getStartLoader());
        playerSc.setFill(Color.BLANCHEDALMOND);
        this.st.setScene(playerSc);
        this.st.setTitle("Choose Players");
        this.st.getIcons().add(new Image("GameData/icon.svg"));
        this.st.show();
    }


    //Loaders
    private VBox getStartLoader() {
        try {
            return (VBox)FXMLLoader.load(getClass().getResource("PlayerMenu.fxml"));
        } catch (IOException ex) {
            System.out.println("Caught " + ex.toString() + "in getStartLoader()");
        }
        return new VBox();
    }


    //Action methods for PlayerMenu
    @FXML private void setIPs() {
        if (this.pIP.getText().isEmpty()) {
            this.pIP.setPromptText("Need an IP!");
        } if (this.svIP.getText().isEmpty()) {
            this.svIP.setPromptText("Need an IP!");
        } else {
            String playerIP = this.pIP.getText().trim();
            String playerName = this.name.getText().trim();
            this.me = new Player(playerIP, playerName);
            this.server = this.svIP.getText().trim();
            try {
                this.st.setScene(connector.getGameLoader());
                this.st.setTitle("Hearts Game");
                this.st.show();
                this.st.setScene(connector.connect());
                this.st.setTitle("Results");
                this.st.show();
            } catch (Exception ex) {
                System.out.println("Caught " + ex.toString() + " connecting to server at " + this.server);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}