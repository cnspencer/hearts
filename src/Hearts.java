import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
        try {
            Parent startLoader = FXMLLoader.load(getClass().getResource("PlayerMenu.fxml"));
        } catch (IOException ex) {
            System.out.print("Caught " + ex.toString() + "in startLoader");
        }

        try {
            Parent gameLoader = FXMLLoader.load(getClass().getResource("GameWindow.fxml"));
        } catch (IOException ex) {
            System.out.print("Caught " + ex.toString() + "in gameLoader");
        }

        for (int i = 0; i < numRounds; i++) {
            round = new Round(i, players);
        }
//        gameLoader.InstructionText = "";
        st.show();
    }

    @FXML private void submitTurn() {
        
    }

    public static void main(String[] args) {

    }
}
