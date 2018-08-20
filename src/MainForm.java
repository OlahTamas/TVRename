import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.TableView;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainForm extends JFrame{
    public JButton button1;
    private JLabel label1;
    private JLabel resultLabel;
    public JButton proceedButton;
    public JButton titleUpdateButton;
    private JPanel mainPanel;
    private JPanel optionsPanel;
    private JPanel bottomPanel;
    private JTable fileListTable;
    public JTextField titleTextField;
    private JScrollPane scrollPane;
    public JFrame frame;

    public MainForm() {
        this.setTitle("TVRename");
        this.button1 = new JButton();
        this.button1.setText("Browse");
        this.button1.setSize(new Dimension(128, 32));

        this.proceedButton = new JButton();
        this.proceedButton.setText("Proceed");
        this.proceedButton.setEnabled(false);
        this.proceedButton.setSize(new Dimension(128, 32));

        this.titleUpdateButton = new JButton();
        this.titleUpdateButton.setText("Update series title");
        this.titleUpdateButton.setEnabled(false);
        this.titleUpdateButton.setSize(new Dimension(128, 32));

        this.label1 = new JLabel();
        this.label1.setText("Select a directory with the Browse button...");

        this.titleTextField = new JTextField();
        this.titleTextField.setPreferredSize(new Dimension(400, 32));

        this.fileListTable = new JTable(new DefaultTableModel());
        this.scrollPane = new JScrollPane(this.fileListTable);
        this.scrollPane.setSize(new Dimension(1000, 350));
        this.scrollPane.setPreferredSize(new Dimension(1000, 400));
        this.fileListTable.setFillsViewportHeight(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(1024, 720));
        this.mainPanel = new JPanel();
        this.mainPanel.setSize(1000, 64);
        this.mainPanel.setPreferredSize(new Dimension(1000, 64));
        this.mainPanel.add(this.label1);
        this.mainPanel.add(this.button1);

        this.optionsPanel = new JPanel();
        this.optionsPanel.setSize(1000, 64);
        this.optionsPanel.setPreferredSize(new Dimension(1000, 64));
        this.optionsPanel.add(this.titleTextField);
        this.optionsPanel.add(this.titleUpdateButton);

        this.bottomPanel = new JPanel();
        this.bottomPanel.setPreferredSize(new Dimension(1000, 64));
        this.bottomPanel.add(proceedButton);
        this.resultLabel = new JLabel();
        this.resultLabel.setPreferredSize(new Dimension(1000, 32));
        this.bottomPanel.add(this.resultLabel);
        this.getContentPane().add(this.mainPanel);
        this.getContentPane().add(this.optionsPanel);
        this.getContentPane().add(this.scrollPane);
        this.getContentPane().add(this.bottomPanel);

        this.setLayout(new FlowLayout());
        this.setVisible(true);

    }

    public static void main(String[] args) {
    }

    public void setDirectoryLabel(String label) {
        this.label1.setText(label);
    }

    public void setResultLabel(String label) {
        this.resultLabel.setText(label);
    }

    public void setFileListTableContent(HashMap<String, String[]> content) {
        FileTableModel tableModel = new FileTableModel(0, 4);
        for (Map.Entry<String, String[]> entry: content.entrySet()) {
            Object[] rowObject = {
                    entry.getKey(),
                    entry.getValue()[0],
                    entry.getValue()[1],
                    entry.getValue()[2],
            };
            tableModel.addRow(rowObject);
        }
        this.fileListTable.setModel(tableModel);
    }

    public void enableProceedButton() {
        this.proceedButton.setEnabled(true);
    }

    public void enableTitleButton() {
        this.titleUpdateButton.setEnabled(true);
    }

}
