import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class HeartsServer {
    private static void runServer() throws Exception {
        String[] ips = new String[4];
        ServerSocket servSock = new ServerSocket(5545);

        boolean connect = true;
        System.out.println("Server running on port " + servSock.getLocalPort());
        while (connect) {
            String reply = null;
            Socket socket = servSock.accept();
            BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = read.readLine().trim();

            if (line.startsWith("ip")) {
                // set ips of players
                for (int i = 0; i < ips.length; i++) {
                    if (ips[i] == null) {
                        ips[i] = line.replaceFirst("ip", "");
                        break;
                    }
                }
            } else if (line.startsWith("start")) {
                // TODO: Need to set bots when less than four IPs are set
            } else if (line.startsWith("turn")) {
                // TODO: process turn then send signal to update graphics
            } else if (line.startsWith("end")) {
                connect = false;
                reply = "getresults";
            } else {
                reply = "errorinvalidstring";
            }

            // send the action to each client
            if (reply != null) {
                for (String i : ips) {
                    if (i != null) {
                        Socket sendTo = new Socket(i, 5544);
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sendTo.getOutputStream()));
                        writer.write(reply);
                        writer.flush();
                        sendTo.close()
                    }
                }
            }
            socket.close();
        }
    }
    private void game(int numRounds) {
        for (int i = 0; i < numRounds; i++) {
            Round round = new Round(i);
            round.displayPX(1);
            round.tradeCards(i);
        }
    }
    public static void main(String[] args) throws Exception {
        runServer();
    }
}