import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class FWTP {

    private static final Integer PORT = 4444;
    private static final String VERSIONS = "1";
    private static final String HIGHEST_VERSION = "1";
    static PackageSender packageSender;
    static ErrorHandler errorHandler = new ErrorHandler();

    public static void main(String[] args) {
        BufferedReader cli_reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String current_line = cli_reader.readLine();
            while (!current_line.startsWith("stop")) {
                if (current_line.startsWith("connect")) {
                    Socket socket = connect(current_line.substring(8));

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    packageSender = new PackageSender(writer);

                    Boolean handShakeAcc = false;
                    String available_versions = null;
                    
                    while (!handShakeAcc) {
                        Package current_package = new Package(reader.readLine());
                        if (current_package.code == "2") {
                            errorHandler.handle(current_package.content);
                            break;
                        }
                        if (current_package.code == "0") {
                            handShakeAcc = true;
                            available_versions = current_package.content;
                        }
                    }

                    if (available_versions.contains(HIGHEST_VERSION)) {
                        packageSender.handShakeAccept(HIGHEST_VERSION);
                        Game game = new Game(reader, cli_reader, HIGHEST_VERSION, errorHandler, packageSender);
                        game.startGame(false);
                    } else {
                        packageSender.error("1");
                    }
                }
                else if (current_line.startsWith("host")) {
                    Socket socket = host();

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    packageSender.handShakeInit(VERSIONS);

                    Boolean handShakeAcc = false;
                    String version = null;
                    
                    while (!handShakeAcc) {
                        Package current_package = new Package(reader.readLine());
                        if (current_package.code.equals("2")) {
                            errorHandler.handle(current_package.content);
                            break;
                        }
                        if (current_package.code.equals("1")) {
                            handShakeAcc = true;
                            version = current_package.content;
                        }
                    }
                    if (handShakeAcc) {
                        Game game = new Game(reader, cli_reader, version, errorHandler, packageSender);
                        game.startGame(true);
                    }
                }
                current_line = cli_reader.readLine();
            }
            cli_reader.close();
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