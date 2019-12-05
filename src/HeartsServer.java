import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HeartsServer {
    private static void runServer() throws Exception {
        String[] ips = new String[4];
        String[] names = new String[4];
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
                // TODO: set bots when less than four IPs are set
                // TODO: deal cards
            } else if (line.startsWith("card")) {       // echo message to update each client GUI

            } else if (line.startsWith("end")) {
                int round = 0;
                for (int i = 0; i < rounds.length; i++) {
                    if (!rounds[i]) {
                        round = i + 1;
                        rounds[i] = true;
                    }
                }
                if (round != rounds.length) {   // once all player's hands are empty, deal a new deck
                    for (boolean i : empty) {
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