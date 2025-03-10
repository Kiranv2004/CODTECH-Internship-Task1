import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileHandlingUtility {

    // Method to write text to a file
    public static void writeToFile(String filePath, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Replace the special character with a newline
            content = content.replace("|", "\n"); // Replace '|' with actual new line
            String[] lines = content.split("\n");
            for (String line : lines) {
                writer.write(line);
                writer.newLine(); // This adds a new line after each line of content
            }
            System.out.println("Content written to file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // Method to read text from a file
    public static String readFromFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            System.out.println("Content read from file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return content.toString();
    }

    // Method to modify a specific line in a file
    public static void modifyFile(String filePath, int lineNumber, String newContent) {
        try {
            // Read the existing content
            String content = readFromFile(filePath);
            String[] lines = content.split("\n");

            // Modify the specified line
            if (lineNumber >= 0 && lineNumber < lines.length) {
                lines[lineNumber] = newContent;
            } else {
                System.err.println("Line number out of range.");
                return;
            }

            // Write the modified content back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.write("\n");
                }
                System.out.println("File modified: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Error modifying file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueRunning = true; // Control variable for the loop

        while (continueRunning) {
            System.out.println("Choose an option:");
            System.out.println("1: Write to a file");
            System.out.println("2: Read from a file");
            System.out.println("3: Exit");
            System.out.print("Enter your choice (1, 2, or 3): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1: // Case 1: Write to a file
                    System.out.print("Enter the file path: ");
                    String filePath = scanner.nextLine();

                    System.out.println("Enter content to write to the file (type 'END' on a new line to finish):");
                    StringBuilder contentBuilder = new StringBuilder();
                    String line;
                    while (!(line = scanner.nextLine()).equals("END")) {
                        contentBuilder.append(line).append("\n"); // Append each line with a newline
                    }
                    String content = contentBuilder.toString();

                    // Writing to the file
                    writeToFile(filePath, content);
                    break;

                case 2: // Case 2: Read from a file
                    System.out.print("Do you want to read a file? (Enter 'yes' or 'no'): ");
                    String readOption = scanner.nextLine();

                    if (readOption.equalsIgnoreCase("yes")) {
                        System.out.print("Enter the path of the file to read: ");
                        String readFilePath = scanner.nextLine();
                        String readContent = readFromFile(readFilePath);
                        System.out.println("Content of the file:\n" + readContent);
                        
                        // Ask if the user wants to modify the content of the read file
                        System.out.print("Do you want to modify the content of this file? (Enter 'yes' or 'no'): ");
                        String modifyReadFileOption = scanner.nextLine();
                        
                        if (modifyReadFileOption.equalsIgnoreCase("yes")) {
                            System.out.print("Do you want to modify a specific line or all lines? (Enter 'specific' or 'all'): ");
                            String modifyOption = scanner.nextLine();

                            if (modifyOption.equalsIgnoreCase("specific")) {
                                System.out.print("Enter the line number(s) to modify (0-based index, separated by commas, or a range like 1-3): ");
                                String lineNumbersInput = scanner.nextLine();
                                String[] lineNumbers = lineNumbersInput.split(",");

                                for (String lineNumberStr : lineNumbers) {
                                    lineNumberStr = lineNumberStr.trim(); // Trim whitespace
                                    if (lineNumberStr.contains("-")) {
                                        // Handle range input
                                        String[] range = lineNumberStr.split("-");
                                        int start = Integer.parseInt(range[0].trim());
                                        int end = Integer.parseInt(range[1].trim());
                                        System.out.print("Enter new content for lines " + start + " to " + end + ": ");
                                        String newContent = scanner.nextLine();

                                        // Modify all lines in the specified range
                                        for (int i = start; i <= end; i++) {
                                            modifyFile(readFilePath, i, newContent);
                                        }
                                    } else {
                                        // Handle single line input
                                        int lineNumber = Integer.parseInt(lineNumberStr);
                                        System.out.print("Enter new content for line " + lineNumber + ": ");
                                        String newContent = scanner.nextLine();
                                        modifyFile(readFilePath, lineNumber, newContent);
                                    }
                                }
                            } else if (modifyOption.equalsIgnoreCase("all")) {
                                System.out.print("Enter new content for all lines: ");
                                String newContent = scanner.nextLine();

                                // Read the existing content
                                String existingContent = readFromFile(readFilePath);
                                String[] lines = existingContent.split("\n");

                                // Modify all lines with the new content
                                for (int i = 0; i < lines.length; i++) {
                                    modifyFile(readFilePath, i, newContent);
                                }
                            } else {
                                System.out.println("Invalid option. No modifications made.");
                            }

                            // Show the updated content after modification
                            String updatedContent = readFromFile(readFilePath);
                            System.out.println("Updated content of the file:\n" + updatedContent);
                        }
                    }
                    break;

                case 3: // Case 3: Exit
                    continueRunning = false; // Set the control variable to false to exit the loop
                    System.out.println("Exiting the program. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                    break;
            }
        }

        scanner.close();
    }
}
