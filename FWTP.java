import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class FWTP {

    private static final Integer PORT = 4444;
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String current_line = reader.readLine();
            while (!current_line.startsWith("stop")) {
                if (current_line.startsWith("connect")) {
                    Socket socket = connect(current_line.substring(8));
                }
                if (current_line.startsWith("host")) {
                    Socket socket = host();
                }
                current_line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Socket host() {
        try {
            ServerSocket serv_socket = new ServerSocket(PORT);
            System.out.println("Hosting on port: " + PORT.toString());
            Socket socket = serv_socket.accept();
            serv_socket.close();
            System.out.println("Established connection with " + socket.getRemoteSocketAddress());
            return socket;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Socket connect(String address) {
        Socket socket = null;
        while (socket == null) {
            System.out.println("Trying to connect to " + address);
            try {
                socket = new Socket(address, PORT);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Established connection");
        return socket;
    }
}