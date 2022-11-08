package slavsquatsuperstar.mayonez.io;

import java.io.*;

/**
 * Reads and writes strings from simple text documents.
 *
 * @author SlavSquatSuperstar
 */
public class TextIO {

    /**
     * Reads text data from the specified file.
     *
     * @param path the file location
     * @return the text from the file
     */
    public static String readText(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder input = new StringBuilder();
            for (String textLine = reader.readLine(); textLine != null; textLine = reader.readLine())
                input.append(textLine).append("\n");
            System.out.println("Read successful");
            return input.toString();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File path invalid");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error: Could not read file");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Saves text data to the specified file. Overwrites the previous contents of the file.
     *
     * @param path the file location
     * @param text the text to save
     */
    public static void saveText(String path, String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (String line : text.split("\n")) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Save successful");
        } catch (NullPointerException e) {
            System.out.println("Error: File contents empty");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error: Could not save to file");
            e.printStackTrace();
        }
    }

    /**
     * Appends text data to the specified file. Preserves the previous contents of the file.
     *
     * @param path the file location
     * @param text the text to save
     */
    public static void appendText(String path, String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (String line : text.split("\n")) {
                writer.append(line);
                writer.newLine();
            }
            System.out.println("Save successful");
        } catch (NullPointerException e) {
            System.out.println("Error: File contents empty");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error: Could not save to file");
            e.printStackTrace();
        }
    }

}
