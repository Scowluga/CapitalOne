package dalu.capitalone.counters;

import dalu.capitalone.AbstractCounter;
import javafx.util.Pair;

import java.io.FileNotFoundException;

/**
 * Implementation of AbstractCounter
 * For all C style languages including:
 * Java, TypeScript, C, C++...
 */
public class CStyleCounter extends AbstractCounter {

    /**
     * Public Constructor
     *
     * @param fileName
     * @throws FileNotFoundException
     */
    public CStyleCounter(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    // If currently in a multi-line comment
    private boolean isMultiline = false;

    /**
     * Clears all strings from a line until either a comment or new line appears
     *
     * @param line
     * @return cleaned line
     */
    private String cleanLine(String line) {
        // Checks for next occurrence
        Pair<Integer, Integer> next = nextOccur(line, "//", "/*", "\"", "\'");

        // Continuously replaces strings
        int size = line.length();
        while (next.getKey() >= 2) {
            switch (next.getKey()) {
                case 2:
                    line = line.replaceFirst(DOUBLE_QUOTE_PATTERN, "");
                    break;
                case 3:
                    line = line.replaceFirst(SINGLE_QUOTE_PATTERN, "");
                    break;
            }
            if (line.length() == size) {
                break;
            }
            size = line.length();

            next = nextOccur(line, "//", "/*", "\"", "\'");
        }

        return line;
    }

    /**
     * Updates variables according to specific line
     *
     * @param line
     * @param isNewLine Whether this line is new
     * @return processed line
     */
    private String processLine(String line, boolean isNewLine) {
        // Checks for next occurrence
        Pair<Integer, Integer> next = nextOccur(line, "//", "/*");

        // Processes accordingly
        switch (next.getKey()) {
            case 0:  // Next is a single-line comment
                if (isNewLine) nCommentLines++;
                nSingleComments++;
                nTODOs += hasTODO(line.substring(next.getValue())) ? 1 : 0;
                return line;
            case 1:  // Next is a multi-line comment
                isMultiline = true;
                nMultiComments++;
                return line.substring(next.getValue() + 2);
            default: // No comments. Simply return
                return line;
        }
    }

    @Override
    protected void nextLine(String line) {
        nTotalLines++;

        if (line.isEmpty()) {
            if (isMultiline) {
                nCommentLines++;
                nMultiCommentLines++;
            }
            return;
        }

        if (!isMultiline) {
            // Just clean and process
            // Blocked placed before processing of multi-line comment
            // because of possibility to enter one
            line = cleanLine(line);
            line = processLine(line, true);
        }

        if (isMultiline) {
            nCommentLines++;
            nMultiCommentLines++;

            // Loop until no multi-line comments end on the current line
            while (isMultiline && line.contains("*/")) {

                int multiEnd = line.indexOf("*/");
                nTODOs += hasTODO(line.substring(0, multiEnd)) ? 1 : 0;
                line = line.substring(multiEnd + 2);
                isMultiline = false;

                line = cleanLine(line);
                line = processLine(line, false);
            }

            if (isMultiline)
                nTODOs += hasTODO(line) ? 1 : 0;
        }
    }
}
