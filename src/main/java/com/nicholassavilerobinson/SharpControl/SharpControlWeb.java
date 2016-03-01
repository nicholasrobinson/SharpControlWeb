package com.nicholassavilerobinson.SharpControl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class SharpControlWeb {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new IndexHandler());
        server.createContext("/api", new ApiHandler());
        server.setExecutor(null);
        server.start();
    }

    static class IndexHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            URI uri = t.getRequestURI();
            // Default to index.html
            if (Objects.equals(uri.getPath(), "/")) {
                try {
                    uri = new URI("/index.html");
                } catch (URISyntaxException ignored) {
                }
            }
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("public_html" + uri.getPath());
            if (is != null) {
                // Object exists: accept with response code 200.
                t.sendResponseHeaders(200, 0);
                OutputStream os = t.getResponseBody();
                final byte[] buffer = new byte[0x10000];
                int count;
                while ((count = is.read(buffer)) >= 0) {
                    os.write(buffer, 0, count);
                }
                os.close();
            } else {
                // Object does not exist: reject with 404 error.
                String response = "404 (Not Found)\n";
                t.sendResponseHeaders(404, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    static class ApiHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String responseString;
            int statusCode;
            switch (t.getRequestMethod()) {
                case "POST":
                    final String[] commandParts = IOUtils.toString(t.getRequestBody(), "UTF-8").split(":");
                    final String server = t.getRequestHeaders().getFirst("X-Sharp-Server");
                    SharpControl sc = new SharpControl(server);
                    try {
                        statusCode = 200;
                        sc.connect();
                        SharpControlReturnData returnData = sc.sendCommand(commandParts);
                        responseString = returnData.getReturnString();
                        sc.disconnect();
                    } catch (SharpControlException e) {
                        statusCode = 500;
                        responseString = e.getMessage();
                        try {
                            sc.disconnect();
                        } catch (SharpControlException ignored) {
                        }
                    }
                    break;
                default:
                    statusCode = 500;
                    responseString = "500 (Internal Server Error)";
            }
            t.sendResponseHeaders(statusCode, responseString.length());
            OutputStream os = t.getResponseBody();
            os.write(responseString.getBytes());
            os.close();
        }
    }
}
