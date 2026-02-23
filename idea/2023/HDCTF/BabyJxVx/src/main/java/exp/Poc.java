package exp;

import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.commons.scxml2.model.SCXML;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class Poc {
    public static void main(String[] args) throws ModelException, XMLStreamException, IOException {

        // engine to execute the scxml instance
        SCXMLExecutor executor = new SCXMLExecutor();
        // parse SCXML URL into SCXML model
        String xml = "";
        SCXML scxml = SCXMLReader.read("http://127.0.0.1:8000/1.xml");

        // set state machine (scxml instance) to execute
        executor.setStateMachine(scxml);
        executor.go();

    }
}
