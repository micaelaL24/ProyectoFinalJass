package online;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientThread extends Thread {

    private final int serverPort = 5000;
    private String serverIpStr = "192.168.0.181";

    private DatagramSocket socket;
    private InetAddress serverIp;

    private volatile boolean end = false;

    private ClientNetworkListener listener;

    private int myPlayerNum = -1;

    public ClientThread(ClientNetworkListener listener) {
        this.listener = listener;

        try {
            serverIp = InetAddress.getByName(serverIpStr);
            socket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListener(ClientNetworkListener listener) {
        this.listener = listener;
    }

    public int getMyPlayerNum() {
        return myPlayerNum;
    }

    @Override
    public void run() {
        if (socket == null) return;

        sendMessage("Connect");
        System.out.println("[CLIENT] -> Connect enviado");

        while (!end) {
            try {
                DatagramPacket packet =
                    new DatagramPacket(new byte[1024], 1024);
                socket.receive(packet);

                processMessage(packet);

            } catch (IOException e) {
                if (!end) e.printStackTrace();
            }
        }

        if (!socket.isClosed()) socket.close();
    }

    private void processMessage(DatagramPacket packet) {
        String msg = new String(packet.getData(), 0, packet.getLength()).trim();
        String[] parts = msg.split(":");

        switch (parts[0]) {

            case "PlayerNum":
                myPlayerNum = Integer.parseInt(parts[1]);
                if (listener != null) listener.onConnected(myPlayerNum);
                break;

            case "Start":
                if (listener != null) listener.onStart();
                break;

            case "Full":
                if (listener != null) listener.onFull();
                break;

            case "UpdatePos":
                if (parts.length >= 5) {
                    int player = Integer.parseInt(parts[1]);
                    float x = Float.parseFloat(parts[2]);
                    float y = Float.parseFloat(parts[3]);
                    boolean dir = parts[4].equals("1");

                    if (listener != null)
                        listener.onUpdatePos(player, x, y, dir);
                }
                break;

            case "Pause":
                listener.onPauseGame();
                break;

            case "Resume":
                listener.onResumeGame();
                break;
        }
    }

    public void sendMessage(String message) {
        try {
            byte[] data = message.getBytes();
            DatagramPacket packet =
                new DatagramPacket(data, data.length, serverIp, serverPort);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPosition(float x, float y, boolean mirandoDerecha) {
        if (myPlayerNum == -1) return;

        int dir = mirandoDerecha ? 1 : 0;
        String msg = "UpdatePos:" + myPlayerNum + ":" + x + ":" + y + ":" + dir;
        sendMessage(msg);
    }

    public void terminate() {
        end = true;
        sendMessage("Disconnect");

        if (socket != null && !socket.isClosed())
            socket.close();

        interrupt();
    }
}
