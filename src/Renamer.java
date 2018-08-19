import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Renamer {
    String path;
    JFrame frame;
    Series series;

    public Renamer() {
        this.frame = new MainForm();
        ((MainForm) this.frame).button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAndParseDirectory(e);
            }
        });

    }

    public static void main( final String[] args ) {
        new Renamer();
    }

    public void openAndParseDirectory(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(new File("o:/sorozat/megnezve/sga_s4"));
        JButton button = (JButton) e.getSource();
        MainForm frame = (MainForm) SwingUtilities.getRoot(button);
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            frame.setDirectoryLabel(chooser.getSelectedFile().toString());
            this.path = chooser.getSelectedFile().toString();
            DirectoryParser parser = new DirectoryParser(this.path);
            this.series = parser.processFiles();
            ((MainForm) this.frame).setFileListTableContent(this.series.getTableData());
        }
    }

}
