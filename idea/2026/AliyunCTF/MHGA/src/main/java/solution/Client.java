package solution;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Client {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        initialContext.lookup("rmi://127.0.0.1:1399/any");
    }
}