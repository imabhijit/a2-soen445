import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.AccessControlException;
import java.security.AccessController;

public class Httpfs {
    private static ServerSocket serverSocket;
    private static String httpVersion;
    private static String filePath;
    private static String pathToDirectory = "src/documents";
    private static String requestSpecification;
    private static boolean debugging;
    private static String data;
    private static int port = 8080;


    private static ServerSocket initializeServer(int portNumber) {
        if (portNumber < 0 && portNumber > 65535) {
            System.out.println("Invalid port number, starting server on default port: 8080");
            portNumber = 8080;
        }
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverSocket;
    }

    public static void checkPermission(String filePath, String permission) {
        FilePermission filePermission = new FilePermission(filePath, permission);
        try {
            AccessController.checkPermission(filePermission);
        } catch (AccessControlException e) {
            System.out.println(Status.FORBIDDEN.toString());
            e.printStackTrace();
        }
    }

    private static String RequestToString(BufferedReader requestReader) throws IOException {
        StringBuilder sb = new StringBuilder();

        String line = requestReader.readLine();
        filePath = pathToDirectory + line.split(" ")[1];

        httpVersion = line.split(" ")[2];

        while (line != null && !line.isEmpty()) {
            sb.append(line + "\n");
            line = requestReader.readLine();
        }
        requestSpecification = (debugging) ? sb.toString():"";
        return sb.toString();
    }

    public static String createResponse(String requestString) {
        String body = "";
        if(!filePath.equals("/")) {
            try {
                body = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            } catch (IOException e) {
                return requestSpecification + httpVersion + " " + Status.NOT_FOUND.toString() + "\r\nContent-Length: " + body.length() + "\r\nContent-Type: text/html\r\n\r\n" + "404 Not Found.";
            }
        }
        return requestSpecification + httpVersion + " " + Status.OK.toString() + "\r\nContent-Length: " + body.length() + "\r\nContent-Type: text/html\r\n\r\n" + body;
    }

    public static void main(String[] args) {

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("help")) {
                System.out.println("usage: httpfs [-v] [-p PORT] [-d PATH-TO-DIR]\n" +
                        "-v Prints debugging messages.\n" +
                        "-p Specifies the port number that the server will listen and serve at.\n" +
                        "\tDefault is 8080.\n" +
                        "-d Specifies the directory that the server will use to read/write requested files." +
                        "Default is the current directory when launching the application.");
                System.exit(0);
            } else {
                for (int i = 0; i < args.length; i++) {
                    parseCommand(i, args);
                }
            }
        }

        ServerSocket serverSocket = initializeServer(port);
        System.out.println("Waiting for client on port: " + port + "...");

        Socket client = null;
        BufferedReader requestReader = null;
        PrintWriter responseWriter = null;

        while (true) {
            try {
                client = serverSocket.accept();
                requestReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                responseWriter = new PrintWriter(client.getOutputStream());
                String request = RequestToString(requestReader);
                System.out.println(createResponse(request));
                responseWriter.print(createResponse(request));
                responseWriter.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void parseCommand(int i, String[] arr) {
        if (arr[i].equalsIgnoreCase("-v")) {
            debugging = true;
        } else if (arr[i].equalsIgnoreCase("-p")) {
            port = Integer.valueOf(arr[i + 1]);
        } else if (arr[i].equalsIgnoreCase("-d")) {
            pathToDirectory = arr[i + 1];
        }
    }
}
