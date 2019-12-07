import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HeartsServer {
    private static void runServer() throws Exception {
        String[] ips = new String[4];
        String[] names = new String[4];
        ArrayList<Player> bots = new ArrayList<>();
        String[] currentCards = new String[4];
        boolean[] empty = new boolean[4];
        boolean[] rounds = new boolean[4];
        int[] scores = new int[4];
        ServerSocket servSock = new ServerSocket(5545);

        boolean connect = true;
        System.out.println("Server running on port " + servSock.getLocalPort());
        while (connect) {
            String reply = null;
            Socket socket = servSock.accept();
            BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = read.readLine().trim();

            if (line.startsWith("ip")) {                // set ips of players
                for (int i = 0; i < ips.length; i++) {
                    if (ips[i] == null) {
                        ips[i] = line.replaceFirst("ip", "");
                        break;
                    } else if (i == ips.length - 1) {
                        reply = "erroripfull";
                    }
                }
            } else if (line.startsWith("name")) {       // set ips of players
                for (int i = 0; i < names.length; i++) {
                    if (names[i] == null) {
                        names[i] = line.replaceFirst("name", "");
                        break;
                    } else if (i == names.length - 1) {
                        reply = "errornamefull";
                    }
                }
            } else if (line.startsWith("start")) {      // initiate the game: assign bots, send out names, deal cards
                int botNum = 1;
                for (int i = 0; i < ips.length; i++) {
                    if (ips[i] == null) {
                        bots.add(new Player("bot" + botNum, "bot" + botNum));
                        ips[i] = "bot" + botNum;
                        names[i] = "bot" + botNum;
                        botNum++;
                    }
                }
                reply = "";
                for (int i = 0; i < names.length; i++) {        // send the names to everyone
                    reply = reply.concat("p" + i + ":" + names[i]);
                }
                Card[][] deals = dealCards();
                for (int i = 0; i < ips.length; i++) {
                    String msg = "deal";
                    for (Card j : deals[i]) {
                        msg = msg.concat(j.getNumber().toString() + ":" + j.getSuit().toString() + ":");
                    }
                    if (!ips[i].startsWith("bot")) {
                        sendTo(ips[i], msg);
                    } else {
                        int bot = 1;
                        // TODO: deal card to bots
                    }
                }
            } else if (line.startsWith("card")) {       // echo message to update each client GUI
                line = line.replaceFirst("card", "");
                if (line.contains("card")) {            // handle card passing
                    // TODO: figure out to whom these passed cards go
                } else if (currentCards[currentCards.length - 1] != null) { // if all cards are out, give a score to someone
                    int n = 0;
                    for (int i = 0; i < ips.length; i++) {
                        if (Suits.valueOf(currentCards[i].split(":")[1]) == Suits.valueOf(currentCards[0].split(":")[1])) {
                            if (Numbers.valueOf(currentCards[i].split(":")[0]).ordinal() > Numbers.valueOf(currentCards[n].split(":")[0]).ordinal()) {
                                n = i;
                            } else if (Numbers.valueOf(currentCards[i].split(":")[0]) == Numbers.ACE) {
                                n = i;
                            }
                        }
                    }
                    int score = 0;
                    for (String i : currentCards) {
                        if (Suits.valueOf(i.split(":")[1]) == Suits.HEARTS) {
                            score++;
                        } else if (Numbers.valueOf(i.split(":")[0]) == Numbers.QUEEN) {
                            if (Suits.valueOf(i.split(":")[1]) == Suits.SPADES) {
                                score += 13;
                            }
                        }
                    }
                    scores[n] += score;
                    reply = "hand" + names[n];
                } else {
                    for (int i = 0; i < currentCards.length; i++) {     // echo the card to each client
                        if (currentCards[i] == null) {
                            currentCards[i] = line;
                            break;
                        }
                    }
                    reply = "card" + line;
                }
            } else if (line.startsWith("end")) {
                int round = 0;
                for (int i = 0; i < rounds.length; i++) {
                    if (!rounds[i]) {
                        round = i + 1;
                        rounds[i] = true;
                    }
                }
                if (round != rounds.length) {
                    for (int i = 0; i < ips.length; i++) {      // set this player's spot in empty[] to true
                        if (ips[i].equals(socket.getInetAddress().toString())) {
                            empty[i] = true;
                        }
                    }
                    for (boolean i : empty) {   // once all player's hands are empty, deal a new deck
                        if (!i) {
                            break;
                        } else {
                            // TODO: deal cards for round++

                        }
                    }
                } else {                        // last round - send results
                    connect = false;
                    reply = "";
                    for (int i = 0; i < scores.length; i++) {
                        reply = reply.concat("resultsp" + names[i] + ":" + scores[i]);
                    }
                }
            } else {
                reply = "errorinvalidresponsefromclient";
            }

            // send the action to every client
            if (reply != null) {
                for (String i : ips) {
                    if (!i.contains("bot")) {
                        Socket sendTo = new Socket(i, 5544);
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sendTo.getOutputStream()));
                        writer.write(reply);
                        writer.flush();
                        sendTo.close();
                    }
                }
            }
            socket.close();
        }
    }

    private static Card[][] dealCards() {
        Deck deck = new Deck();
        deck.shuffle();
        Card[][] deals = new Card[4][13];
        String msg = "";
        for (int i= 0; i < deck.getCards(); i++) {
            for (int i = 0; i < names.length; i++) {
                if (names[i].contains("bot")) {
                    String bot = names[i].replaceFirst("bot", "");
                    bots.get(Integer.parseInt(bot)).dealCard(deck.dealCard(), );
                } else {
                    msg = msg.concat("card" + deck.dealCard().toString());
                }
            }
        }
        return deals;
    }

    private static void sendTo(String ip, String msg) {
        try {
            Socket to = new Socket(ip, 5544);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(to.getOutputStream()));
            writer.write(msg);
            writer.flush();
            to.close();
        } catch (java.io.IOException ex) {
            System.out.print("Caught " + ex.toString() + " in server sentTo() to " + ip);
        }
    }

    public static void main(String[] args) throws Exception {
        runServer();
    }
}