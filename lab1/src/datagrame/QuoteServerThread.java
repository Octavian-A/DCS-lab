package datagrame;

import java.io.*;
import java.net.*;
import java.util.*;

public class QuoteServerThread extends Thread {

    protected DatagramSocket socket = null;
    protected boolean moreQuotes = true;
    private List<InetAddress> clientAddresses = new ArrayList<>();
    private List<Integer> clientPorts = new ArrayList<>();

    public QuoteServerThread() throws IOException {
        this("QuoteServerThread");
    }

    public QuoteServerThread(String name) throws IOException {
        super(name);
        socket = new DatagramSocket(4445);
    }

    public void run() {
        while (moreQuotes) {
            try {
                byte[] buf = new byte[256];

                // Receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                // Figure out response
                String dString = getNextQuote();
                buf = dString.getBytes();

                // Send the response to all clients
                for (int i = 0; i < clientAddresses.size(); i++) {
                    InetAddress address = clientAddresses.get(i);
                    int port = clientPorts.get(i);
                    packet = new DatagramPacket(buf, buf.length, address, port);
                    socket.send(packet);
                }
            } catch (IOException e) {
                e.printStackTrace();
                moreQuotes = false;
            }
        }
        socket.close();
    }

    protected String getNextQuote() {
        String returnValue = null;
        try (BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\apetr\\Desktop\\one-liners.txt"))) {
            if ((returnValue = in.readLine()) == null) {
                moreQuotes = false;
                returnValue = "No more quotes. Goodbye.";
            }
        } catch (IOException e) {
            returnValue = "IOException occurred in server.";
        }
        return returnValue;
    }

    public static void main(String[] args) {
        try {
            QuoteServerThread srv = new QuoteServerThread("test");
            srv.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
