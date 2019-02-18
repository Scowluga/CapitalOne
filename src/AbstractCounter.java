
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

public abstract class AbstractCounter {

    // Takes in a fileName and generates the corresponding AbstractCounter instance
    // Throws exceptions when the file name isn't supported, or file isn't found
    public static AbstractCounter build(String fileName) throws Exception {
        assert (fileName != null); // defensive

        if (fileName.substring(0, 1).equals("."))
            throw new Exception("File ignored.");
        if (fileName.split("\\.").length != 2)
            throw new Exception("File name invalid.");

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

    // Checks a string for TODOs
    // Compiles pattern once into a static instance for performance
    protected static Pattern todoPattern;
    protected static boolean hasTODO(String line) {
        if (todoPattern == null) {
            todoPattern = Pattern.compile(
                    "^(.*)?\\btodo\\b(.*)?$",
                    Pattern.CASE_INSENSITIVE
            );
        }
        return todoPattern.matcher(line).matches();
    }

    // Searches a line for which value occurs next
    // Returns <Index of Value, Index in Line>
    // Returns <-1, -1> when none are found
    protected static Pair<Integer, Integer> nextOccur(String line, String... values) {

        Pair<Integer, Integer> next = new Pair<>(-1, -1);

        for (int valIndex = 0; valIndex < values.length; valIndex++) {
            int lineIndex = line.indexOf(values[valIndex]);
            if (lineIndex != -1) {
                if (next.getKey() == -1 || lineIndex < next.getValue()) {
                    next = new Pair<>(valIndex, lineIndex);
                }
            }
        }
        return next;
    }

    // Clears single line strings from a line until either comment or new line appears
    // Used in nextLine implementation
    protected String cleanLine(String line) {
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

    // Output variables
    protected int nTotalLines = 0;        // # non-empty lines
    protected int nCommentLines = 0;      // # lines with any type of comment
    protected int nSingleComments = 0;    // # single comments
    protected int nMultiComments = 0;     // # multi comments
    protected int nMultiCommentLines = 0; // # lines multi comments cover
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

    // Protected Constructor
    // Read through file, calling abstract nextLine on each line
    protected AbstractCounter(String fileName) throws FileNotFoundException {

        File file = new File("input_tests/" + fileName);

        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            nextLine(line);
        }

        sc.close();
    }

    // Abstract method, to be implemented by each individual AbstractCounter
    // Updates each output variable accordingly
    protected abstract void nextLine(String line);

    // Output
    public final void output() {
        System.out.println("Total # of lines: " + nTotalLines);
        System.out.println("Total # of comment lines: " + nCommentLines);
        System.out.println("Total # of single line comments: " + nSingleComments);
        System.out.println("Total # of comment lines within block comments: " + nMultiCommentLines);
        System.out.println("Total # of block line comments: " + nMultiComments);
        System.out.println("Total # of TODO's: " + nTODOs);
    }
}