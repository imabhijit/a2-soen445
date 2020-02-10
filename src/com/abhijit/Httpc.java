package com.abhijit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class Httpc {
    static String host = "httpbin.org";
    static RequestType requestType = RequestType.POST;
    static String endpoint = "/post";
    static String header = "Content-Type: application/json";
    static String data = "{\"Assignment\": 1}";
    static boolean verbose = false;
    static HttpcHelp helpWriter = new HttpcHelp();
    static int urlIndex = 1;

    public static void main(String[] args) {

//        TCPClient httpc = new TCPClient(host, 80);

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("help")) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("get")) {
                        helpWriter.printHelpGet();
                    } else if (args[1].equalsIgnoreCase("post")) {
                        helpWriter.printHelpPost();
                    } else {
                        helpWriter.printHelpError();
                    }
                } else {
                    helpWriter.printHelp();
                }
            } else if (args[0].equalsIgnoreCase("get")) {
                processGetRequest(args);
            } else if (args[0].equalsIgnoreCase("post")) {
                processPostRequest(args);
            }

            try {
                URL url = new URL(args[urlIndex]);
                host = url.getHost();
                endpoint = url.getFile();
                TCPClient client = new TCPClient(host, (url.getPort()==-1) ? 80: url.getPort());
                //TODO: add support for headers (-h command)
                client.sendRequest(requestType, endpoint, host, header, data, verbose);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void processGetRequest(String[] args) {
        urlIndex = 1;
        requestType = RequestType.GET;
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("-v")) {
                verbose = true;
                urlIndex += 1;
            }
            if (args[1].equalsIgnoreCase("-h")) {
                if (args.length > 2) {
                    //TODO check if header need to be a list for example if we can have many different headers?
                    header = args[2];
                    urlIndex += 2;
                } else {
                    invalidSyntax(requestType);
                }
            }
        } else {
            invalidSyntax(requestType);
        }
    }

    public static void processPostRequest(String[] args) {

    }

    public static void invalidSyntax(RequestType requestType) {
        System.out.println("Invalid Syntax!");
        if (requestType == RequestType.GET) helpWriter.printHelpGet();
        else if (requestType == RequestType.POST) helpWriter.printHelpPost();
        else helpWriter.printHelpError();
    }
}
