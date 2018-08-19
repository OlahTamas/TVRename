import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.TableView;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainForm extends JFrame{
    public JButton button1;
    private JLabel label1;
    private JPanel mainPanel;
    private JTable fileListTable;
    private JScrollPane scrollPane;
    public JFrame frame;

    public MainForm() {
        this.setTitle("TVRename");
        this.button1 = new JButton();
        this.label1 = new JLabel();
        Object[][] emptyTableData = {};
        this.fileListTable = new JTable(new DefaultTableModel());
        this.fileListTable.setSize(new Dimension(640, 300));
        this.scrollPane = new JScrollPane(this.fileListTable);
        this.fileListTable.setFillsViewportHeight(true);
        this.button1.setText("Browse");
        this.label1.setText("Select a directory with the Browse button...");
        this.button1.setSize(new Dimension(128, 32));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(1024, 480));
        this.getContentPane().add(this.label1);
        this.getContentPane().add(this.button1);
        this.getContentPane().add(this.scrollPane);
        this.setLayout(new FlowLayout());
        this.setVisible(true);

    }

    public static void main(String[] args) {
    }

    public void setDirectoryLabel(String label) {
        this.label1.setText(label);
    }

    private String[] getColumnNamesForFileTable() {
        String[] columnNames = {"Original name", "New name"};

        return columnNames;
    }

    public void setFileListTableContent(HashMap<String, String> content) {
        DefaultTableModel tableModel = new DefaultTableModel(0, 2);
        for (Map.Entry<String, String> entry: content.entrySet()) {
            Object[] rowObject = {entry.getKey(), entry.getValue()};
            tableModel.addRow(rowObject);
        }
        this.fileListTable.setModel(tableModel);
    }

}
