import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author C. N. Spencer
 *
 */

public class Client {
    //Game window
    @FXML protected Pane box;
    @FXML protected FlowPane center;
    @FXML protected FlowPane hand;
    @FXML protected Label pLeft;
    @FXML protected Label pTop;
    @FXML protected Label pRight;
    @FXML protected Label pMe;
    @FXML protected Label instructionText;
    @FXML protected Button startbt;

    private int padding = 15;

    private String server;
    private Player me;

    public Client() {
        this.server = "127.0.0.1";     //DEFAULT
        this.me = new Player("127.0.0.1", "myName");   //DEFAULT
    }

    @FXML protected void setPlayer(Player p) {
        this.me = p;
    }

    @FXML protected void setServerIP(String ip) {
        this.server = ip;
    }

    @FXML protected Scene connect() throws Exception {
        boolean connected = true;
        while (connected) {
            String submit = null;
            Socket socket = new Socket(this.server, 5545);
            BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = read.readLine().trim();

            if (line.startsWith("error")) {                         // report an error
                this.instructionText.setText("Error: " + line.replaceFirst("error", "") + ".\nDisconnecting from server.\nPlease restart game.");
                connected = false;
            } else if (line.startsWith("deal")) {                   // get dealt cards
                String[] cards = line.replaceFirst("deal", "").split(":");
                for (int i = 0; i < cards.length; i++, i++) {
                    this.me.dealCard(new Card(Numbers.valueOf(cards[i]), Suits.valueOf(cards[i++])), i);
                }
                for (Card i : this.me.getHand()) {
                    ImageView img = new ImageView(i.showFront());
                    this.hand.getChildren().add(img);
                    img.setOnMouseClicked(e -> {
                        if (img.getScaleX() == 1.0) {
                            img.setScaleX(1.2);
                            img.setScaleY(1.2);
                        } else {
                            img.setScaleX(1.0);
                            img.setScaleY(1.0);
                        }
                    });
                    //listener to toggle selection
                    img.scaleXProperty().addListener(ov -> {
                        if (img.scaleXProperty().getValue() > 1.0) {
                            this.me.getCard(Numbers.valueOf(img.getImage().getUrl().split("of")[0]), Suits.valueOf(img.getImage().getUrl().split("of")[1])).setSelected(true);
                        } else {
                            this.me.getCard(Numbers.valueOf(img.getImage().getUrl().split("of")[0]), Suits.valueOf(img.getImage().getUrl().split("of")[1])).setSelected(false);
                        }
                    });
                }
            } else if (line.startsWith("pass")) {                   // time to pass cards
                line = line.replaceFirst("pass", "");
                this.instructionText.setText("Choose three cards to pass to the player to the " + line + ".");
                int selected = 0;
                while (selected != 3) {     // wait until player has selected exactly 3 cards
                    for (Card i : this.me.getHand()) {
                        if (i.isSelected()) {
                            selected++;
                        }
                    }
                }
                submit = "";
                for (Card i : this.me.getHand()) {
                    if (i.isSelected()) {
                        submit = submit.concat("card" + i.getNumber() + ":" + i.getSuit());
                        this.me.removeCard(i);
                    }
                }
            } else if (line.startsWith("p")) {
                String[] nameLine = line.split("p");
                String[] names = new String[4];
                for (String i : nameLine) {
                    names[Integer.parseInt(i.split(":")[0])] = i.split(":")[1];
                }
                int playerNum = 0;
                for (int i = 0; i < names.length; i++) {
                    if (names[i].equals(this.me.getName())) {
                        playerNum = i;
                        break;
                    }
                }
                switch (playerNum) {
                    case (0):
                        this.pMe.setText(names[0]);
                        this.pLeft.setText(names[1]);
                        this.pTop.setText(names[2]);
                        this.pRight.setText(names[3]);
                        break;
                    case (1):
                        this.pMe.setText(names[1]);
                        this.pLeft.setText(names[2]);
                        this.pTop.setText(names[3]);
                        this.pRight.setText(names[0]);
                        break;
                    case (2):
                        this.pMe.setText(names[2]);
                        this.pLeft.setText(names[3]);
                        this.pTop.setText(names[0]);
                        this.pRight.setText(names[1]);
                        break;
                    case (3):
                        this.pMe.setText(names[3]);
                        this.pLeft.setText(names[0]);
                        this.pTop.setText(names[1]);
                        this.pRight.setText(names[2]);
                        break;
                }
            } else if (line.startsWith("hand")) {                   // clear center and add score if it's my hand
                line = line.replaceFirst("handp", "");
                this.instructionText.setText("The hand goes to " + line + " and is now " + line + "'s turn");
                wait(2);
                this.instructionText.setText("It is " + line + "'s turn.");
                if (line.equalsIgnoreCase(this.me.getName())) {
                    this.me.addScore(Integer.parseInt(line.replaceFirst(this.me.getName(), "")));
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

                    //Show results window & disconnect
                    return new Scene(this.getResults(name1, score1, name2, score2, name3, score3, name4, score4), 400, 400);
                } else if (line.startsWith("card")) {               // update center or add passed cards to hand
                    line = line.replaceFirst("card", "");
                    String[] cardLine = line.split(":");
                    if (line.contains("card")) {
                        int n = 1;
                        while (cardLine[n].contains("card") && cardLine[n] != null) {
                            cardLine = cardLine[n].split(":");
                            n++;
                        }
                        this.instructionText.setText("It is " + cardLine[n - 1].split("p")[1] + "'s turn");
                        cardLine[n - 1] = cardLine[n - 1].split("p")[0];
                        for (int i = 0; i < cardLine.length; i++, i++) {
                            Card card = new Card(Numbers.valueOf(cardLine[i]), Suits.valueOf(cardLine[i++]));
                            this.center.getChildren().add(new ImageView(card.showFront()));

                        }
                    } else {
                        Card card = new Card(Numbers.valueOf(cardLine[0]), Suits.valueOf(cardLine[1]));
                        this.center.getChildren().add(new ImageView(card.showFront()));
                    }
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
        return null;
    }

    //Action methods for GameWindow
    @FXML protected void ready() {
        try {
            Socket serv = new Socket(this.server, 5545);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(serv.getOutputStream()));
            writer.write("start");
            writer.flush();
            serv.close();
        } catch (java.io.IOException ex) {
            System.out.println("Caught \"" + ex.toString() + "\" sending start signal to server at \"" + this.server + "\"");
        }
    }
    @FXML protected void submitTurn() {
        System.out.println("Turn processing...");
        String msg = null;
        // find the selected card
        Card[] select = new Card[3];
        for (Card i : this.me.getHand()) {
            if (i.isSelected()) {
                for (int j = 0; j < select.length; j++) {
                    if (select[j] == null) {
                        select[j] = i;
                    }
                }
            }
        }
        if (select[0] == null) { // no card chosen
            this.instructionText.setText("Please choose a legal card.");
        } else {
            msg = "";
            // check for legality of card selected
            if (!select[0].getSuit().equals((Suits.valueOf(((ImageView)center.getChildren().get(0)).getImage().getUrl().replaceFirst(".png", "").split("of")[1])))) {
                // card must have same suit as first card
                for (Card i : this.me.getHand()) {
                    if (i.getSuit().equals((Suits.valueOf(((ImageView)center.getChildren().get(0)).getImage().getUrl().replaceFirst(".png", "").split("of")[1])))) {
                        // unless they have none of those cards
                        this.instructionText.setText("Please choose a legal card.");
                        msg = null;
                        break;
                    }
                }
            }
            for (Card i : this.me.getHand()) { // must play two of clubs if in hand
                if (i.getNumber() == Numbers.TWO) {
                    if (i.getSuit() == Suits.CLUBS) {
                        if (!i.isSelected()) {
                            this.instructionText.setText("You must play the Two of Clubs to start the game.");
                            msg = null;
                        }
                    }
                }
            }
            if (msg != null) {
                for (Card i : select) {
                    if (i != null) {
                        this.hand.getChildren().remove(this.me.getIndex(i));
                        this.me.removeCard(i);
                        msg = msg.concat("card" + i.getNumber().toString() + ":" + i.getSuit());
                    }
                }
            }
        }
        if (msg != null) {
            try {
                Socket serv = new Socket(this.server, 5545);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(serv.getOutputStream()));
                writer.write(msg);
                writer.flush();
                serv.close();
            } catch (java.io.IOException ex) {
                System.out.println("Caught \"" + ex.toString() + "\" sending \"" + msg + "\" to server at \"" + this.server + "\"");
            }
        }
    }

    @FXML protected Scene getGameLoader() {
        Scene sc = null;
        try {
            sc = new Scene(FXMLLoader.load(getClass().getResource("GameWindow.fxml")));
        } catch (Exception ex) {
            System.out.println("Caught \"" + ex.toString() + "\" in getGameLoader");
        }
        return sc;
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

}
