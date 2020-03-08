import clientSide.RequestType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.AccessControlException;
import java.security.AccessController;

public class Httpfs {
    private static ServerSocket serverSocket;
    private static String httpVersion;

    private static ServerSocket initializeServer(int port){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverSocket;
    }

    public static void checkPermission(String filePath, String permission){
        FilePermission filePermission = new FilePermission(filePath, permission);
        try{
            AccessController.checkPermission(filePermission);
        } catch (AccessControlException e) {
            System.out.println(Status.FORBIDDEN.toString());
            e.printStackTrace();
        }
    }

    private static String RequestToString(BufferedReader requestReader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line = requestReader.readLine();

        httpVersion = line.split(" ")[2];

        while (line != null) {
            if(line.isEmpty()){
                break;
            }
            sb.append(line+"\n");
            line = requestReader.readLine();
        }
        return sb.toString();
    }

    public static String createResponse(String requestString){
        String body = "Hello world my gulay!";
        return httpVersion+" "+Status.OK.toString()+"\r\nContent-Length: "+body.length()+"\r\nContent-Type: text/html\r\n\r\n"+body;
    }

    public static void main(String[] args){
        ServerSocket serverSocket = initializeServer(8080);
        System.out.println("Waiting for client on port: "+8080+"...");

        Socket client = null;
        BufferedReader requestReader = null;
        PrintWriter responseWriter = null;
        while (true){
            try {
                client = serverSocket.accept();
                requestReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                responseWriter = new PrintWriter(client.getOutputStream());

                System.out.println("Before Reading");
                String request = RequestToString(requestReader);
                System.out.println(createResponse(request));
                responseWriter.print(createResponse(request));
                responseWriter.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
