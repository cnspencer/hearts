import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * @author C. N. Spencer
 *
 */

public class Hearts extends Application {
    private Round round;
    private int numRounds = 4;
    private Player[] players = new Player[4];

    public void start(Stage st) {

        Pane playerPn = this.getStartLoader();

        Scene sc = new Scene(playerPn);

        Pane gamePn = this.getGameLoader();
        Scene mainSc = new Scene(gamePn);

         playerPn.lookup("buttonPane").lookup("ipSubmit").setOnMouseClicked(e -> {
             this.players[0] = new Player(((TextField)(playerPn.lookup("P1IP"))).getText());
             this.players[1] = new Player(((TextField)(playerPn.lookup("P2IP"))).getText());
             this.players[2] = new Player(((TextField)(playerPn.lookup("P3IP"))).getText());
             this.players[3] = new Player(((TextField)(playerPn.lookup("P4IP"))).getText());
             st.setScene(mainSc);
        });

        st.setScene(sc);
//        ((Label)gameLoader.lookup("InstructionText")).setText("");

        //set scene with game pane

        for (int i = 0; i < numRounds; i++) {
            round = new Round(i, this.players);
        }

        st.show();
    }

    private Pane getStartLoader() {
        try {
            return FXMLLoader.load(getClass().getResource("PlayerMenu.fxml"));
        } catch (IOException ex) {
            System.out.print("Caught " + ex.toString() + "in getStartLoader()");
        } finally {
            return new Pane();
        }
    }

    private Pane getGameLoader() {
        try {
            return FXMLLoader.load(getClass().getResource("GameWindow.fxml"));
        } catch (IOException ex) {
            System.out.print("Caught " + ex.toString() + "in getGameLoader");
        }
        return new Pane();
    }

    @FXML private void setIPs() {
    }

    @FXML private void submitTurn() {
    }

    public static void main(String[] args) {
        launch(args);
    }
}
