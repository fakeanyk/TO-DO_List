import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ManagementFunctions {
    private static JPanel taskListPanel;

    private static String selectedItemName;
    private static String selectedItemType;

    public static void getTasksGUI(JPanel tempPanel) {
        taskListPanel = tempPanel;

        selectedItemName = null;
        selectedItemType = null;
        
        Items.init();
        
        taskListPanel.removeAll();
        createButtons("Folder");
        createButtons("Task");
        taskListPanel.repaint();
    }
    private static void createButtons(String type) {
        for (String taskName : Items.get(type)) {
            JPanel taskPanel = new JPanel(new BorderLayout(10, 10));

            ImageIcon icon = getScaledImageIcon("Assets/" + type + "Icon.png");
        
            JButton itemButton = new JButton();
            itemButton.setText(taskName);
            itemButton.setFont(Main.font);

            itemButton.setFocusPainted(false);
            itemButton.setContentAreaFilled(false);
            itemButton.setBorder(null);
            itemButton.addActionListener(_ -> select(itemButton, type));
            
            JButton iconLabel = new JButton();
            iconLabel.setIcon(icon);
            iconLabel.setFocusPainted(false);
            iconLabel.setContentAreaFilled(false);
            iconLabel.setBorder(null);
            iconLabel.addActionListener(_ -> select(itemButton, type));

            taskPanel.add(itemButton, BorderLayout.CENTER);
            taskPanel.add(iconLabel, BorderLayout.WEST);

            taskListPanel.add(taskPanel);
        }
    }
    private static ImageIcon getScaledImageIcon(String path) {
        try {
            URL iconPath = Main.class.getClassLoader().getResource(path);
            if (iconPath != null) {
                BufferedImage img = ImageIO.read(iconPath);
                Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImg);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static void select(JButton itemButton, String itemType) {
        String itemName = itemButton.getText();
        for (Component taskPanel : taskListPanel.getComponents()) {
            Component button = ((Container) taskPanel).getComponent(0);
            button.setForeground(new Color(0, 0, 0));
        }
        if (itemName.equals(selectedItemName) & itemType.equals("Folder")) {
            Main.currentFolderPath.add(itemName);
            selectedItemName = null;
            selectedItemType = null;
            MainPage.refreshFolder();

            return;
        }

        selectedItemName = itemName;
        selectedItemType = itemType;
        itemButton.setForeground(new Color(0, 255, 255));
    }

    public static void add() {
        PopupPage mainPanel = new PopupPage(Main.frame);
        JPanel northPanel = new JPanel(new BorderLayout(10, 10));
        JPanel closeButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JTextField nameEntry = new JTextField();
        nameEntry.setFont(Main.font);

        JButton typeButton = new JButton();
        typeButton.setFont(Main.font);
        typeButton.setFocusPainted(false);
        typeButton.setIcon(getScaledImageIcon("Assets/TaskIcon.png"));
        typeButton.setText("Task");
        typeButton.addActionListener(_ -> {
            switch (typeButton.getText()) {
                case "Task":
                    typeButton.setIcon(getScaledImageIcon("Assets/FolderIcon.png"));
                    typeButton.setText("Folder");
                    break;
                case "Folder":
                    typeButton.setIcon(getScaledImageIcon("Assets/TaskIcon.png"));
                    typeButton.setText("Task");
                    break;
            }
        });

        JButton saveButton = new JButton();
        saveButton.setFont(Main.font);
        saveButton.setText("Done");
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(_ -> {
            String name = nameEntry.getText();
            String type = typeButton.getText();
            Items.add(type, name, null);
            MainPage.refreshFolder();

            mainPanel.closeWindow();
        });
        closeButtonsPanel.add(saveButton);

        JButton discardButton = new JButton();
        discardButton.setFont(Main.font);
        discardButton.setText("Discard");
        discardButton.setFocusPainted(false);
        discardButton.addActionListener(_ -> mainPanel.closeWindow());
        closeButtonsPanel.add(discardButton);

        northPanel.add(nameEntry, BorderLayout.NORTH);
        northPanel.add(typeButton, BorderLayout.SOUTH);
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(closeButtonsPanel, BorderLayout.SOUTH);

        mainPanel.openWindow();
    }
    public static void remove() {
        if (selectedItemName == null) {
            return;
        }
        Items.remove(selectedItemType, selectedItemName);
        MainPage.refreshFolder();
    }
    public static void modify() {
        if (selectedItemName == null) {
            return;
        }

        PopupPage mainPanel = new PopupPage(Main.frame);
        JPanel northPanel = new JPanel(new BorderLayout(10, 10));
        JPanel closeButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JTextField nameEntry = new JTextField(selectedItemName);
        nameEntry.setFont(Main.font);

        JButton changeButton = new JButton();
        changeButton.setFont(Main.font);
        changeButton.setFocusPainted(false);
        changeButton.setIcon(getScaledImageIcon("Assets/" + selectedItemType + "Icon.png"));
        changeButton.setText(selectedItemType);
        changeButton.addActionListener(_ -> {
            if (changeButton.getText().equals("Task")) {
                changeButton.setIcon(getScaledImageIcon("Assets/FolderIcon.png"));
                changeButton.setText("Folder");
            } else {
                changeButton.setIcon(getScaledImageIcon("Assets/TaskIcon.png"));
                changeButton.setText("Task");
            }
        });

        JButton saveButton = new JButton();
        saveButton.setFont(Main.font);
        saveButton.setText("Done");
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(_ -> {
            String name = nameEntry.getText();
            String type = changeButton.getText();
            Items.modify(selectedItemType, selectedItemName, type, name);
            MainPage.refreshFolder();

            mainPanel.closeWindow();
        });
        closeButtonsPanel.add(saveButton);

        JButton discardButton = new JButton();
        discardButton.setFont(Main.font);
        discardButton.setText("Discard");
        discardButton.setFocusPainted(false);
        discardButton.addActionListener(_ -> mainPanel.closeWindow());
        closeButtonsPanel.add(discardButton);

        northPanel.add(nameEntry, BorderLayout.NORTH);
        northPanel.add(changeButton, BorderLayout.SOUTH);
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(closeButtonsPanel, BorderLayout.SOUTH);

        mainPanel.openWindow();
    }
}