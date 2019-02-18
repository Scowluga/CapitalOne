import Counters.AbstractCounter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    // The fileName is proper, proceed to processFile
    static final String MESSAGE_SUCCESS = "";
    // The input was "q". Quit.
    static final String MESSAGE_QUIT = "Goodbye.";
    // File begins with ".". To be ignored.
    static final String MESSAGE_IGNORED = "File type ignored.";
    // Too many "."s in the fileName
    static final String MESSAGE_INVALID = "Enter a valid file name.";
    // The extension is currently not supported
    static final String MESSAGE_UNSUPPORTED = "File extension not supported.";

    // Returns message if extension is incorrect
    // Otherwise returns empty string meaning start processing
    static String checkFile(String fileName) {
        if (fileName.equals("q"))
            return MESSAGE_QUIT;

        if (fileName.substring(0, 1).equals("."))
            return MESSAGE_IGNORED;

        if (fileName.split("\\.").length != 2)
            return MESSAGE_INVALID;


        // Change to dictionary
        // <String, AbstractCounter>
        // Create an object of this
        // Moduralize into that

        Set<String> supportedExtensions = new HashSet<>(Arrays.asList(
                ".java",
                ".ts"
        ));

        // Check for a supported extension
        String extension = fileName.substring(fileName.indexOf("."));
        if (!supportedExtensions.contains(extension))
            return MESSAGE_UNSUPPORTED;

        return MESSAGE_SUCCESS;
    }

    // Clears strings from a line until either a comment, or new line appears
    static String cleanLine(String line) {
        int singleStart = line.indexOf("//");
        int multiStart = line.indexOf("/*");
        int stringStart = line.indexOf("\"");
        int charStart = line.indexOf("\'");

        if (singleStart == -1) singleStart = Integer.MAX_VALUE;
        if (multiStart == -1) multiStart = Integer.MAX_VALUE;
        if (stringStart == -1) stringStart = Integer.MAX_VALUE;
        if (charStart == -1) charStart = Integer.MAX_VALUE;

        // A string happens before the next comment
        while ((
                stringStart != Integer.MAX_VALUE
                        && stringStart < Math.min(singleStart, multiStart))
                || (charStart != Integer.MAX_VALUE
                && charStart < Math.min(singleStart, multiStart))) {

            // Remove that string with regex
            if (stringStart < charStart)
                line = line.replaceFirst("[\"].*?[\"]", "");
            else
                line = line.replaceFirst("[\'].*?[\']", "");


            singleStart = line.indexOf("//");
            multiStart = line.indexOf("/*");
            stringStart = line.indexOf("\"");
            charStart = line.indexOf("\'");

            if (singleStart == -1) singleStart = Integer.MAX_VALUE;
            if (multiStart == -1) multiStart = Integer.MAX_VALUE;
            if (stringStart == -1) stringStart = Integer.MAX_VALUE;
            if (charStart == -1) charStart = Integer.MAX_VALUE;
        }

        return line;
    }

    // Processes a specific file name
    static void processFile(String fileName) {

        // File IO
        File file = new File("input_tests/" + fileName);
        try {
            Scanner sc = new Scanner(file);
            gl:
            while (sc.hasNextLine()) {
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
                    // clean line of strings
                    line = cleanLine(line);

                    // determine which type of comment starts first
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

                    // Loop until no multi-line comments end on the current line
                    while (line.contains("*/")) {

                        int multiEnd = line.indexOf("*/");
                        nTODOs += countTODOs(line.substring(0, multiEnd));
                        line = line.substring(multiEnd + 2);
                        isMultiline = false;

                        // clean the line
                        line = cleanLine(line);

                        // check for what happens next
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


            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file: " + e.getMessage());
        }
    }

    // Main method
    // Continuously reads in file name as input
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {

            // Setting up input
            System.out.println("--------------------------------");
            System.out.println("Enter a file name or 'q' to quit");
            System.out.print("> ");

            // Reading input
            String fileName = scanner.nextLine().trim();
            String message = checkFile(fileName);

            switch (message) {
                case MESSAGE_QUIT:
                    System.out.println(message);
                    return;
                case MESSAGE_SUCCESS:
                    processFile(fileName);
                    break;
                default:
                    System.out.println(message);
                    break;
            }
        }
    }
}
