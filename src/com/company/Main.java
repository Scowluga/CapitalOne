package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    // so you can't recursive this thing
    // but you can recurse the counting the multi line comments

    // so you can't do this anymore sigh holy shit

    private static int countTODOs(String line) {
        return line.replaceAll("\\W*[tT][oO][dD][oO]\\W*", "").equals(line) ? 0 : 1;
    }

    // Dealing  with Python
    private static void processPyFile(String fileName) {

    }


    // Dealing with // /* */ languages
    private static void processFile(String fileName) {

        int nTotalLines = 0; // # non-empty lines
        int nCommentLines = 0; // # lines with any type of comment
        int nSingleComments = 0; // number of single comments
        int nMultiComments = 0; // number of multi comments
        int nMultiCommentLines = 0; // number of lines multi comments cover
        int nTODOs = 0; // number of TODO
        boolean isMultiline = false; // if a multi-line comment is going on



        /* TODO: 0 */ /*TODO: 1 */ // wtf this counts as 2
        //TODO
        /*
        **** ** * /// $ % TODO#%( TODO2 TODOa
        * *TODO
        * @TODO
        * >TODO acts like a //. Anything after is automatically a todo unless the multi comment ends
         */

        // ^+.*todo
        // so you can have anything non-character before or after
        // and todo can be upper or lower case
        // toDO or any combination

        // \W[tT][oO][dD][oO]\W

        // Java Regex for anything that's not a character

        File file = new File("test/" + fileName);
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();


                if (line.isEmpty()) {
                    if (isMultiline) nMultiCommentLines++;
                    continue;
                }


                nTotalLines++;

                if (!isMultiline) {
                    int singleStart = line.indexOf("//");
                    int multiStart = line.indexOf("/*");

                    if (singleStart == -1 && multiStart == -1) {
                        continue;
                    }

                    if (multiStart == -1 || singleStart < multiStart) {
                        // single line first
                        nSingleComments++;
                        nCommentLines++;
                        nTODOs += countTODOs(line.substring(singleStart));

                    } else { // multi line first
                        isMultiline = true;
                        line = line.substring(multiStart + 2);
                    }
                }

                if (isMultiline) {


                    nMultiComments++;
                    if (line.contains("*/")) {
                        nTODOs += countTODOs(line.substring(0, line.indexOf("*/")));
                        isMultiline = false;
                    } else {
                        nTODOs += countTODOs(line);
                    }

                    // omg search for another start


                } else {
                }



            }


            System.out.println("Total # of lines: " + nTotalLines);
            System.out.println("Total # of comment lines: " + nCommentLines);
            System.out.println("Total # of single line comments: " + nSingleComments);
            System.out.println("Total # of comment lines within block comments: " + nMultiCommentLines);
            System.out.println("Total # of block line comments: " + nMultiComments);
            System.out.println("Total # of TODO's: " + nTODOs);

            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file: " + e.getMessage());
            return;
        }

    }


    // Main method
    // Continuously reads in file name as input
    // Calls specific functions to process respective extensions
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {

            // Input with Scanner for simplicity
            System.out.println("Enter a file name or 'q' to quit");
            System.out.print("> ");

            String fileName = scanner.nextLine().trim();

            // Processing file name
            if (fileName.equals("q"))
                return;

            if (fileName.length() == 0)
                continue;

            if (fileName.substring(0, 1).equals("."))
                continue;

            if (fileName.split("\\.").length != 2)
                continue;

            // Calls respective function
            String extension = fileName.substring(fileName.indexOf("."));

            if (extension.equals(".py"))
                processPyFile(fileName);
            else if (true) {
                processFile(fileName);
            }

        }
    }
}
