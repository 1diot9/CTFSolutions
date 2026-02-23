package tools;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import memshell.ControllerShell01;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Write2Path {
    public static void write2Path(String path) throws NotFoundException, IOException, CannotCompileException {
        byte[] bytecode = ClassPool.getDefault().get(ControllerShell01.class.getName()).toBytecode();
        String s = Base64.getEncoder().encodeToString(bytecode);
        new FileOutputStream(path).write(s.getBytes());
    }

    public static void main(String[] args) throws NotFoundException, IOException, CannotCompileException {
        String path = "D:\\tmp\\memshell.txt";
        write2Path(path);
    }
}
