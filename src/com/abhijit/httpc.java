package com.abhijit;

import java.io.IOException;

public class httpc {

    public static void main(String[] args) {
        String host = "httpbin.org";

        RequestType getRequestType = RequestType.GET;
        String getEndpoint = "/get?course=networking&assignment=1";

        RequestType requestType = RequestType.POST;
        String endpoint = "/post";
        String header = "Content-Type: application/json";
        String data = "{\"Assignment\": 1}";
        boolean verbose = false;

        TCPClient httpc = new TCPClient(host, 80);

        try {
            httpc.sendRequest(requestType, endpoint, host, header, data, verbose);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
