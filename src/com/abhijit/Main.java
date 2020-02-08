package com.abhijit;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String host = "httpbin.org";
        RequestType requestType = RequestType.GET;
        String endpoint = "/status/418";

        TCPClient httpc = new TCPClient();

        httpc.createSocket(host, 80);

        try {
            httpc.sendRequest(requestType, endpoint, host);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
