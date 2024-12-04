import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.FileSystems;

import org.json.JSONArray;
import org.json.JSONObject;

class SpaceFileManagement {
    private static File taskFile;
    public static void init() {
        String fileSeparator = FileSystems.getDefault().getSeparator();
        String fileDirectoryPath = System.getProperty("user.home") + fileSeparator + ".anyksoftware" +
        fileSeparator + "todolist";
        File fileDirectory = new File(fileDirectoryPath);
        taskFile = new File(fileDirectoryPath + fileSeparator + "items.json");

        fileDirectory.mkdirs();
        try {
            taskFile.createNewFile();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
            null,
            e.toString(),
            "App failed to run",
            JOptionPane.ERROR_MESSAGE);

            return;
        }
        readFromFile();
    }
    private static void readFromFile() {
        StringBuilder jsonData = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(taskFile))) {
            String line;
                while ((line = reader.readLine()) != null) {
                    jsonData.append(line);
                }
        } catch (IOException e) {
            Main.showError(e.toString());

            return;
        }

        if (jsonData.isEmpty()) {
            Main.fileContents = new JSONObject();
        }
        else {
            Main.fileContents = new JSONObject(jsonData.toString());
        }

        format();
    }

    public static void writeToFile() {
        PrintWriter writer;
        try {
            writer = new PrintWriter(taskFile);
        } catch (FileNotFoundException e) {
            Main.showError(e.toString());

            return;
        }

        writer.print(Main.fileContents.toString());

        writer.close();
    }

    private static void format() {
        if (! Main.fileContents.has("version")) {
            Main.fileContents.put("version", 1);
            Main.fileContents.put("Folder", new JSONObject().put("Root", new JSONObject()
                .put("Folder", new JSONObject()).put("Task", new JSONArray())));
        }
    }
}