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
        Card[] played = new Card[3];
        String[] passes = new String[4];
        ServerSocket servSock = new ServerSocket(5545);

        boolean connect = true;
        System.out.println("Server running on port " + servSock.getLocalPort());
        while (connect) {
            String reply = null;
            Socket socket = servSock.accept();
            BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = "";
            try {
                line = read.readLine().trim();
            } catch (NullPointerException ex) {
                System.out.println("Caught \"" + ex.toString() + " trying to read input");
            }

            if (line.startsWith("ip")) {                // set ips of players
                for (int i = 0; i < ips.length; i++) {
                    if (ips[i] == null) {
                        ips[i] = line.replaceFirst("ip", "");
                        break;
                    } else if (i == ips.length - 1) {
                        reply = "erroripfull";
                        sendTo(socket.getInetAddress().toString(), reply);
                    }
                }
            } else if (line.startsWith("name")) {       // set ips of players
                for (int i = 0; i < names.length; i++) {
                    if (names[i] == null) {
                        names[i] = line.replaceFirst("name", "");
                        break;
                    } else if (i == names.length - 1) {
                        reply = "errornamefull";
                        sendTo(socket.getInetAddress().toString(), reply);
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
                for (String i : ips) {
                    if (!i.contains("bot")) {
                        sendTo(i, reply);
                    }
                }

                Card[][] deals = dealCards();
                int bot = 0;
                for (int i = 0; i < ips.length; i++) {
                    String msg = "deal";
                    if (!ips[i].startsWith("bot")) {
                        for (Card j : deals[i]) {
                            msg = msg.concat(j.getNumber().toString() + ":" + j.getSuit().toString() + ":");
                        }
                        sendTo(ips[i], msg);
                    } else {
                        for (Card j : deals[i]) {
                            bots.get(bot).dealCard(j, i);
                        }
                        bot++;
                    }
                }

                rounds[0] = true;
                int round = 0;
                for (int i = 0; i < rounds.length; i++) {
                    if (rounds[i]) {
                        round = i;
                    }
                }
                String direction = getDirection(round);
                int botx = 0;
                if (direction != null) {
                    reply = "pass" + direction;
                    for (int i = 0; i < ips.length; i++) {
                        if (!ips[i].startsWith("bot")) {
                            sendTo(ips[i], reply);
                        } else {
                            Card[] botPass = bots.get(botx).botTurnPass();
                            String cards = "";
                            for (Card j : botPass) {
                                cards = cards.concat("card" + j.getNumber().toString() + ":" + j.getSuit().toString());
                            }
                            passes[i] = cards;
                            botx++;
                        }
                    }
                }

                // does a bot have the Two of Clubs
                for (int i = 0; i < currentCards.length; i++) {
                    played[i] = new Card(Numbers.valueOf(currentCards[i].split(":")[0]), Suits.valueOf(currentCards[i].split(":")[1]));
                }
                int botn = 0;
                for (int j = 0; j < ips.length; j++) {
                    if (bots.get(botn).getCard(Numbers.TWO, Suits.CLUBS) != null) {
                        bots.get(botn).botTurnPlay(played);
                    }
                    botn++;
                }
            } else if (line.startsWith("card")) {       // card operations
                line = line.replaceFirst("card", "");
                if (line.contains("card")) {            // handle card passing
                    for (int i = 0; i < ips.length; i++) {
                        if (ips[i].equals(socket.getInetAddress().toString())) {
                            passes[i] = line.concat("card");
                        }
                    }
                    for (int i = 0; i < passes.length; i++) {
                        if (passes[i] != null) {
                            if (i == passes.length - 1) {
                                int round = 0;
                                for (int j = 0; j < rounds.length; j++) {
                                    if (rounds[j]) {
                                        round = j;
                                    }
                                }
                                switch (round) {
                                    case (0):
                                        sendTo(ips[1], passes[0]);
                                        sendTo(ips[2], passes[1]);
                                        sendTo(ips[3], passes[2]);
                                        sendTo(ips[0], passes[3]);
                                        break;
                                    case (1):
                                        sendTo(ips[3], passes[0]);
                                        sendTo(ips[0], passes[1]);
                                        sendTo(ips[1], passes[2]);
                                        sendTo(ips[2], passes[3]);
                                        break;
                                    case (2):
                                        sendTo(ips[2], passes[0]);
                                        sendTo(ips[3], passes[1]);
                                        sendTo(ips[0], passes[2]);
                                        sendTo(ips[1], passes[3]);
                                        break;
                                }
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < currentCards.length; i++) {
                        if (currentCards[i] == null) {      // keep track of the played cards for the bots
                            currentCards[i] = line;
                            break;
                        }
                    }
                    for (int i = 0; i < currentCards.length; i++) {
                        played[i] = new Card(Numbers.valueOf(currentCards[i].split(":")[0]), Suits.valueOf(currentCards[i].split(":")[1]));
                    }
                    reply = "card" + line;      // echo to other clients
                    for (int i = 0; i < ips.length; i++) {
                        if (!ips[i].contains("bot")) {
                            sendTo(ips[i], reply + "p" + names[i]);
                        }
                    }
                    if (currentCards[currentCards.length - 1] != null) { // if all cards are out, give a score to someone
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
                        int bot = 0;
                        for (int i = 0; i < ips.length; i++) {
                            if (!ips[i].contains("bot")) {
                                sendTo(ips[i], reply);
                            } else {
                                Card play = bots.get(bot).botTurnPlay(played);
                                for (String j : ips) {
                                    if (!j.contains("bot")) {
                                        sendTo(j, "card" + play.getNumber().toString() + ":" + play.getSuit().toString());
                                        for (int k = 0; k < played.length; k++) {
                                            if (played[k] == null) {
                                                played[k] = play;
                                                break;
                                            }
                                        }
                                    }
                                }
                                bot++;
                            }
                        }
                    }
                }
            } else if (line.startsWith("end")) {
                int round = 0;
                for (int i = 0; i < rounds.length; i++) {
                    if (!rounds[i]) {
                        round = i;
                        rounds[i] = true;
                        break;
                    }
                }
                if (round != rounds.length - 1) {
                    for (int i = 0; i < ips.length; i++) {      // set this player's spot in empty[] to true
                        if (ips[i].equals(socket.getInetAddress().toString())) {
                            empty[i] = true;
                        }
                    }
                    if (empty[empty.length - 1]) {  // once all player's hands are empty, deal a new deck
                        Card[][] deals = dealCards();
                        int bot = 0;
                        for (int i = 0; i < ips.length; i++) {
                            String msg = "deal";
                            if (!ips[i].startsWith("bot")) {
                                for (Card j : deals[i]) {
                                    msg = msg.concat(j.getNumber().toString() + ":" + j.getSuit().toString() + ":");
                                }
                                sendTo(ips[i], msg);
                            } else {
                                for (Card j : deals[i]) {
                                    bots.get(bot).dealCard(j, i);
                                }
                                bot++;
                            }
                        }

                        round = 0;
                        for (int i = 0; i < rounds.length; i++) {
                            if (rounds[i]) {
                                round = i;
                            }
                        }
                        String direction = getDirection(round);
                        int botx = 0;
                        if (direction != null) {
                            reply = "pass" + direction;
                            for (int i = 0; i < ips.length; i++) {
                                if (!ips[i].startsWith("bot")) {
                                    sendTo(ips[i], reply);
                                } else {
                                    Card[] botPass = bots.get(botx).botTurnPass();
                                    String cards = "";
                                    for (Card j : botPass) {
                                        cards = cards.concat("card" + j.getNumber().toString() + ":" + j.getSuit().toString());
                                    }
                                    passes[i] = cards;
                                    botx++;
                                }
                            }


                            // does a bot have the Two of Clubs
                            for (int i = 0; i < currentCards.length; i++) {
                                played[i] = new Card(Numbers.valueOf(currentCards[i].split(":")[0]), Suits.valueOf(currentCards[i].split(":")[1]));
                            }
                            int botn = 0;
                            for (int j = 0; j < ips.length; j++) {
                                if (bots.get(botn).getCard(Numbers.TWO, Suits.CLUBS) != null) {
                                    bots.get(botn).botTurnPlay(played);
                                }
                                botn++;
                            }
                        }
                    }
                } else {                        // last round - send results
                    connect = false;
                    reply = "";
                    for (int i = 0; i < scores.length; i++) {
                        for (String j : ips) {
                            if (!j.contains("bot")) {
                                sendTo(j, "resultsp" + names[i] + ":" + scores[i]);
                            }
                        }
                    }
                }
            } else {
                reply = "errorinvalidresponsefromclient";
                sendTo(socket.getInetAddress().toString(), reply);

            }
            socket.close();
        }
    }

    private static Card[][] dealCards() {
        Deck deck = new Deck();
        deck.shuffle();
        Card[][] deals = new Card[4][13];
        for (int i = 0; i < deck.getCards(); i++) {
            deals[Math.floorMod(i, 4)][Math.floorMod(i, 13)] = deck.dealCard();
        }
        return deals;
    }

    private static String getDirection(int round) {
        String direction = null;
        switch (round) {
            case (0):
                direction = "right"; break;
            case (1):
                direction = "left"; break;
            case (2):
                direction = "top"; break;
        }
        return direction;
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