import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * @author C. N. Spencer
 *
 */

public class Round {
    private Deck deck;
    private int round = 0;
    private Player[] players;

    Round(int round, Player[] players) {
        this.round = round;
        this.players = players;
        this.deck = new Deck();
        deck.shuffle();
        this.dealCards();
    }

    private void dealCards() {
        for (int i = 0; i < this.deck.getCards(); i++) {
            if (i >= 13) {
                break;
            }
            this.players[Math.floorMod(i, this.players.length)].addCard(this.deck.dealCard(), i);
        }
    }

    protected void displayPX(Pane pn, int player) {
        for (int i = 0; i < this.players[player].getHand().length; i++) {
            ImageView img = new ImageView(this.players[player].getCard(i).showFront());
            pn.getChildren().add(img);
//            img.setX();
//            img.setY();
        }
    }

    protected void tradeCards(int round) {

        switch (round) {
            case 1:
                //player left
            case 2:
                //player up
            case 3:
                //player right
            case 4:
                //no switching
        }
    }
}