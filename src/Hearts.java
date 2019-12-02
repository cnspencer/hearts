import javafx.application.Application;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * @author C. N. Spencer
 *
 */

public class Hearts extends Application {
    //Player menu
    @FXML protected TextField svIP;
    @FXML protected Button ipSubmit;
    @FXML protected TextField p1IP;
    @FXML protected TextField p2IP;
    @FXML protected TextField p3IP;
    @FXML protected TextField p4IP;
    @FXML protected CheckBox p2Bot;
    @FXML protected CheckBox p3Bot;
    @FXML protected CheckBox p4Bot;
    //Game window
    @FXML protected Pane main;
    @FXML protected Label instructionText;

    private String server;
    private Stage st;
    private int numRounds = 4;
    private String clientIP;

    public void start(Stage st) {
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

        //Add heart
//        Pane heart = new Pane();
//        heart.getChildren().add(new Ellipse(121, 70, 30, 50));
//        heart.getChildren().add(new Ellipse(239, 70, 30, 50));
//        heart.getChildren().add(new Line(252, 150, 171, 103));
//        heart.getChildren().add(new Line(252, 150, 209, 103));
//        win.getChildren().add(heart);
//
//        //Add player names and scores
//        for (int i = 0; i < this.players.length; i++) {
//            if (i+1 < this.players.length) {
//                if (this.players[i].getScore() < this.players[i + 1].getScore()) {
//                    Player temp = this.players[i];
//                    this.players[i] = this.players[i + 1];
//                    this.players[i + 1] = temp;
//                }
//            }
//        }
//        for (int i = 0; i < this.players.length; i++) {
//            win.getChildren().add(new Label("Player " + this.players[i].getIP() + " : " + this.players[i].getScore()));
//        }
        return win;
    }

    //Game logic
    private void initiateGame() {
        Stage st2 = new Stage();
        st2.setScene(new Scene(this.getGameLoader()));
        st2.setTitle("Hearts Game");
        this.st = st2;
        this.st.show();
        for (int i = 0; i < this.numRounds; i++) {
            Round round = new Round(i);
            round.displayPX(this.main, 1);
            round.tradeCards(i);
        }

        //Show results window
        Stage st3 = new Stage();
        st3.setScene(new Scene(this.getResults(), 400, 400));
        st3.setTitle("Results");
        this.st = st3;
        this.st.show();
    }


    //Action methods for PlayerMenu
    @FXML private void setIPs() {
        if (this.p1IP.getText().isEmpty()) {
            this.p1IP.setPromptText("Need an IP!");
        } if (this.svIP.getText().isEmpty()) {
            this.svIP.setPromptText("Need an IP!");
        } else {
//            this.players[0] = new Player(this.p1IP.getText());
            this.server = this.svIP.getText();
            this.ipSubmit.setOnMouseClicked(e -> {
//                this.players[1] = new Player(this.p2IP.getText());
//                this.players[2] = new Player(this.p3IP.getText());
//                this.players[3] = new Player(this.p4IP.getText());
                this.initiateGame();
            });
        }
    }
//    public void p2Bind() {
//        this.p2IP.setEditable(!this.p2Bot.isSelected());
//        this.p2IP.setText("bot");
//        this.p2IP.editableProperty().isNotEqualTo(this.p2Bot.selectedProperty());
//    }
//    public void p3Bind() {
//        this.p3IP.setEditable(!this.p2Bot.isSelected());
//        this.p3IP.setText("bot");
//        this.p3IP.editableProperty().isNotEqualTo(this.p3Bot.selectedProperty());
//    }
//    public void p4Bind() {
//        this.p4IP.setEditable(!this.p2Bot.isSelected());
//        this.p4IP.setText("bot");
//        this.p4IP.editableProperty().isNotEqualTo(this.p4Bot.selectedProperty());
//    }


    //Action methods for GameWindow
    @FXML private void submitTurn() {
    }


    public static void main(String[] args) {
        launch(args);
    }
}