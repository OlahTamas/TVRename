import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm {
    private JButton button1;
    private JPanel mainPanel;

    public MainForm() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
    }

    public void createForm() {
        JFrame frame = new JFrame("TVRename");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(800, 480));
        JButton button1 = new JButton();
        button1.setText("Nyomj meg");
        button1.setSize(new Dimension(128, 32));
        frame.getContentPane().add(button1);
        frame.setVisible(true);
    }

}
