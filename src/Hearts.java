import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * @author C. N. Spencer
 *
 */

public class Hearts extends Application {
    @FXML protected VBox buttonPane;
    @FXML protected Button ipSubmit;
    @FXML protected TextField p1IP;
    @FXML protected TextField p2IP;
    @FXML protected TextField p3IP;
    @FXML protected TextField p4IP;
    @FXML protected CheckBox p1Bot;
    @FXML protected CheckBox p2Bot;
    @FXML protected CheckBox p3Bot;
    @FXML protected CheckBox p4Bot;
    private Stage st;
    private Scene mainSc;
    private Round round;
    private int numRounds = 4;
    private Player[] players = {new Player(""), new Player(""), new Player(""), new Player("")};

    public void start(Stage st) {
        this.st = st;
        this.st.setScene(new Scene(this.getStartLoader()));
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

    private Pane getGameLoader() {
        try {
            return FXMLLoader.load(getClass().getResource("GameWindow.fxml"));
        } catch (IOException ex) {
            System.out.println("Caught " + ex.toString() + "in getGameLoader");
        }
        return new Pane();
    }

    private Pane getResults() {
        VBox win = new VBox();

        //Add 3D heart
        Pane heart = new Pane();
        heart.getChildren().add(new Ellipse(121, 70, 30, 50));
        heart.getChildren().add(new Ellipse(239, 70, 30, 50));
        heart.getChildren().add(new Line(252, 150, 121, 103));
        heart.getChildren().add(new Line(252, 150, 209, 103));
        win.getChildren().add(heart);

        //Add player names and scores
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i++] != null) {
                if (this.players[i].getScore() < this.players[i++].getScore()) {
                    Player temp = this.players[i];
                    this.players[i] = this.players[i++];
                    this.players[i++] = temp;
                }
            }
        }
        for (int i = 0; i < this.players.length; i++) {
            win.getChildren().add(new Label("Player " + i + " : " + this.players[i].getScore()));
        }
        return win;
    }

    private void initiateGame() {
        for (int i = 0; i < this.numRounds; i++) {
            this.round = new Round(i, this.players);
        }
        Stage st3 = new Stage();
        st3.setScene(new Scene(this.getResults(), 400, 400));
        st3.setTitle("Results");
        st3.show();
    }


    //Action methods for PlayerMenu
    @FXML private void setIPs() {
        this.ipSubmit.setOnMouseClicked(e -> {
            this.players[0] = new Player(this.p1IP.getText());
            this.players[1] = new Player(this.p2IP.getText());
            this.players[2] = new Player(this.p3IP.getText());
            this.players[3] = new Player(this.p4IP.getText());
            this.st.setScene(this.mainSc);
        });
        Stage st2 = new Stage();
        st2.setScene(new Scene(this.getGameLoader()));
        st2.setTitle("Hearts Game");
        st2.show();
        this.initiateGame();
    }

    public void p1Bind() {
        this.p1IP.editableProperty().isNotEqualTo(this.p1Bot.selectedProperty());
    }

    public void p2Bind() {
        this.p2IP.editableProperty().isNotEqualTo(this.p2Bot.selectedProperty());
    }

    public void p3Bind() {
        this.p3IP.editableProperty().isNotEqualTo(this.p3Bot.selectedProperty());
    }

    public void p4Bind() {
        this.p4IP.editableProperty().isNotEqualTo(this.p4Bot.selectedProperty());
    }


    //Action methods for GameWindow
    @FXML private void submitTurn() {
    }


    public static void main(String[] args) {
        launch(args);
    }
}
