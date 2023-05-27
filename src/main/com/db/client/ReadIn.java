package com.db.client;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class ReadIn {

    public static void initial() {
        try {
            System.out.println("Welcome to Distributed-DB!");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while (true) {
                    int index;
                    String line;
                    StringBuilder input = new StringBuilder();
                    while (true) {
                        line = reader.readLine();
                        if (line.equals("quit")) {
                            System.out.println("See You Again.");
                            return;
                        }
                        if (line.contains(";")) {
                            index = line.indexOf(";");
                            input.append(line, 0, index);
                            break;
                        } else
                            input.append(line);
                    }
                    String query = input.toString().trim().replaceAll("\\s+", " ").replaceAll("’", "'");
                    System.out.println("Your input: " + query + ";");
                    strategy.interpreter(query);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }


}
