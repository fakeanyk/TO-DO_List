import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MainPage {
    private static JPanel taskPanel;
    private static JPanel directoryPanel;
    private static JScrollPane directoryScrollPane;
    public static void create() {
        JPanel northPanel = new JPanel(new BorderLayout(10, 10));
        directoryPanel = new JPanel();
        directoryPanel.setLayout(new BoxLayout(directoryPanel, BoxLayout.X_AXIS));
        directoryScrollPane = new JScrollPane(directoryPanel,
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        directoryScrollPane.setBorder(null);
        directoryScrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scrollDirectoryToRight();
            }
        });

        JPanel functionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        taskPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        JScrollPane taskScrollPane = new JScrollPane(taskPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        taskScrollPane.setBorder(new LineBorder(new Color(0, 0, 0), 3));
        taskScrollPane.getVerticalScrollBar().setUnitIncrement(10);

        JButton addButton = new JButton();
        addButton.setFont(Main.font);
        addButton.setText("Add");
        addButton.setFocusPainted(false);
        addButton.addActionListener(_ -> ManagementFunctions.add());
        functionsPanel.add(addButton);

        JButton removeButton = new JButton();
        removeButton.setFont(Main.font);
        removeButton.setText("Remove");
        removeButton.setFocusPainted(false);
        removeButton.addActionListener(_ -> ManagementFunctions.remove());
        functionsPanel.add(removeButton);

        JButton modifyButton = new JButton();
        modifyButton.setFont(Main.font);
        modifyButton.setText("Modify");
        modifyButton.setFocusPainted(false);
        modifyButton.addActionListener(_ -> ManagementFunctions.modify());
        functionsPanel.add(modifyButton);

        refreshFolder();

        northPanel.add(directoryScrollPane, BorderLayout.NORTH);
        northPanel.add(functionsPanel, BorderLayout.SOUTH);
        Main.frame.add(northPanel, BorderLayout.NORTH);
        Main.frame.add(taskScrollPane, BorderLayout.CENTER);
    }
    public static void refreshFolder() {
        ManagementFunctions.getTasksGUI(taskPanel);
        taskPanel.revalidate();
        directoryPanel.removeAll();
        for (int i = 0; i<Main.currentFolderPath.size(); i++) {
            int index = i;
            JButton directoryButton = new JButton();
            directoryButton.setFocusPainted(false);
            directoryButton.setFont(Main.font);
            directoryButton.setBorder(null);
            directoryButton.setContentAreaFilled(false);
            directoryButton.setText(Main.currentFolderPath.get(index) + " > ");
            directoryButton.addActionListener(_ -> {
                while (Main.currentFolderPath.size()>index+1) {
                    Main.currentFolderPath.removeLast();
                }
                refreshFolder();
            });

            directoryPanel.add(directoryButton);
        }
        directoryPanel.revalidate();
        directoryPanel.repaint();
        SwingUtilities.invokeLater(MainPage::scrollDirectoryToRight);
    }
    private static void scrollDirectoryToRight() {
        JScrollBar scrollBar = directoryScrollPane.getHorizontalScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());
    }
}