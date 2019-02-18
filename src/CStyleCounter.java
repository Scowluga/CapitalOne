import javafx.util.Pair;

import java.io.FileNotFoundException;

public class CStyleCounter extends AbstractCounter {

    // Constructor
    public CStyleCounter(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    // If a multi-line comment is going on
    // Used in nextLine implementation
    protected boolean isMultiline = false;

    // Clears single line strings from a line until either comment or new line appears
    // Used in nextLine implementation
    protected String cleanLine(String line) {

        Pair<Integer, Integer> next = nextOccur(line, "//", "/*", "\"", "\'");

        int timeout = 0; // defensive. In case there's a syntax error checked in.
        while (next.getKey() >= 2 && timeout++ < TIMEOUT_ITERATIONS) {
            switch(next.getKey()) {
                case 2:
                    line = line.replaceFirst("[\"].*?[\"]", "");
                    break;
                case 3:
                    line = line.replaceFirst("[\'].*?[\']", "");
                    break;
            }

            next = nextOccur(line, "//", "/*", "\"", "\'");
        }

        return line;
    }

    protected String processLine(String line, boolean isNewLine) {
        Pair<Integer, Integer> next = nextOccur(line, "//", "/*");
        switch(next.getKey()) {
            case 0:  // next: //
                if (isNewLine) nCommentLines++;
                nSingleComments++;
                nTODOs += hasTODO(line.substring(next.getValue())) ? 1 : 0;
                return line;
            case 1:  // next: /*
                isMultiline = true;
                nMultiComments++;
                return line.substring(next.getValue() + 2);
            default: // No comment
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
            // clean line of strings
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
