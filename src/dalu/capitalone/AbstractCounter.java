package dalu.capitalone;

import dalu.capitalone.counters.CStyleCounter;
import dalu.capitalone.counters.PythonStyleCounter;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Abstract class to count required values in a file
 */
public abstract class AbstractCounter {

    /**
     * Map for building AbstractCounter instances
     *
     * Key: File extension
     * Value: Corresponding AbstractCounter implementation
     */
    private static final Map<String, Class<? extends AbstractCounter>> COUNTER_MAP;
    static {
        Map<String, Class<? extends AbstractCounter>> temp = new HashMap<>();

        // Adding currently supported files
        // TODO: Load mapping from external source
        temp.put(".java", CStyleCounter.class);
        temp.put(".ts", CStyleCounter.class);
        temp.put(".c", CStyleCounter.class);
        temp.put(".cpp", CStyleCounter.class);
        temp.put(".cc", CStyleCounter.class);

        temp.put(".py", PythonStyleCounter.class);
        temp.put(".sh", PythonStyleCounter.class);

        // Using unmodifiableMap to ensure COUNTER_MAP remains completely unchanged
        COUNTER_MAP = Collections.unmodifiableMap(temp);
    }

    /**
     * Instantiates and returns an AbstractCounter to complete the challenge
     * Uses COUNTER_MAP
     *
     * @param fileName
     * @return the Corresponding implementation
     * @throws Exception
     */
    public static AbstractCounter build(String fileName) throws Exception {
        assert (fileName != null);

        // Checks validity of file name
        if (fileName.substring(0, 1).equals("."))  // Specified to be ignored
            throw new Exception("File ignored.");
        if (fileName.split("\\.").length < 2)      // No '.' character
            throw new Exception("File name invalid.");

        // Returns corresponding implementation
        String extension = fileName.substring(fileName.lastIndexOf("."));
        Class<? extends AbstractCounter> klass = COUNTER_MAP.get(extension);

        if (klass == null) {
            // File extension not found, thus not supported
            throw new Exception("File extension not supported.");
        }

        // Uses Reflection to call constructor
        return klass.getConstructor(String.class).newInstance(fileName);
    }

    // Static regex pattern for finding TODOs
    // Prevents continuous re-compilation
    private static final Pattern TODO_PATTERN = Pattern.compile(
            "^(.*)?\\btodo\\b(.*)?$",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * Searches a line for existence of TODOs using regex
     *
     * @param line
     * @return boolean for existence
     */
    protected static boolean hasTODO(String line) {
        // Check regex match
        return TODO_PATTERN.matcher(line).matches();
    }

    /**
     * Searches for the next occurrence of any value in a line
     *
     * @param line
     * @param values varargs for what values to search the line for
     * @return Pair(index of value, index in line) for the first value found
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

    // Regex for replacing the next string with double quotes
    protected static final String DOUBLE_QUOTE_PATTERN = "[\"][^\"]*[\"]";
    // Regex for replacing the next string with single quotes
    protected static final String SINGLE_QUOTE_PATTERN = "[\'][^\']*[\']";

    /**
     * Cleans line of all escaped quotes before trying to get rid of strings
     * Called on every inputted line before <code>nextLine</code>
     *
     * @param line
     * @return Cleaned line
     */
    protected static String cleanEscapedQuotes(String line) {
        return line
               .replace("\\\\", "")
               .replace("\\\"", "")
               .replace("\\\'", "");
    }

    // Variables
    protected int nTotalLines = 0;        // # lines
    protected int nCommentLines = 0;      // # lines with any type of comment
    protected int nSingleComments = 0;    // # single comments
    protected int nMultiComments = 0;     // # multi comments
    protected int nMultiCommentLines = 0; // # lines part of a multi comment
    protected int nTODOs = 0;             // # TODOs

    // Getters

    /**
     * @return total number of lines
     */
    public int getnTotalLines() {
        return nTotalLines;
    }

    /**
     * @return total number of lines with any comments
     */
    public int getnCommentLines() {
        return nCommentLines;
    }

    /**
     * @return total number of single-line comments
     */
    public int getnSingleComments() {
        return nSingleComments;
    }

    /**
     * @return total number of multi-line comments
     */
    public int getnMultiComments() {
        return nMultiComments;
    }

    /**
     * @return total number of lines with multi-line comments
     */
    public int getnMultiCommentLines() {
        return nMultiCommentLines;
    }

    /**
     * @return total number of TODOs
     */
    public int getnTODOs() {
        return nTODOs;
    }

    /**
     * Constructor method
     * Processes a file line-by-line by calling <code>nextLine</code>
     *
     * @param fileName
     * @throws FileNotFoundException when no such file exists
     */
    protected AbstractCounter(String fileName) throws FileNotFoundException {
        File file = new File("input_tests/" + fileName);

        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            line = cleanEscapedQuotes(line); // Cleans of escaped quotes
            nextLine(line); // Calls abstract nextLine method
        }

        sc.close();
    }

    /**
     * Processes the next line, updating output variables accordingly
     * Abstract: implementation dependent on language
     *
     * @param line
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