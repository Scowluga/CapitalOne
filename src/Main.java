
import java.util.Scanner;

public class Main {

    // Continuously processes file names from user input
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // Reading fileName
            System.out.println("--------------------------------");
            System.out.println("Enter a file name or 'q' to quit");
            System.out.print("> ");

            String fileName = scanner.nextLine().trim();

            // Processing fileName
            if (fileName.equals("q")) {
                System.out.println("Goodbye.");
                return;
            } else {
                try {

                    // Solve the problem
                    AbstractCounter counter = AbstractCounter.build(fileName);
                    counter.output();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
