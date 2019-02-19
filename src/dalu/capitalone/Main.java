package dalu.capitalone;

import java.util.Scanner;

/**
 * Completes the Capital One coding challenge
 */
public class Main {

    /**
     * Processes file by building an AbstractCounter
     *
     * @param fileName
     * @throws Exception
     */
    private static void processFile(String fileName) throws Exception {
        AbstractCounter counter = AbstractCounter.build(fileName.trim());
        counter.output();
    }

    /**
     * Java main method to run the program
     * Reads inputs from command line or user
     * Processes each input with <code>processFile</code>
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 0) {
            // Reading from command line
            for (String fileName : args) {
                System.out.println("--------------------------------");
                System.out.println("> Output for file: " + fileName);
                try {
                    processFile(fileName);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            // Reading from user input
            Scanner sc = new Scanner(System.in);
            while (true) {
                // Reading file name
                System.out.println("--------------------------------");
                System.out.println("Enter a file name or 'q' to quit");
                System.out.print("> ");

                String fileName = sc.nextLine().trim();

                // Processing file name
                if (fileName.equals("q")) {
                    System.out.println("Goodbye.");
                    sc.close();
                    return;
                } else {
                    try {
                        processFile(fileName);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
