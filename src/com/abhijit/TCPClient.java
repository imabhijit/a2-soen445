package com.abhijit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient {
    private Socket socket;
    private PrintWriter sender;
    private BufferedReader receiver;

    private TCPClient() {
        //do not allow creating of TCP client without any params;
    }

    public TCPClient(Socket socket, PrintWriter sender, BufferedReader receiver) {
        this.socket = socket;
        this.sender = sender;
        this.receiver = receiver;
    }

    public TCPClient(String host, int port){
        try{
            socket = new Socket(host, port);
            sender = new PrintWriter(socket.getOutputStream(), true);
            receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRequest(RequestType requestType, String endpoint, String host, String header, String data, boolean verbose) throws IOException {
        sender.println(requestType+" "+endpoint+" HTTP/1.0");
        sender.println("Host: "+host);

        if(requestType == RequestType.GET) sendGetRequest();
        if(requestType == RequestType.POST) sendPostRequest(header, data);

        printResponse(verbose);
        socket.close();
    }

    private void sendGetRequest() throws IOException {
        sender.println("Connection: Close");
        sender.println();
    }

    private void sendPostRequest(String header, String data) throws IOException {
        sender.println(header);
        sender.println("Content-Length: "+data.length());
        sender.println();
        sender.println(data);
        sender.println("Connection: Close");
        sender.println();
    }

    private void printResponse(boolean verbose) throws IOException {
        StringBuilder sb = new StringBuilder();
        Character character = (char) receiver.read();

        if(verbose == false){
            while(character != '{') {
                character = (char) receiver.read();
            }
        }

        sb.append(character);
        String line = receiver.readLine();
        while (line != null) {
            sb.append(line+"\n");
            line = receiver.readLine();
        }
        receiver.close();
        System.out.println(sb.toString());
    }
}
