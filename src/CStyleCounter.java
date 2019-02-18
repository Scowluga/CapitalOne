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

            Pair<Integer, Integer> next = nextOccur(line, "//", "/*");
            switch(next.getKey()) {
                case -1: // Neither
                    return;
                case 0:  // next: //
                    nCommentLines++;
                    nSingleComments++;
                    nTODOs += hasTODO(line.substring(next.getValue())) ? 1 : 0;
                    break;
                case 1:  // next: /*
                    isMultiline = true;
                    nMultiComments++;
                    line = line.substring(next.getValue() + 2);
                    break;
            }
        }

        if (isMultiline) {
            nCommentLines++;
            nMultiCommentLines++;

            // Loop until no multi-line comments end on the current line
            while (line.contains("*/")) {

                int multiEnd = line.indexOf("*/");
                nTODOs += hasTODO(line.substring(0, multiEnd)) ? 1 : 0;
                line = line.substring(multiEnd + 2);
                isMultiline = false;

                // clean the line
                line = cleanLine(line);

                Pair<Integer, Integer> next = nextOccur(line, "//", "/*");
                switch(next.getKey()) {
                    case -1: // Neither
                        return;
                    case 0:  // next: //
                        nSingleComments++;
                        nTODOs += hasTODO(line.substring(next.getValue())) ? 1 : 0;
                        return;
                    case 1:  // next: /*
                        isMultiline = true;
                        nMultiComments++;
                        line = line.substring(next.getValue() + 2);
                        break;
                }
            }
            nTODOs += hasTODO(line) ? 1 : 0;
        }
    }
}
