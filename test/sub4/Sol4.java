import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.google.gson.Gson;
import com.lgcns.test.DeviceInfoData.DeviceInfo;
import com.lgcns.test.ServerCommandInfoData.ServerCommandInfo;

public class Sol4 {

    private Gson gson = new Gson();
    private ServerCommandInfoData serverCommandInfoData;
    private DeviceInfoData deviceInfoData;

    public void run() throws Exception {

        loadServerCommandInfo();
        loadDeviceInfo();

        Server fromServer = createServer();
        fromServer.start();
    }

    private void loadServerCommandInfo() throws Exception {
        serverCommandInfoData = gson.fromJson(
                String.join("", Files.readAllLines(Paths.get("INFO/SERVER_COMMAND.JSON"))),
                ServerCommandInfoData.class);
    }

    private void loadDeviceInfo() throws Exception {
        deviceInfoData = gson
                .fromJson(String.join("", Files.readAllLines(Paths.get("INFO/DEVICE.JSON"))), DeviceInfoData.class);
        this.deviceInfoMap = new HashMap<>();
    }

    private Server createServer() {
        Server server = new Server();
        ServerConnector http = new ServerConnector(server);
        http.setHost("127.0.0.1");
        http.setPort(8010);
        server.addConnector(http);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/*");
        context.addServlet(new ServletHolder(new EdgeNodeServlet(serverCommandInfoMap, deviceInfoMap)), "/*");
        server.setHandler(context);

        return server;
    }
}