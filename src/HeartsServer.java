import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class HeartsServer {
    private String[] ips = new String[4];
    HeartsServer(String p1, String p2, String p3, String p4) {
        this.ips[0] = p1;
        this.ips[1] = p2;
        this.ips[2] = p3;
        this.ips[3] = p4;
    }
    public static void main(String[] args) throws Exception {

        System.out.println("Server running");
        while (true) {

        }
    }
}