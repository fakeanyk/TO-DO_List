import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONObject;

class Main {
    public static void main(String[] args) {
        System.out.println("Working Directory: " + System.getProperty("user.dir"));

        font = new Font("Serif", Font.PLAIN, 40);

        SpaceFileManagement.init();

        currentFolderPath = new ArrayList<>();
        currentFolderPath.add("Root");

        frame = new JFrame();
        frame.setLayout(new BorderLayout(10, 10));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        URL iconPath = Main.class.getClassLoader().getResource("Assets/appIcon.png");
        if (iconPath != null) {
            ImageIcon icon = new ImageIcon(iconPath);
            frame.setIconImage(icon.getImage());
        }
        frame.setTitle("TO-DO List");
        frame.setSize(new Dimension(1000, 500));
        frame.setMinimumSize(new Dimension(400, 300));
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               SpaceFileManagement.writeToFile();
            }
        });

        MainPage.create();

        frame.setVisible(true);
    }
    public static void showError(String error) {
        JOptionPane.showMessageDialog(
            frame,
            error,
            "An error occurred",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public static JSONObject fileContents;


    public static List<String> currentFolderPath;

    public static JFrame frame;

    public static Font font;
}