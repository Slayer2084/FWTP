public class Package {
    public final String code;
    public final String content;
    public Package(String packageToBeInterpreted) {
        String[] packageArray = packageToBeInterpreted.split("\\|");
        code = packageArray[0];
        if (code != "2") {
            content = packageArray[1];
        } else {
            String error_code = packageArray[1];
            String error_message = packageArray[2];
            content = error_code + error_message;
        }
        System.out.println(code);
    }
}
