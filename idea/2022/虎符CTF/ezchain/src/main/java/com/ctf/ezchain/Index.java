package com.ctf.ezchain;

import com.caucho.hessian.io.Hessian2Constants;
import com.caucho.hessian.io.Hessian2Input;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;

/* loaded from: ezchain.jar:com/ctf/ezchain/Index.class */
public class Index {
    public static void main(String[] args) throws Exception {
        System.out.println("server start");
        HttpServer server = HttpServer.create(new InetSocketAddress(8090), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }

    /* loaded from: ezchain.jar:com/ctf/ezchain/Index$MyHandler.class */
    static class MyHandler implements HttpHandler {
        MyHandler() {
        }

        public void handle(HttpExchange t) throws IOException {
            String query = t.getRequestURI().getQuery();
            Map<String, String> queryMap = queryToMap(query);
            String response = "Welcome to HFCTF 2022";
            if (queryMap != null) {
                String token = queryMap.get("token");
                if (Objects.hashCode(token) == "HFCTF2022".hashCode() && !"HFCTF2022".equals(token)) {
                    InputStream is = t.getRequestBody();
                    try {
                        Hessian2Input input = new Hessian2Input(is);
                        input.readObject();
                    } catch (Exception e) {
                        response = "oops! something is wrong";
                    }
                } else {
                    response = "your token is wrong";
                }
            }
            t.sendResponseHeaders(Hessian2Constants.BC_INT_BYTE_ZERO, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        public Map<String, String> queryToMap(String query) {
            if (query == null) {
                return null;
            }
            Map<String, String> result = new HashMap<>();
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    result.put(entry[0], entry[1]);
                } else {
                    result.put(entry[0], "");
                }
            }
            return result;
        }
    }
}
