import com.nicholassavilerobinson.SharpControl.SharpControl;
import com.nicholassavilerobinson.SharpControl.SharpControlException;
import com.nicholassavilerobinson.SharpControl.SharpControlReturnData;
import com.nicholassavilerobinson.SharpControl.commands.AbstractSharpControlCommand;
import com.nicholassavilerobinson.SharpControl.commands.PureSharpControlCommand;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

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
            String root = System.getProperty("user.dir") + "/public_html";
            URI uri = t.getRequestURI();
            File file = new File(root + uri.getPath()).getCanonicalFile();
            if (!file.getPath().startsWith(root)) {
                // Suspected path traversal attack: reject with 403 error.
                String response = "403 (Forbidden)\n";
                t.sendResponseHeaders(403, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else if (!file.isFile()) {
                // Object does not exist or is not a file: reject with 404 error.
                String response = "404 (Not Found)\n";
                t.sendResponseHeaders(404, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // Object exists and is a file: accept with response code 200.
                t.sendResponseHeaders(200, 0);
                OutputStream os = t.getResponseBody();
                FileInputStream fs = new FileInputStream(file);
                final byte[] buffer = new byte[0x10000];
                int count = 0;
                while ((count = fs.read(buffer)) >= 0) {
                    os.write(buffer, 0, count);
                }
                fs.close();
                os.close();
            }
        }
    }

    static class ApiHandler implements HttpHandler {
        private static final String SERVER = "aquos";

        public void handle(HttpExchange t) throws IOException {
            String responseString;
            int statusCode;
            switch (t.getRequestMethod()) {
                case "POST":
                    final String commandAlias = IOUtils.toString(t.getRequestBody(), "UTF-8");
                    try {
                        statusCode = 200;
                        SharpControl sc = new SharpControl(SERVER);
                        sc.connect();
//                        SharpControlReturnData rd = sc.sendCommand("REMOTE_BUTTON", 12);
//                        SharpControlReturnData rd3 = sc.sendCommand("MODEL_NAME");
                        SharpControlReturnData returnData = sc.sendCommand(commandAlias);
                        responseString = returnData.getReturnString();
                        sc.disconnect();
                    } catch (SharpControlException e) {
                        statusCode = 500;
                        responseString = e.getMessage();
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