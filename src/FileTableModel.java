import javax.swing.table.DefaultTableModel;

public class FileTableModel extends DefaultTableModel {
    private final String[] columnNames = {"Original name", "Original subtitle name", "New name", "New subtitle name"};

    public FileTableModel(int rows, int columns) {
        this.setRowCount(rows);
        this.setColumnCount(columns);
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

}
