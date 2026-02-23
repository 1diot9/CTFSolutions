import java.io.Serializable;

public class ToStringClass implements Serializable {
    public String toString(){
        System.out.println("ToStringClass#toString be called");
        return "ToStringClass#toString()";
    }
}
