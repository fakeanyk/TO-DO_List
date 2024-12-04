import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

public class Items {
    private static List<String> folders;
    private static List<String> tasks;
    public static void init() {
        folders = new ArrayList<>();
        tasks = new ArrayList<>();
        JSONObject folderContents = getFolderContents();

        folderContents.getJSONObject("Folder").keys().forEachRemaining(folders::add);
        folders.sort(String::compareToIgnoreCase);

        List<Object> tasksObjects = folderContents.getJSONArray("Task").toList();
        for (Object taskObject : tasksObjects) {
            tasks.add(taskObject.toString());
        }
        tasks.sort(String::compareToIgnoreCase);
    }

    public static List<String> get(String type) {
        return switch (type) {
            case "Folder" -> folders;
            case "Task" -> tasks;
            default -> new ArrayList<>();
        };
    }
    public static void add(String type, String name, JSONObject folderContents) {
        if (get(type).contains(name)) {
            Main.showError("Folder with the same name already exits!");
            return;
        }
        if (folderContents == null) {
            folderContents = new JSONObject().put("Folder", new JSONObject()).put("Task", new JSONArray());
        }
        get(type).add(name);
        switch (type) {
            case "Folder":
                getFolderContents().getJSONObject("Folder").put(name, folderContents);
                return;
            case "Task":
                getFolderContents().getJSONArray("Task").put(name);
        }
    }
    public static JSONObject remove(String type, String name) {
        get(type).remove(name);

        switch (type) {
            case "Folder":
                return (JSONObject) getFolderContents().getJSONObject("Folder").remove(name);
            case "Task":
                int index = getFolderContents().getJSONArray("Task").toList().indexOf(name);
                getFolderContents().getJSONArray("Task").remove(index);

                return null;
            default:
                return null;
        }
    }
    public static void modify(String type, String name, String newType, String newName) {
        if (get(newType).contains(newName)) {
            Main.showError(newType + " with the same name already exits!");
            return;
        }
        if (type.equals("Folder") &! newType.equals("Folder")) {
            if (isEmpty(name)) {
                if (! showConfirm("Folder you're modifying isn't empty. "
                + "Everything inside this folder will be deleted.")) {
                    return;
                }
            }
        }
        add(newType, newName, remove(type, name));
    }

    private static Boolean isEmpty(String name) {
        JSONObject folder = getFolderContents().getJSONObject("Folder").getJSONObject(name);
        Boolean noFolders = folder.getJSONObject("Folder").isEmpty();
        Boolean noTasks = folder.getJSONArray("Task").isEmpty();
        return !(noFolders & noTasks);
    }
    private static JSONObject getFolderContents() {
        JSONObject folders = Main.fileContents;

        for (String folderName : Main.currentFolderPath) {
            folders = folders.getJSONObject("Folder").getJSONObject(folderName);
        }
        return folders;
    }
    private static boolean showConfirm(String error) {
        return JOptionPane.showConfirmDialog(
            Main.frame,
            error,
            "An error occurred",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        ) == JOptionPane.OK_OPTION;
    }
}