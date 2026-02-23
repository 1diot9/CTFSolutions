//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package challenge;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import javax.naming.InitialContext;

public class Web {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8000), 0);
        server.createContext("/lookup", (exchange) -> {
            String url = exchange.getRequestHeaders().getFirst("X-Lookup-URL");

            try {
                (new InitialContext()).lookup(url);
            } catch (Exception var3) {
            }

            exchange.sendResponseHeaders(200, 0L);
            exchange.getResponseBody().write("OK".getBytes());
            exchange.close();
        });
        server.start();
    }
}
