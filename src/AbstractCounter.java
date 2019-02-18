
abstract class AbstractCounter {

    // Output variables
    int nTotalLines = 0; // # non-empty lines
    int nCommentLines = 0; // # lines with any type of comment
    int nSingleComments = 0; // number of single comments
    int nMultiComments = 0; // number of multi comments
    int nMultiCommentLines = 0; // number of lines multi comments cover
    int nTODOs = 0; // number of TODOs
    boolean isMultiline = false; // if a multi-line comment is going on
    
    String extension; 
    String singleCommentStart;
    String multiCommentStart;
    String multiCommentEnd;
    
    AbstractCounter() {
        
    }

    // Counting if the to-do statement exists in a specific string
    // Uses regex to check each possible case
    int countTODOs(String line) {
        if (line.matches("^[tT][oO][dD][oO]\\W.*")
                || line.matches(".*\\W[tT][oO][dD][oO]$")
                || line.matches("^[tT][oO][dD][oO]$")
                || line.matches(".*\\W[tT][oO][dD][oO]\\W.*")) {
            return 1;
        }
        return 0;
    }
    
    // Output
    void output() {
        System.out.println("Total # of lines: " + nTotalLines);
        System.out.println("Total # of comment lines: " + nCommentLines);
        System.out.println("Total # of single line comments: " + nSingleComments);
        System.out.println("Total # of comment lines within block comments: " + nMultiCommentLines);
        System.out.println("Total # of block line comments: " + nMultiComments);
        System.out.println("Total # of TODO's: " + nTODOs);
    }

    abstract void nextLine(String line);
}
