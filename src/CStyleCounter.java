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
