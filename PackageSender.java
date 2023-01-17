import java.io.BufferedWriter;
import java.io.IOException;

public class PackageSender {

    BufferedWriter writer;

    public PackageSender(BufferedWriter writer) {
        this.writer = writer;
    }
    public void sendPackage(String aktion, String inhalt) {
        try {
            writer.write(aktion + "|" + inhalt + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handShakeInit(String versionList) {
        System.out.println("Initializing handshake");
        sendPackage("0", versionList);
    }

    public void handShakeAccept(String version) {
        System.out.println("Accepting handshake");
        sendPackage("1", version);
    }

    public void error(String code) {
        sendPackage("2", code);
    }

    public void move(Integer column) {
        sendPackage("3", column.toString());
    }
}
