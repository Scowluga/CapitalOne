package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {


    String s = " asdf ' ' asdf ";
    String b = ' asdf ';




    // just check NOT cTODO or TODOc or cTODOc
    // if there's a single one, return 1
    // TODOs TODOe aTODO TODO*&^%#@!)(P
    private static int countTODOs(String line) {
        // huh what to do here
        return line.replaceAll("\\W*[tT][oO][dD][oO]\\W*", "").equals(line) ? 0 : 1;
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



        /*TODO */ /*(TODO:&
        TODO**/ /*TODOA */ //

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
        // YOu also have to check for when the indexOfs are not in a quote
        // omg or multiline quote nope we're ignoring that case

        File file = new File("test/" + fileName);
        try {
            Scanner sc = new Scanner(file);
            gl: while (sc.hasNextLine()) {
                String line = sc.nextLine();
                nTotalLines++;

                if (line.isEmpty()) {
                    if (isMultiline) {
                        nCommentLines++;
                        nMultiCommentLines++;
                    }
                    continue;
                }

                if (!isMultiline) {
                    int singleStart = line.indexOf("//");
                    int multiStart = line.indexOf("/*");

                    if (singleStart == -1 && multiStart == -1) {
                        continue;
                    }

                    if (multiStart == -1) {
                        nCommentLines++;
                        nSingleComments++;
                        nTODOs += countTODOs(line.substring(singleStart));

                    } else {
                        isMultiline = true;
                        nMultiComments++;
                        line = line.substring(multiStart + 2);
                    }
                }

                if (isMultiline) {
                    nCommentLines++;
                    nMultiCommentLines++;

                    while (line.contains("*/")) {

                        int multiEnd = line.indexOf("*/");
                        nTODOs += countTODOs(line.substring(0, multiEnd));
                        line = line.substring(multiEnd + 2);
                        isMultiline = false;

                        int singleStart = line.indexOf("//");
                        int multiStart = line.indexOf("/*");

                        if (singleStart == -1 && multiStart == -1) {
                            continue gl;
                        }

                        if (multiStart == -1) {
                            nSingleComments++;
                            nTODOs += countTODOs(line.substring(singleStart));
                            continue gl;

                        } else {
                            isMultiline = true;
                            nMultiComments++;
                            line = line.substring(multiStart + 2);
                        }
                    }

                    nTODOs += countTODOs(line);

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

            if (fileName.substring(0, 1).equals("."))
                continue;

            if (fileName.split("\\.").length != 2)
                continue;

            // Calls respective function
            String extension = fileName.substring(fileName.indexOf("."));


            processFile(fileName);


        }
    }
}
