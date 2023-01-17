import java.util.HashMap;

public class ErrorHandler {
    HashMap<String, String> dict = new HashMap<String, String>();

    public ErrorHandler() {
        dict.put("1", "No matching version found!");
        dict.put("2", "Invalid move!");
    }

    public void handle(String code) {
        System.out.println(dict.getOrDefault(code, "Oops! Something went wrong!"));
    }
}
