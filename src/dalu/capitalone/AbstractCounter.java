package dalu.capitalone;

import dalu.capitalone.counters.CStyleCounter;
import dalu.capitalone.counters.PythonStyleCounter;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Abstract class to complete the Capital One coding challenge
 */
public abstract class AbstractCounter {

    // Max number of iterations for any while-loop
    protected static final int TIMEOUT_ITERATIONS = 1000;

    /**
     * Instantiates and returns an AbstractCounter to complete the challenge
     *
     * @param fileName The file to be processed
     * @return The Corresponding implementation of AbstractCounter
     */
    public static AbstractCounter build(String fileName) throws Exception {
        assert (fileName != null);

        // Checks validity of file name
        if (fileName.substring(0, 1).equals("."))  // Specified to be ignored
            throw new Exception("File ignored.");
        if (fileName.split("\\.").length != 2)     // Too many '.' characters
            throw new Exception("File name invalid.");

        // Returns corresponding implementation
        String extension = fileName.substring(fileName.indexOf("."));
        switch (extension) {
            case ".java": // Java
            case ".ts":   // TypeScript
            case ".cpp":  // C++
                return new CStyleCounter(fileName);
            case ".py":   // Python
            case ".sh":   // Shell
                return new PythonStyleCounter(fileName);
            default:
                throw new Exception("File extension not supported.");
        }
    }

    // Static todo_regex pattern to prevent re-compilation
    protected static Pattern todoPattern;

    /**
     * Searches a line for existence of TODOs using regex
     *
     * @param line The line to search
     * @return A boolean for existence
     */
    protected static boolean hasTODO(String line) {
        if (todoPattern == null) { // Compile if null
            todoPattern = Pattern.compile(
                    "^(.*)?\\btodo\\b(.*)?$",
                    Pattern.CASE_INSENSITIVE
            );
        }
        return todoPattern.matcher(line).matches(); // Check regex match
    }

    /**
     * Searches for the next occurrence of any value in a line
     *
     * @param line   The line to search
     * @param values The values searched for
     * @return Pair(-1, -1) when no values are found
     */
    protected static Pair<Integer, Integer> nextOccur(String line, String... values) {
        Pair<Integer, Integer> next = new Pair<>(-1, -1); // Default pair

        // Check each value's index
        for (int valIndex = 0; valIndex < values.length; valIndex++) {
            int lineIndex = line.indexOf(values[valIndex]);
            if (lineIndex != -1) {
                if (next.getKey() == -1 || lineIndex < next.getValue()) {
                    // Update the returned Pair
                    next = new Pair<>(valIndex, lineIndex);
                }
            }
        }
        return next;
    }

    // Output variables
    protected int nTotalLines = 0;        // # lines
    protected int nCommentLines = 0;      // # lines with any type of comment
    protected int nSingleComments = 0;    // # single comments
    protected int nMultiComments = 0;     // # multi comments
    protected int nMultiCommentLines = 0; // # lines part of a multi comment
    protected int nTODOs = 0;             // # TODOs

    // Getters
    public int getnTotalLines() {
        return nTotalLines;
    }

    public int getnCommentLines() {
        return nCommentLines;
    }

    public int getnSingleComments() {
        return nSingleComments;
    }

    public int getnMultiComments() {
        return nMultiComments;
    }

    public int getnMultiCommentLines() {
        return nMultiCommentLines;
    }

    public int getnTODOs() {
        return nTODOs;
    }

    /**
     * Processes a file line-by-line using Scanner as input
     *
     * @param fileName The file to be processed
     * @throws FileNotFoundException when no such file exists
     */
    protected AbstractCounter(String fileName) throws FileNotFoundException {
        File file = new File("input_tests/" + fileName);

        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            nextLine(line); // Calls abstract nextLine method
        }

        sc.close();
    }

    /**
     * Processes the next line, updating output variables accordingly
     * Abstract, implemented according to each language
     *
     * @param line The input string
     */
    protected abstract void nextLine(String line);

    /**
     * Outputs variables according to format
     */
    public final void output() {
        System.out.println("Total # of lines: " + nTotalLines);
        System.out.println("Total # of comment lines: " + nCommentLines);
        System.out.println("Total # of single line comments: " + nSingleComments);
        System.out.println("Total # of comment lines within block comments: " + nMultiCommentLines);
        System.out.println("Total # of block line comments: " + nMultiComments);
        System.out.println("Total # of TODO's: " + nTODOs);
    }
}