package exp;

import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.util.Set;

public class tmp {
    public static void main(String[] args) throws IOException {
        Process exec = Runtime.getRuntime().exec("touch 1.txt");
        System.out.println(exec.getInputStream());
    }
}

