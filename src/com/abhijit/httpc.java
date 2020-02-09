package com.abhijit;

import sun.misc.Request;

import java.io.IOException;


public class Httpc {

    public static void main(String[] args) {

        String host = "httpbin.org";
        HttpcHelp helpWriter = new HttpcHelp();

        RequestType getRequestType = RequestType.GET;
        String getEndpoint = "/get?course=networking&assignment=1";

        RequestType requestType = RequestType.POST;
        String endpoint = "/post";
        String header = "Content-Type: application/json";
        String data = "{\"Assignment\": 1}";
        boolean verbose = false;

        TCPClient httpc = new TCPClient(host, 80);


        if(args.length > 0  && args[0].equalsIgnoreCase("help")) {
            if(args.length > 1){
                if(args[1].equalsIgnoreCase("get")){
                    helpWriter.printHelpGet();
                }else if(args[1].equalsIgnoreCase("post")){
                    helpWriter.printHelpPost();
                }else{
                    helpWriter.printHelpError();
                }
            }else{
                helpWriter.printHelp();
            }
        }


//        try {
//            httpc.sendRequest(requestType, endpoint, host, header, data, verbose);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
