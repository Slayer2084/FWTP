import java.io.BufferedWriter;
import java.io.IOException;

public class PackageSender {
    public void sendPackage(BufferedWriter writer, String aktion, String inhalt) {
        try {
            writer.write(aktion + "|" + inhalt);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handShakeInit(BufferedWriter writer, String versionList) {
        sendPackage(writer, "0", versionList);
    }

    public void handShakeAccept(BufferedWriter writer, String version) {
        sendPackage(writer, "1", version);
    }

    public void error(BufferedWriter writer, String code) {
        sendPackage(writer, "2", code);
    }

    public void move(BufferedWriter writer, Integer column) {
        sendPackage(writer, "3", column.toString());
    }
}
