package dalu.capitalone.counters;

import dalu.capitalone.AbstractCounter;
import javafx.util.Pair;

import java.io.FileNotFoundException;

/**
 * Completes the Capital One coding challenge
 * For all Python style languages including:
 * Python, Shell Script
 */
public class PythonStyleCounter extends AbstractCounter {

    // Public constructor
    public PythonStyleCounter(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    // The number of multi-line comment lines right before the current line
    protected int multiLineCommentCounter = 0;

    // If currently in a multi-line string
    protected boolean isMultiLineString = false;

    // The corresponding multi-line string end
    protected String multiLineStringEnd = "";

    @Override
    protected void nextLine(String line) {
        nTotalLines++;

        if (line.isEmpty()) {
            multiLineCommentCounter = 0;
            return;
        }

        // Check if line starts with comment
        // This means it could possibly be a multi-line comment
        if (!isMultiLineString && line.trim().substring(0, 1).equals("#")) {
            nTODOs += hasTODO(line) ? 1 : 0;
            nCommentLines++;
            switch (multiLineCommentCounter) {
                case 0: // The first comment
                    // Treat it like a single-line
                    nSingleComments++;
                    multiLineCommentCounter = 1;
                    return;
                case 1: // The second comment
                    // Remove previous treatment as single-line
                    // Then update accordingly
                    nSingleComments--;
                    nMultiComments++;
                    nMultiCommentLines += 2;
                    multiLineCommentCounter = 2;
                    return;
                default:
                    nMultiCommentLines++;
                    return;
            }
        } else {
            multiLineCommentCounter = 0;
        }

        // Continuously remove strings until comment or end of line
        int timeout = 0;
        while (timeout++ < TIMEOUT_ITERATIONS) {
            if (isMultiLineString) {
                // If in a multi-line string, just try to search for the end
                if (line.contains(multiLineStringEnd)) {
                    line = line.substring(line.indexOf(multiLineStringEnd) + 3);
                    isMultiLineString = false;
                } else {
                    break;
                }
            } else {
                // Find the next occurrence
                // Process accordingly
                Pair<Integer, Integer> next = nextOccur(line, "#", "\'\'\'", "\"\"\"", "\'", "\"");
                switch (next.getKey()) {
                    case 0: // #
                        nCommentLines++;
                        nSingleComments++;
                        nTODOs += hasTODO(line.substring(next.getValue())) ? 1 : 0;
                        return;
                    case 1: // '''
                        isMultiLineString = true;
                        multiLineStringEnd = "\'\'\'";
                        line = line.substring(next.getValue() + 3);
                        break;
                    case 2: // """
                        isMultiLineString = true;
                        multiLineStringEnd = "\"\"\"";
                        line = line.substring(next.getValue() + 3);
                        break;
                    case 3: // '
                        line = line.replaceFirst("[\'].*?[\']", "");
                        break;
                    case 4: // "
                        line = line.replaceFirst("[\"].*?[\"]", "");
                        break;
                    default:
                        return;
                }
            }
        }
    }
}
