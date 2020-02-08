package com.abhijit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient {
    private Socket socket;
    PrintWriter sender;
    BufferedReader receiver;


    public void createSocket(String host, int port){
        try{
            this.socket = new Socket(host, port);
            this.sender = new PrintWriter(socket.getOutputStream(), true);
            this.receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRequest(RequestType requestType, String endpoint, String host) throws IOException {
        sender.println(requestType.toString()+" "+endpoint+" HTTP/1.0");
        sender.println("Host: "+host);
        sender.println("Connection: Close");
        sender.println();

        printResponse();
        socket.close();
    }

    private void printResponse() throws IOException {
        StringBuilder sb = new StringBuilder();
        String line = receiver.readLine();
        while (line != null) {
            sb.append(line+"\n");
            line = receiver.readLine();
        }
        receiver.close();
        System.out.println(sb.toString());
    }
}
