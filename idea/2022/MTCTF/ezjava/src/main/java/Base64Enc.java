import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class Base64Enc {
    public static void main(String[] args) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("D://tmp//memshell//evilInterceptor.class"));
        String s = Base64.getEncoder().encodeToString(bytes);
        System.out.println(s);
    }
}
