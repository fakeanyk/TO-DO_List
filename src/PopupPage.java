import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class PopupPage extends JPanel {
    private final JDialog frame;
    private final Container parentFrame;

    public PopupPage(Container temp) {
        parentFrame = temp;
        frame = new JDialog();

        frame.setLayout(new BorderLayout());
        Dimension size = new Dimension(parentFrame.getWidth()*4/5, parentFrame.getHeight()*4/5);
        frame.setSize(size);
        frame.setLocationRelativeTo(parentFrame);
        frame.setUndecorated(true);
        frame.setModal(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "You may have unsaved changes which will distracted if you quit. Are you sure you want to close this window?",
                    "Confirm Close",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );

                if (choice == JOptionPane.YES_OPTION) {
                    closeWindow();
                    Main.frame.dispose();
                }
            }
        });
        frame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                Main.frame.toFront();
                Main.frame.requestFocus();
            }
            @Override
            public void windowLostFocus(WindowEvent e){}
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "ESCAPE");
        this.getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeWindow();
            }
        });

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createLineBorder(Color.gray, 3));
    }

    public void openWindow() {
        setEnabledForAllComponents(parentFrame, false);

        frame.add(this);
        frame.setVisible(true);
    }
    public void closeWindow() {
        setEnabledForAllComponents(parentFrame, true);
        frame.dispose();
    }

    private void setEnabledForAllComponents(Container container, boolean b) {
        for (Component component : container.getComponents()) {
            container.setEnabled(b);
            if (component instanceof Container) {
                setEnabledForAllComponents((Container) component, b);
            }
        }
    }
}