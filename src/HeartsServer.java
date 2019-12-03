import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class HeartsServer {
    private static void runServer() {
        String[] ips = new String[4];
        ServerSocket servSock;
        try {
            servSock = new ServerSocket(5545);
        } catch (java.io.IOException ex) {
            System.out.println("Caught " + ex.toString() + "HeartsServer()");
        }
        boolean connect = true;
        System.out.println("Server running on port " + servSock.getLocalPort());
        while (connect) {


            connect = false;
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