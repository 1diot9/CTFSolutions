package solution;


import bsh.EvalError;
import org.springframework.scripting.bsh.BshScriptUtils;
import org.springframework.util.ObjectUtils;

import javax.naming.ldap.Rdn;

public class String2Object {
    public static void main(String[] args) throws EvalError {
        String string = "http://127.0.0.1:8000/1.xml";
        Object bshObject = BshScriptUtils.createBshObject("new java.lang.Object[]{\"http://127.0.0.1:8000/1.xml\"}");
        System.out.println(bshObject);
//        Object o = Rdn.unescapeValue(string);
//        System.out.println(o.getClass().getName());
    }
}
