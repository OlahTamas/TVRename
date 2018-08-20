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
        ((MainForm) this.frame).proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                proceedWithRenaming();
            }
        });
        ((MainForm) this.frame).titleUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSeriesTitle();
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
            ((MainForm) this.frame).enableProceedButton();
            ((MainForm) this.frame).enableTitleButton();
            ((MainForm) this.frame).titleTextField.setText(this.series.seriesTitle);

        }
    }

    public void proceedWithRenaming() {
        for (int i = 0; i < this.series.episodes.size(); i++) {
            String pathSeparator = System.getProperty("file.separator");
            Episode episode = this.series.episodes.get(i);
            File videoFile = new File(this.path.concat(pathSeparator).concat(episode.videoFilename));
            String newName = this.series.proposeFilename(i);
            videoFile.renameTo(new File(this.path.concat(pathSeparator).concat(newName)));
            if (episode.subtitleFileName != null) {
                String subtitleExtension = DirectoryParser.getFileExtension(episode.subtitleFileName);
                String newSubtitleName = this.series.proposeFilename(i, subtitleExtension);
                File subtitleFile = new File(this.path.concat(pathSeparator).concat(episode.subtitleFileName));
                subtitleFile.renameTo(new File(this.path.concat(pathSeparator).concat(newSubtitleName)));
            }
        }
        ((MainForm) this.frame).setResultLabel("Operation completed");

    }

    public void updateSeriesTitle() {
        String newTitle = ((MainForm) this.frame).titleTextField.getText();
        this.series.setSeriesTitle(newTitle);
        ((MainForm) this.frame).setFileListTableContent(this.series.getTableData());
    }

}
