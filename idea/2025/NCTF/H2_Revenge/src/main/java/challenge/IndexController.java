package challenge;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
/* loaded from: H2Revenge.jar:BOOT-INF/classes/challenge/IndexController.class */
public class IndexController {
    @RequestMapping({"/"})
    public String index() {
        return "H2 Revenge";
    }

    @PostMapping({"/deserialize"})
    public String deserialize(@RequestParam String data) throws Exception {
        byte[] buffer = Base64.getDecoder().decode(data);
        ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(buffer));
        try {
            input.readObject();
            input.close();
            return "ok";
        } catch (Throwable th) {
            try {
                input.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }
}
