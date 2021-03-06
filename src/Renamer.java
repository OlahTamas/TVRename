import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Renamer {
    String path;
    String copySourcePath;
    String copyDestinationPath;
    JFrame frame;
    Series series;
    ArrayList<String> filesToBeCopiedOrMoved;
    ArrayList<String> directoriesToBeDeleted;
    ArrayList<String> renameLog;

    public Renamer() {
        this.filesToBeCopiedOrMoved = new ArrayList<String>();
        this.directoriesToBeDeleted = new ArrayList<String>();
        this.renameLog = new ArrayList<String>();
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
                try {
                    proceedWithRenaming();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
            }
        });
        ((MainForm) this.frame).titleUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSeriesTitle();
            }
        });
        ((MainForm) this.frame).copyBrowseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCopySourceDirectory(e);
            }
        });
        ((MainForm) this.frame).copyDestinationBrowseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCopyDestinationDirectory(e);
            }
        });
        ((MainForm) this.frame).copySearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFilesForCopying();
            }
        });
        ((MainForm) this.frame).copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    copyFiles();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        ((MainForm) this.frame).moveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    moveFiles();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        ((MainForm) this.frame).moveEmptyDirectoriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    moveEmptyDirectories();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        ((MainForm) this.frame).scanEmptyDirectoriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchDirectoriesWithoutVideofiles();
            }
        });
        ((MainForm) this.frame).revertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    undoRenameFromLogfile(e);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
