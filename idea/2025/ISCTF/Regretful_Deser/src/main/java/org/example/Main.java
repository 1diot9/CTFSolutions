//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new IndexHandler());
        server.createContext("/hello", new HelloHandler());
        server.createContext("/api/echo", new EchoHandler());
        server.setExecutor((Executor)null);
        System.out.println("Server started at http://localhost:" + port);
        server.start();
    }

    private static void sendText(HttpExchange exchange, int statusCode, String text) throws IOException {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, (long)bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }

    }

    private static void sendHtml(HttpExchange exchange, int statusCode, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, (long)bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }

    }

    private static String readBody(HttpExchange exchange) throws IOException {
        String var9;
        try (InputStream is = exchange.getRequestBody()) {
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            Throwable var4 = null;

            try {
                BufferedReader br = new BufferedReader(isr);
                Throwable var6 = null;

                try {
                    StringBuilder sb = new StringBuilder();

                    String line;
                    while((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    var9 = sb.toString();
                } catch (Throwable var53) {
                    var6 = var53;
                    throw var53;
                } finally {
                    if (br != null) {
                        if (var6 != null) {
                            try {
                                br.close();
                            } catch (Throwable var52) {
                                var6.addSuppressed(var52);
                            }
                        } else {
                            br.close();
                        }
                    }

                }
            } catch (Throwable var55) {
                var4 = var55;
                throw var55;
            } finally {
                if (isr != null) {
                    if (var4 != null) {
                        try {
                            isr.close();
                        } catch (Throwable var51) {
                            var4.addSuppressed(var51);
                        }
                    } else {
                        isr.close();
                    }
                }

            }
        }

        return var9;
    }

    private static Map<String, String> parseQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");

            for(String pair : pairs) {
                int idx = pair.indexOf(61);
                if (idx > 0 && idx < pair.length() - 1) {
                    String key = URLDecoder.decode(pair.substring(0, idx), String.valueOf(StandardCharsets.UTF_8));
                    String value = URLDecoder.decode(pair.substring(idx + 1), String.valueOf(StandardCharsets.UTF_8));
                    result.put(key, value);
                } else if (idx == -1) {
                    String key = URLDecoder.decode(pair, String.valueOf(StandardCharsets.UTF_8));
                    result.put(key, "");
                }
            }

            return result;
        } else {
            return result;
        }
    }

    static class IndexHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if (!"GET".equalsIgnoreCase(method)) {
                Main.sendText(exchange, 405, "Method Not Allowed");
            } else {
                String html = "hello index";
                Main.sendHtml(exchange, 200, html);
            }
        }
    }

    static class HelloHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                Main.sendText(exchange, 405, "Method Not Allowed");
            } else {
                URI uri = exchange.getRequestURI();
                Map<String, String> queryParams = Main.parseQuery(uri.getRawQuery());
                String name = (String)queryParams.getOrDefault("name", "World");
                String response = "Hello, " + name + "!";
                Main.sendText(exchange, 200, response);
            }
        }
    }

    static class EchoHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            List<String> cookie = exchange.getRequestHeaders().get("Pass");
            String pass = (String)cookie.get(0);
            if (!pass.equals("n1ght") && pass.hashCode() == "n1ght".hashCode()) {
                List<String> echo = exchange.getRequestHeaders().get("echo");
                String s = (String)echo.get(0);
                byte[] decode = Base64.getDecoder().decode(s);

                try {
                    (new SecurityObjectInputStream(new ByteArrayInputStream(decode))).readObject();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
