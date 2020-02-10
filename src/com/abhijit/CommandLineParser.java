package com.abhijit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class CommandLineParser {
    Boolean verbose = false;
    String headers = "";
    String data = "";
    Boolean bodyFromData = false;
    Boolean error = false;
    int i = 0;

    public void updateAttributes(String args[]){
        while(i < args.length){
            if(args[i].equalsIgnoreCase("-v")){
                this.verbose = true;
                i++;
            }

            if(i>=args.length){break;}

            if(args[i].equalsIgnoreCase("-h")){
                i++;
                this.headers = args[i];
                i++;
            }

            if(i>=args.length){break;}

            if(args[i].equalsIgnoreCase("-d")){
                this.bodyFromData = true;
                i++;
                this.data = args[i];
                i++;
            }

            if(i>=args.length){break;}

            if(args[i].equalsIgnoreCase("-f")){
                if(!this.bodyFromData){
                    i++;
                    this.data=convertFileToString(args[i]);
                    i++;
                }else{
                    this.error = true;
                }
            }

            if(i>=args.length){break;}

            i++;
        }
    }

    public static String convertFileToString(String filepath){
        BufferedReader bufferedReader;
        StringBuilder str = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new FileReader(filepath));
            String line = bufferedReader.readLine();
            while (line != null) {
                str.append(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }
}
