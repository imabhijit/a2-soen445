package com.abhijit;

import sun.misc.Request;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;


public class Httpc {
    static String host;
    static RequestType requestType;
    static String endpoint;
    static String header;
    static String data;
    static boolean verbose = false;
    static HttpcHelp helpWriter = new HttpcHelp();
    static int urlIndex = 1;
    static boolean output = false;
    static String filepath = "";

    public static void main(String[] args) {

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
                client.setOutput(output);
                client.setFilePath(filepath);
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
            if (args[urlIndex].equalsIgnoreCase("-v")) {
                verbose = true;
                urlIndex += 1;
            }
            if (args[urlIndex].equalsIgnoreCase("-h")) {
                if (args.length > 2) {
                    header = args[urlIndex+1];
                    urlIndex += 2;
                } else {
                    invalidSyntax(requestType);
                }
            }
            if(args[urlIndex + 1].equalsIgnoreCase("-o")){
                output = true;
                filepath = args[urlIndex + 2];
            }
        } else {
            invalidSyntax(requestType);
        }
    }

    public static void processPostRequest(String[] args) {
        requestType = RequestType.POST;

        CommandLineParser clp = new CommandLineParser();
        clp.updateAttributes(args);
        urlIndex = clp.urlIndex;

        if(clp.error){
            invalidSyntax(requestType);
        }

        if(args[urlIndex + 1].equalsIgnoreCase("-o")){
            output = true;
            filepath = args[urlIndex + 2];
        }

        verbose = clp.verbose;
        header = clp.headers;
        data = clp.data;

    }

    public static void invalidSyntax(RequestType requestType) {
        System.out.println("Invalid Syntax!");
        if (requestType == RequestType.GET) helpWriter.printHelpGet();
        else if (requestType == RequestType.POST) helpWriter.printHelpPost();
        else helpWriter.printHelpError();

        System.exit(0);
    }
}
