import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.TableView;
import java.awt.*;
import java.util.ArrayList;
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
    private JTabbedPane mainTabbedPanel;
    private JPanel renameTab;
    private JPanel copyTab;
    public JButton copyBrowseButton;
    public JButton copyDestinationBrowseButton;
    public JLabel copySourceLabel;
    public JLabel copyDestinationLabel;
    public JTextField copyFilterField;
    public JButton copySearchButton;
    public JTextArea copySearchResults;
    public JButton copyButton;
    public JButton moveButton;
    public JButton scanEmptyDirectoriesButton;
    public JButton moveEmptyDirectoriesButton;

    public MainForm() {
        this.setTitle("TVRename");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(1024, 720));
        this.mainTabbedPanel = new JTabbedPane();
        this.mainTabbedPanel.setPreferredSize(new Dimension(1020, 716));
        this.renameTab = new JPanel();
        this.copyTab = new JPanel();
        this.mainTabbedPanel.addTab("Rename", this.renameTab);
        this.mainTabbedPanel.addTab("Recursive copy", this.copyTab);
        this.getContentPane().add(this.mainTabbedPanel);

        this.addMainPanel(this.renameTab);
        this.addOptionsPanel(this.renameTab);
        this.addTablePanel(this.renameTab);
        this.addBottomPanel(this.renameTab);

        this.addMainCopyPanel(this.copyTab);

        this.setLayout(new FlowLayout());
        this.setVisible(true);

    }


    private void addTablePanel(Container parent) {
        this.fileListTable = new JTable(new DefaultTableModel());
        this.scrollPane = new JScrollPane(this.fileListTable);
        this.scrollPane.setSize(new Dimension(1000, 350));
        this.scrollPane.setPreferredSize(new Dimension(1000, 400));
        this.fileListTable.setFillsViewportHeight(true);

        parent.add(this.scrollPane);
    }

    private void addBottomPanel(Container parent) {
        this.proceedButton = new JButton();
        this.proceedButton.setText("Proceed");
        this.proceedButton.setEnabled(false);
        this.proceedButton.setSize(new Dimension(128, 32));

        this.bottomPanel = new JPanel();
        this.bottomPanel.setPreferredSize(new Dimension(1000, 64));
        this.bottomPanel.add(proceedButton);
        this.resultLabel = new JLabel();
        this.resultLabel.setPreferredSize(new Dimension(1000, 32));
        this.bottomPanel.add(this.resultLabel);

        parent.add(this.bottomPanel);
    }

    private void addOptionsPanel(Container parent) {
        this.titleUpdateButton = new JButton();
        this.titleUpdateButton.setText("Update series title");
        this.titleUpdateButton.setEnabled(false);
        this.titleUpdateButton.setSize(new Dimension(128, 32));

        this.titleTextField = new JTextField();
        this.titleTextField.setPreferredSize(new Dimension(400, 32));

        this.optionsPanel = new JPanel();
        this.optionsPanel.setSize(1000, 64);
        this.optionsPanel.setPreferredSize(new Dimension(1000, 64));
        this.optionsPanel.add(this.titleTextField);
        this.optionsPanel.add(this.titleUpdateButton);

        parent.add(this.optionsPanel);
    }

    private void addMainPanel(Container parent) {
        this.button1 = new JButton();
        this.button1.setText("Browse");
        this.button1.setSize(new Dimension(128, 32));

        this.label1 = new JLabel();
        this.label1.setText("Select a directory with the Browse button...");

        this.mainPanel = new JPanel();
        this.mainPanel.setSize(1000, 64);
        this.mainPanel.setPreferredSize(new Dimension(1000, 64));
        this.mainPanel.add(this.label1);
        this.mainPanel.add(this.button1);
        parent.add(this.mainPanel);
    }

    private void addMainCopyPanel(Container parent) {
        JPanel panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(1000, 64));
        this.copySourceLabel = new JLabel();
        this.copySourceLabel.setText("Select a directory with the Browse button...");

        this.copyBrowseButton = new JButton();
        this.copyBrowseButton.setText("Browse");
        this.copyBrowseButton.setSize(new Dimension(128, 32));
        panel1.add(this.copySourceLabel);
        panel1.add(this.copyBrowseButton);

        JPanel panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(1000, 64));

        this.copyFilterField = new JTextField();
        this.copyFilterField.setPreferredSize(new Dimension(400, 32));

        this.copySearchButton = new JButton();
        this.copySearchButton.setText("Search this expression recursively");
        this.copySearchButton.setSize(new Dimension(128, 32));
        this.copySearchButton.setEnabled(false);

        panel2.add(this.copyFilterField);
        panel2.add(this.copySearchButton);

        JPanel panel3 = new JPanel();
        this.copySearchResults = new JTextArea();
        //this.copySearchResults.setPreferredSize(new Dimension(950, 300));
        JScrollPane scroll = new JScrollPane(this.copySearchResults);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(new Dimension(950, 300));
        panel3.add(scroll);

        JPanel panel4 = new JPanel();

        this.copyDestinationLabel = new JLabel();
        this.copyDestinationLabel.setText("Select target directory");

        this.copyDestinationBrowseButton = new JButton();
        this.copyDestinationBrowseButton.setText("Browse");
        this.copyDestinationBrowseButton.setSize(new Dimension(128, 32));
        panel4.add(this.copyDestinationLabel);
        panel4.add(this.copyDestinationBrowseButton);
        panel4.setPreferredSize(new Dimension(1000, 64));
        JPanel panel5 = new JPanel();
        panel5.setPreferredSize(new Dimension(1000, 64));

        this.copyButton = new JButton();
        this.copyButton.setText("Copy files");
        this.copyButton.setSize(new Dimension(128, 32));

        this.moveButton = new JButton();
        this.moveButton.setText("Move files");
        this.moveButton.setSize(new Dimension(128, 32));
        this.copyButton.setEnabled(false);
        this.moveButton.setEnabled(false);

        panel5.add(this.copyButton);
        panel5.add(this.moveButton);

        JPanel panel6 = new JPanel();
        panel5.setPreferredSize(new Dimension(1000, 64));
        this.scanEmptyDirectoriesButton = new JButton();
        this.scanEmptyDirectoriesButton.setText("Scan for directories without video files");
        this.moveEmptyDirectoriesButton = new JButton();
        this.moveEmptyDirectoriesButton.setText("Move empty directories to a .trash folder");
        this.moveEmptyDirectoriesButton.setEnabled(false);

        panel6.add(this.scanEmptyDirectoriesButton);
        panel6.add(this.moveEmptyDirectoriesButton);


        this.copyTab.add(panel1);
        this.copyTab.add(panel2);
        this.copyTab.add(panel3);
        this.copyTab.add(panel4);
        this.copyTab.add(panel5);
        this.copyTab.add(panel6);
    }

    public static void main(String[] args) {
    }

    public void setDirectoryLabel(String label) {
        this.label1.setText(label);
    }

    public void setResultLabel(String label) {
        this.resultLabel.setText(label);
    }

    public void setFileListTableContent(ArrayList<String[]> content) {
        FileTableModel tableModel = new FileTableModel(0, 4);
        for (String[] entry: content) {
            Object[] rowObject = {
                    entry[0],
                    entry[1],
                    entry[2],
                    entry[3],
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

    public void enableCopySearchButton() {
        this.copySearchButton.setEnabled(true);
    }

}