//        TVDBConnector tvdbConnector = new TVDBConnector();
//        try {
//            setCurrentStatusDisplay(tvdbConnector.authenticate());
//            setCurrentStatusDisplay(tvdbConnector.searchSeriesByName("Supernatural"));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

    }

    public static void main( final String[] args ) {
        new Renamer();
    }

    public void openAndParseDirectory(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //chooser.setCurrentDirectory(new File("o:/sorozat/megnezve/sga_s4"));
        JButton button = (JButton) e.getSource();
        MainForm frame = (MainForm) SwingUtilities.getRoot(button);
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            setCurrentStatusDisplay("Loading directory");
            frame.setDirectoryLabel(chooser.getSelectedFile().toString());
            this.path = chooser.getSelectedFile().toString();
            DirectoryParser parser = new DirectoryParser(this.path);
            this.series = parser.processFiles();
            ((MainForm) this.frame).setFileListTableContent(this.series.getTableData());
            ((MainForm) this.frame).enableProceedButton();
            ((MainForm) this.frame).enableTitleButton();
            ((MainForm) this.frame).titleTextField.setText(this.series.seriesTitle);
            setCurrentStatusDisplay("Directory loaded");
        }
    }

    public void proceedWithRenaming() throws FileNotFoundException, UnsupportedEncodingException {
        setCurrentStatusDisplay("Processing files");
        for (int i = 0; i < this.series.episodes.size(); i++) {
            String pathSeparator = System.getProperty("file.separator");
            Episode episode = this.series.episodes.get(i);
            File videoFile = new File(this.path.concat(pathSeparator).concat(episode.videoFilename));
            String newName = this.series.proposeFilename(i);
            File newFile = new File(this.path.concat(pathSeparator).concat(newName));
            videoFile.renameTo(newFile);
            this.logRename(videoFile, newFile);
            if (episode.subtitleFileName != null) {
                String subtitleExtension = DirectoryParser.getFileExtension(episode.subtitleFileName);
                String newSubtitleName = this.series.proposeFilename(i, subtitleExtension);
                File subtitleFile = new File(this.path.concat(pathSeparator).concat(episode.subtitleFileName));
                File newSubtitleFile = new File(this.path.concat(pathSeparator).concat(newSubtitleName));
                this.logRename(subtitleFile, newSubtitleFile);
                subtitleFile.renameTo(newSubtitleFile);
            }
        }
        this.saveRenameLog();
        setCurrentStatusDisplay("Operation completed");

    }

    protected void logRename(File oldFile, File newFile) {
        this.renameLog.add(oldFile.getAbsolutePath().concat(" ====> ").concat(newFile.getAbsolutePath()));
    }

    protected String getCurrentOperationLogFileName()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        return this.path.concat(System.getProperty("file.separator"))
            .concat(this.series.seriesTitle)
            .concat("_")
            .concat(formatter.format(Calendar.getInstance().getTime()))
            .concat(".renamelog");

    }

    public void saveRenameLog() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter logfile = new PrintWriter(this.getCurrentOperationLogFileName(), "UTF-8");
        for (String entry: this.renameLog) {
            logfile.println(entry);
        }
        logfile.close();
        this.renameLog = new ArrayList<String>();
    }

    public void updateSeriesTitle() {
        String newTitle = ((MainForm) this.frame).titleTextField.getText();
        this.series.setSeriesTitle(newTitle);
        ((MainForm) this.frame).setFileListTableContent(this.series.getTableData());
    }

    public void setCurrentStatusDisplay(String statusText) {
        ((MainForm) this.frame).setResultLabel(statusText);
    }

    public void setCopySourceDirectory(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //chooser.setCurrentDirectory(new File("d:/Torrent"));
        JButton button = (JButton) e.getSource();
        MainForm frame = (MainForm) SwingUtilities.getRoot(button);
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            this.copySourcePath = chooser.getSelectedFile().toString();
            frame.copySourceLabel.setText(this.copySourcePath);
            frame.enableCopySearchButton();
        }

    }

    public void setCopyDestinationDirectory(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        JButton button = (JButton) e.getSource();
        MainForm frame = (MainForm) SwingUtilities.getRoot(button);
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            this.copyDestinationPath = chooser.getSelectedFile().toString();
            frame.copyDestinationLabel.setText(this.copyDestinationPath);
        }
    }

    public String getFilterExpression() {
        return ((MainForm) this.frame).copyFilterField.getText();
    }

    public void searchFilesForCopying() {
        DeepDirectoryParser parser = new DeepDirectoryParser(this.copySourcePath, this.getFilterExpression());
        try {
            parser.parseDirectory();
            this.showSearchResults(parser.getResults());
            this.filesToBeCopiedOrMoved = parser.getResults();
            ((MainForm) this.frame).copyButton.setEnabled(true);
            ((MainForm) this.frame).moveButton.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSearchResults(ArrayList<String> results) {
        ((MainForm) this.frame).copySearchResults.setText("");
        for (String entry: results) {
            ((MainForm) this.frame).copySearchResults.append(entry);
            ((MainForm) this.frame).copySearchResults.append("\n");
        }
    }

    private void copyFiles() throws IOException {
        String pathSeparator = System.getProperty("file.separator");
        ((MainForm) this.frame).copySearchResults.setRows(0);
        for (String entry: this.filesToBeCopiedOrMoved) {
            File sourceFile = new File(entry);
            File destinationFile = new File(this.copyDestinationPath.concat(pathSeparator).concat(sourceFile.getName()));
            try {
                Files.copy(sourceFile.toPath(), destinationFile.toPath());
            } catch (IOException e) {
                ((MainForm) this.frame).copySearchResults.append("Error copying ".concat(sourceFile.getName()).concat(" to ").concat(destinationFile.getPath()));
                ((MainForm) this.frame).copySearchResults.append("\n");
            }
        }
    }

    private void moveFiles() throws IOException {
        String pathSeparator = System.getProperty("file.separator");
        ((MainForm) this.frame).copySearchResults.setRows(0);
        for (String entry: this.filesToBeCopiedOrMoved) {
            File sourceFile = new File(entry);
            File destinationFile = new File(this.copyDestinationPath.concat(pathSeparator).concat(sourceFile.getName()));
            try {
                Files.move(sourceFile.toPath(), destinationFile.toPath());
            } catch (IOException e) {
                ((MainForm) this.frame).copySearchResults.append("Error moving ".concat(sourceFile.getName()).concat(" to ").concat(destinationFile.getPath()));
                ((MainForm) this.frame).copySearchResults.append("\n");
            }
        }
    }

    public void searchDirectoriesWithoutVideofiles() {
        DeepDirectoryParser parser = new DeepDirectoryParser(this.copySourcePath, this.getFilterExpression());
        try {
            parser.parseDirectoryForSubDirectories();
            for (String entry: parser.getResults()) {
                if (!entry.matches(".*\\.trash.*")) {
                    this.directoriesToBeDeleted.add(entry);
                }
            }
            this.showSearchResults(this.directoriesToBeDeleted);
            ((MainForm) this.frame).moveEmptyDirectoriesButton.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveEmptyDirectories() throws IOException {
        String pathSeparator = System.getProperty("file.separator");
        ((MainForm) this.frame).copySearchResults.setRows(0);
        File trashBase = new File(this.copySourcePath.concat(pathSeparator).concat(".trash"));
        trashBase.mkdirs();
        for (String entry: this.directoriesToBeDeleted) {
            if (!entry.contains(".trash")) {
                File sourceFile = new File(entry);
                String sourceFileFullName = sourceFile.toString().replace(this.copySourcePath.concat(pathSeparator), "");
                File destinationFile = new File(trashBase.toString().concat(pathSeparator).concat(sourceFileFullName));
                try {
                    if (!sourceFile.renameTo(destinationFile)) {
                        throw new IOException("Error");
                    }
                    //Files.move(sourceFile.toPath(), destinationFile.toPath());
                } catch (IOException e) {
                    ((MainForm) this.frame).copySearchResults.append("Error moving ".concat(sourceFile.getName()).concat(" to ").concat(destinationFile.getPath()));
                    ((MainForm) this.frame).copySearchResults.append("\n");
                }
            }
        }

    }

    public void undoRenameFromLogfile(ActionEvent e) throws IOException {
        this.setCurrentStatusDisplay("");
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a directory");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //chooser.setCurrentDirectory(new File("o:/sorozat/megnezve/sga_s4"));
        JButton button = (JButton) e.getSource();
        MainForm frame = (MainForm) SwingUtilities.getRoot(button);
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            this.renameLog = (ArrayList<String>) Files.readAllLines(chooser.getSelectedFile().toPath());
            this.revertRenamesFromRenameLog();
            chooser.getSelectedFile().delete();
        }
    }

    protected void revertRenamesFromRenameLog() {
        for (String entry: this.renameLog) {
            String[] parts = entry.split("\\s\\=\\=\\=\\=\\>\\s");
            File oldFile = new File(parts[0]);
            File newFile = new File(parts[1]);
            boolean result = newFile.renameTo(oldFile);
        }
        this.setCurrentStatusDisplay("Operation reverted, log file deleted");
    }
}
