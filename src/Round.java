/**
 * @author C. N. Spencer
 *
 */

public class Round {
    private Deck deck = new Deck();
    private int round = 0;
    private Player[] players;

    Round(int round, Player[] players) {
        this.round = round;
        this.players = players;
        deck.shuffle();
        for (int i = 0; i < deck.getCards(); i++) {
            System.out.println(this.players.length);
            System.out.println(i);
            this.players[Math.floorMod(i, this.players.length)].addCard(this.deck.dealCard(), i);
        }
        tradeCards(round);
    }

    private void tradeCards(int round) {

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
