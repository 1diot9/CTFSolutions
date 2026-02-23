package tools;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import memshell.SSOLogin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class ReadByte {
    public static String readFile(String fileName) throws IOException, NotFoundException, CannotCompileException {
        byte[] bytecode = ClassPool.getDefault().get(SSOLogin.class.getName()).toBytecode();
//        byte[] bytes = Files.readAllBytes(Paths.get(fileName));
        String s = Base64.getEncoder().encodeToString(bytecode);
        return s;
    }

    public static void main(String[] args) throws IOException, NotFoundException, CannotCompileException {
        String s = readFile("D:\\tmp\\SSOLogin.class");
        System.out.println(s);
    }
}
