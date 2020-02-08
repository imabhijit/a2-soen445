package com.abhijit;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String host = "httpbin.org";
//        RequestType requestType = RequestType.GET;
//        String endpoint = "/get?course=networking&assignment=1";
        RequestType requestType = RequestType.POST;
        String endpoint = "/post";
        String header = "Content-Type:application/json";

        TCPClient httpc = new TCPClient();

        httpc.createSocket(host, 80);

        try {
            httpc.sendRequest(requestType, endpoint, host, header);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
