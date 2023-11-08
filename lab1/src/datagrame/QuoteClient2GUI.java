package datagrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class QuoteClient2GUI {
    private DatagramSocket socket;
    private int clientPort = 4446;

    private JFrame frame;
    private JTextArea messageArea;
    private JTextField messageInput;
    private JButton sendButton;

    public QuoteClient2GUI() {
        try {
            socket = new DatagramSocket(clientPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        frame = new JFrame("Chat Client 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        frame.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        messageInput = new JTextField();
        sendButton = new JButton("Send");

        // Create a JPanel to organize the text field and button
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        frame.add(inputPanel, BorderLayout.SOUTH);

        // Action listener for the Send button
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = "Client 2: " + messageInput.getText();
                sendMessage(message);
                appendMessage(message); // Append the sent message to the message area
                messageInput.setText("");
            }
        });

        frame.setVisible(true);

        // Start a thread to receive messages
        Thread receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    receiveMessage();
                }
            }
        });
        receiveThread.start();
    }

    private void sendMessage(String message) {
        try {
            // Assuming you know the address of the other client
            InetAddress client1Address = InetAddress.getByName("localhost");
            int client1Port = 4445;  // Port of the receiving client (Client 1)

            byte[] buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, client1Address, client1Port);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage() {
        try {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            String receivedMessage = new String(packet.getData(), 0, packet.getLength());
            appendMessage(receivedMessage); // Append the received message to the message area
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendMessage(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                messageArea.append(message + "\n");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new QuoteClient2GUI();
            }
        });
    }
}
