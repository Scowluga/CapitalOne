import javafx.util.Pair;

import java.io.FileNotFoundException;

public class PythonStyleCounter extends AbstractCounter {

    // Constructor
    public PythonStyleCounter(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    // if the last line was a multi-line comment
    protected int multiLineCounter = 0;

    /*

    0: If found, start
    1: If still, decrease damage from 0

    If not found, stop


     */


    protected boolean isMultiLine = false;
    protected String multiLineEnd = "";

    @Override
    protected void nextLine(String line) {
        nTotalLines++;

        if (line.isEmpty()) {
            multiLineCounter = 0;
            return;
        }

        // Check if line starts with comment
        if (line.trim().substring(0, 1).equals("#")) {
            nTODOs += hasTODO(line) ? 1 : 0;
            switch(multiLineCounter) {
                case 0:
                    nCommentLines++;
                    nSingleComments++;
                    multiLineCounter = 1;
                    return;
                case 1:
                    nCommentLines++;
                    nSingleComments--;
                    nMultiComments++;
                    nMultiCommentLines += 2;
                    multiLineCounter = 2;
                    return;
                default:
                    nCommentLines++;
                    nMultiCommentLines++;
                    return;
            }
        } else {
            multiLineCounter = 0;
        }

        int timeout = 0;
        while (timeout++ < 1000) {
            if (isMultiLine) {
                if (line.contains(multiLineEnd)) {
                    line = line.substring(line.indexOf(multiLineEnd));
                    isMultiLine = false;
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
                        isMultiLine = true;
                        multiLineEnd = "\'\'\'";
                        line = line.substring(next.getValue());
                        break;
                    case 2: // """
                        isMultiLine = true;
                        multiLineEnd = "\"\"\"";
                        line = line.substring(next.getValue());
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
