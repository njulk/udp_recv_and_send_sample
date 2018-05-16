package nju.lemon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    /**
     * This is a test code only
     * @param args sendPort receivePort [int]
     * @throws IOException e
     */
    public static void main(String[] args) throws IOException{
        if (args.length < 2) {
            System.out.println("Usage: Main [send port] [receive port]");
            return;
        }
        int sendPort = Integer.valueOf(args[0]);
        int receivePort = Integer.valueOf(args[1]);
        final File receiveFile = new File("receive_messages_" + sendPort + "to" + receivePort + ".txt");
        FileOutputStream fos = new FileOutputStream(receiveFile);
        UdpRelay.PacketReceivedListener listener = msg -> {
            try {
                fos.write((msg + "/n").getBytes());
                System.out.println("Received: " + msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        UdpRelay relay = new UdpRelay(sendPort, receivePort, listener);
        relay.start();
        Scanner in = new Scanner(System.in);
        String input = "";
        while(!"exit".equals(input)) {
            input = in.next();
            if("exit".equals(input)) continue;
            relay.send(input);
        }
        relay.stop();
    }
}
