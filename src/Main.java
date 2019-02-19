
import java.util.Scanner;

/**
 * Receives file names as input from user
 * Outputs corresponding solution
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // Reading file name
            System.out.println("--------------------------------");
            System.out.println("Enter a file name or 'q' to quit");
            System.out.print("> ");

            String fileName = scanner.nextLine().trim();

            // Processing file name
            if (fileName.equals("q")) {
                System.out.println("Goodbye.");
                return;
            } else {
                try {

                    // Completing challenge
                    AbstractCounter counter = AbstractCounter.build(fileName);
                    counter.output();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
