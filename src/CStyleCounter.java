import java.io.FileNotFoundException;

public class CStyleCounter extends AbstractCounter {

    // Constructor
    public CStyleCounter(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    // If a multi-line comment is going on
    // Used in nextLine implementation
    boolean isMultiline = false;

    // Clears strings from a line until either a comment, or new line appears
    // Used in nextLine implementation
    static String cleanLine(String line) {
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

            // determine which type of comment starts first
            int singleStart = line.indexOf("//");
            int multiStart = line.indexOf("/*");

            if (singleStart == -1 && multiStart == -1) {
                return;
            }

            if (multiStart == -1) {
                nCommentLines++;
                nSingleComments++;
                nTODOs += hasTODO(line.substring(singleStart)) ? 1 : 0;

            } else {
                isMultiline = true;
                nMultiComments++;
                line = line.substring(multiStart + 2);
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

                // check for what happens next
                int singleStart = line.indexOf("//");
                int multiStart = line.indexOf("/*");

                if (singleStart == -1 && multiStart == -1) {
                    return;
                }

                if (multiStart == -1) {
                    nSingleComments++;
                    nTODOs += hasTODO(line.substring(singleStart)) ? 1 : 0;
                    return;

                } else {
                    isMultiline = true;
                    nMultiComments++;
                    line = line.substring(multiStart + 2);
                }
            }
            nTODOs += hasTODO(line) ? 1 : 0;
        }
    }
}
