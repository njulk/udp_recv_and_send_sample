package nju.lemon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
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
//        int sendPort = Integer.valueOf(args[0]);
//        int receivePort = Integer.valueOf(args[1]);
        int sendPort = 5005;
        int receivePort = 25563;
        final File receiveFile = new File("receive_messages_" + sendPort + "to" + receivePort + ".txt");
        final FileOutputStream fos = new FileOutputStream(receiveFile);
        UdpRelay.PacketReceivedListener listener = new UdpRelay.PacketReceivedListener() {
            @Override
            public void onReceive(String msg) {
                try {
                    fos.write((msg + "/n").getBytes());
                    System.out.println("Received: " + msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        UdpRelay relay = new UdpRelay(sendPort, receivePort, listener);
        //relay.setSendIp("225.0.0.1").setReceiveIp("192.168.11.2");
        relay.setReceiveIp("114.212.117.15");
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
