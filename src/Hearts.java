import javafx.animation.RotateTransition;
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
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.event.MouseEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


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
    @FXML protected Pane cards;
    @FXML protected Label instructionText;

    private String server;
    protected Player me;
    private Stage st;
//    private int numRounds = 4;
    private Suits firstSuit;

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

    private Pane getResults(String name1, int score1, String name2, int score2, String name3, int score3, String name4, int score4) {
        VBox win = new VBox();

        //Add some cool effects for show
        Pane heart = new Pane();
        PhongMaterial red = new PhongMaterial(Color.CRIMSON);
        Sphere ball1 = new Sphere(25);
        ball1.setTranslateX((win.getPrefWidth() / 2) - 25);
        ball1.setTranslateY(25 + 20);
        ball1.setCullFace(CullFace.FRONT);
        ball1.setMaterial(red);
        Sphere ball2 = new Sphere(25);
        ball2.setTranslateX((win.getPrefWidth() / 2) + 25);
        ball2.setTranslateY(25 + 20);
        ball2.setCullFace(CullFace.FRONT);
        ball2.setMaterial(red);
        TriangleMesh pyramidMesh = new TriangleMesh();
        pyramidMesh.getPoints().addAll(0, 0, 0, 0, 50, -50, -50, 50, 0, 50, 50, 0, 0, 50, 50);
        pyramidMesh.getFaces().addAll(0, 0, 2, 0, 1, 0, 0, 0, 1, 0, 3, 0, 0, 0, 3, 0, 4, 0, 0, 0, 4, 0, 2, 0, 4, 0, 1, 0, 2, 0, 4, 0, 3, 0, 1, 0);
        MeshView pyramid = new MeshView(pyramidMesh);
        pyramid.setDrawMode(DrawMode.FILL);
        pyramid.setMaterial(red);
        pyramid.setTranslateX(win.getPrefWidth() / 2);
        pyramid.setTranslateY(45);
        pyramid.setRotate(180);
        heart.getChildren().add(ball1);
        heart.getChildren().add(ball2);
        heart.getChildren().add(pyramid);
        RotateTransition ballrot1 = new RotateTransition();
        ballrot1.setNode(ball1);
        ballrot1.setDuration(Duration.seconds(6));
        ballrot1.setAxis(Rotate.Y_AXIS);
        ballrot1.play();
        RotateTransition ballrot2 = new RotateTransition();
        ballrot2.setNode(ball2);
        ballrot2.setDuration(Duration.seconds(6));
        ballrot2.setAxis(Rotate.Y_AXIS);
        ballrot2.play();
        RotateTransition pyrrot = new RotateTransition();
        pyrrot.setNode(pyramid);
        pyrrot.setDuration(Duration.seconds(6));
        pyrrot.setAxis(Rotate.Y_AXIS);
        pyrrot.play();
        win.getChildren().add(heart);

        //Add player names and scores
        win.getChildren().add(new Label("Player " + name1 + " : " + score1));
        win.getChildren().add(new Label("Player " + name2 + " : " + score2));
        win.getChildren().add(new Label("Player " + name3 + " : " + score3));
        win.getChildren().add(new Label("Player " + name4 + " : " + score4));

        return win;
    }

    //Game logic
    private void connect() throws Exception {
        Stage st2 = new Stage();
        st2.setScene(new Scene(this.getGameLoader()));
        st2.setTitle("Hearts Game");
        this.st = st2;
        this.st.show();
        boolean connected = true;
        while (connected) {
            String submit = null;
            Socket socket = new Socket(this.server, 5545);
            BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = read.readLine().trim();

            if (line.startsWith("error")) {
                this.instructionText.setText("Error: " + line.replaceFirst("error", "") + ".\nDisconnecting from server.\nPlease restart game.");
                connected = false;
            } else if (line.startsWith("pass" + this.pIP)) {
                line = line.replaceFirst("pass" + this.pIP, "");
                this.instructionText.setText("Choose three cards to pass to the player to the " + line + ".");
                int selected = 0;
                while (selected != 3) {
                    for (Card i : this.me.getHand()) {
                        if (i.isSelected()) {
                            selected++;
                        }
                    }
                }
                submit = "";
                for (Card i : this.me.getHand()) {
                    submit = submit.concat("card" + i.getNumber() + ":" + i.getSuit());
                }
            } else if (line.startsWith("hand")) {
                line = line.replaceFirst("handp", "");
                this.instructionText.setText("The hand goes to " + line);
                wait(2);
                this.instructionText.setText("It is " + line + "'s turn.");
                if (line.equalsIgnoreCase(this.name.getText())) {
                    this.me.addScore(Integer.parseInt(line.replaceFirst(this.name.getText(), "")));
                } else if (line.startsWith("results")) {
                    String[] p1 = read.readLine().trim().replaceFirst("resultsp", "").split(":");
                    String[] p2 = read.readLine().trim().replaceFirst("resultsp", "").split(":");
                    String[] p3 = read.readLine().trim().replaceFirst("resultsp", "").split(":");
                    String[] p4 = read.readLine().trim().replaceFirst("resultsp", "").split(":");
                    String name1 = p1[0];
                    int score1 = Integer.parseInt(p1[1]);
                    String name2 = p2[0];
                    int score2 = Integer.parseInt(p2[1]);
                    String name3 = p3[0];
                    int score3 = Integer.parseInt(p3[1]);
                    String name4 = p4[0];
                    int score4 = Integer.parseInt(p4[1]);

                    //Show results window
                    Stage st3 = new Stage();
                    st3.setScene(new Scene(this.getResults(name1, score1, name2, score2, name3, score3, name4, score4), 400, 400));
                    st3.setTitle("Results");
                    this.st = st3;
                    this.st.show();
                    connected = false;
                }

                // send the action to the server
                if (submit != null) {
                    ServerSocket serv = new ServerSocket(5545);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(submit);
                    writer.flush();
                    serv.close();
                }
                socket.close();
            }
        }
    }


    //Action methods for PlayerMenu
    @FXML private void setIPs() {
        if (this.pIP.getText().isEmpty()) {
            this.pIP.setPromptText("Need an IP!");
        } if (this.svIP.getText().isEmpty()) {
            this.svIP.setPromptText("Need an IP!");
        } else {
            this.me = new Player(this.pIP.getText(), this.name.getText());
            this.server = this.svIP.getText();
            this.ipSubmit.setOnMouseClicked(e -> {
                try {
                    this.connect();
                } catch (Exception ex) {
                    System.out.println("Caught " + ex.toString() + " connecting to server at " + this.svIP.getText() + ".");
                }
            });
        }
    }

    //Action methods for GameWindow
    @FXML private void submitTurn() {
        System.out.println("Turn processing...");
        // find the selected card
        Card select = null;
        for (Card i: this.me.getHand()) {
            if (i.isSelected()) {
                select = i;
                break;
            }
        }
        if (select == null) { // no card chosen
            this.instructionText.setText("Please choose a legal card.");
        } else {
            // check for legality of card selected
            if (!select.getSuit().equals(firstSuit)) { // card must have same suit as first card
                for (Card i : this.me.getHand()) {
                    if (i.getSuit().equals(firstSuit)) { // unless they have none of those cards
                        this.instructionText.setText("Please choose a legal card.");
                        break;
                    }
                }
            }
            for (Card i : this.me.getHand()) { // must play two of clubs if in hand
                if (i.getNumber() == Numbers.TWO) {
                    if (i.getSuit() == Suits.CLUBS) {
                        if (!i.isSelected()) {
                            this.instructionText.setText("You must play the Two of Clubs to start the game.");
                        }
                    }
                }
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}