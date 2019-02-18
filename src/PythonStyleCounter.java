import javafx.util.Pair;

import java.io.FileNotFoundException;

public class PythonStyleCounter extends AbstractCounter {

    // Constructor
    public PythonStyleCounter(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    // if the last line was a multi-line comment
    protected int multiLineCommentCounter = 0;

    protected boolean isMultiLineString = false;
    protected String multiLineStringEnd = "";

    @Override
    protected void nextLine(String line) {
        nTotalLines++;

        if (line.isEmpty()) {
            multiLineCommentCounter = 0;
            return;
        }

        // Check if line starts with comment
        if (!isMultiLineString && line.trim().substring(0, 1).equals("#")) {
            nTODOs += hasTODO(line) ? 1 : 0;
            nCommentLines++;
            switch(multiLineCommentCounter) {
                case 0:
                    nSingleComments++;
                    multiLineCommentCounter = 1;
                    return;
                case 1:
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

        int timeout = 0;
        while (timeout++ < TIMEOUT_ITERATIONS) {
            if (isMultiLineString) {
                if (line.contains(multiLineStringEnd)) {
                    line = line.substring(line.indexOf(multiLineStringEnd) + 3);
                    isMultiLineString = false;
                } else {
                    break;
                }
            } else {
                Pair<Integer, Integer> next = nextOccur(line, "#", "\'\'\'", "\"\"\"", "\'", "\"");
                switch(next.getKey()) {
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
