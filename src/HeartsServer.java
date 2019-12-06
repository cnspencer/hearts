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
            } else if (line.startsWith("start")) {      // initiate the game: assign bots, deal cards
                int botNum = 1;
                for (int i = 0; i < ips.length; i++) {
                    if (ips[i] == null) {
                        bots.add(new Player("bot" + botNum, "bot" + botNum));
                        ips[i] = "bot" + botNum;
                        names[i] = "bot" + botNum;
                        botNum++;
                    }
                }
                // TODO: deal cards

                for (int i = 0; i < names.length; i++) {
                    if (names[i].contains("bot")) {
                        String bot = names[i].replaceFirst("bot", "");
                        bots.get(Integer.parseInt(bot)).dealCard(card);
                    } else {
                        sendTo(ips[i]);
                    }
                }
            } else if (line.startsWith("card")) {       // echo message to update each client GUI

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
                            // TODO: deal cards
                        }
                    }
                } else {                        // last round - send results
                    connect = false;
                    reply = "results";
                    // TODO: get everyone's scores together and concatenate onto reply
                }
            } else {
                reply = "errorinvalidresponsefromclient";
            }

            // send the action to each client
            if (reply != null) {
                for (String i : ips) {
                    if (i != null) {
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

    public static void main(String[] args) throws Exception {
        runServer();
    }
}