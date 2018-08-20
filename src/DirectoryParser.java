import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryParser {
    String path;
    ArrayList<String> videoFiles;
    ArrayList<String> subtitleFiles;

    ArrayList<String> videoFileExtensions;
    ArrayList<String> subtitleFileExtensions;

    public DirectoryParser(String path) {
        this.path = path;
        this.videoFileExtensions = new ArrayList<String>();
        this.videoFileExtensions.add("mkv");
        this.videoFileExtensions.add("mp4");
        this.videoFileExtensions.add("avi");
        this.subtitleFileExtensions = new ArrayList<String>();
        this.subtitleFileExtensions.add("srt");
        this.subtitleFileExtensions.add("sub");
        this.videoFiles = new ArrayList<String>();
        this.subtitleFiles = new ArrayList<String>();
    }

    public Series processFiles() {
        File folder = new File(this.path);
        File[] fileList = folder.listFiles();
        Series result = new Series();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile()) {
                if (isFileVideoFile(fileList[i].toString())) {
                    this.videoFiles.add(fileList[i].toString());
                    Episode episode = new Episode(fileList[i].getName());
                    result.addEpisode(episode);
                }
                if (isFileSubtitleFile(fileList[i].toString())) {
                    this.subtitleFiles.add(fileList[i].getName());
                }
            }
        }
        for (String subtitleFile: this.subtitleFiles) {
            result.addSubtitleFile(subtitleFile);
        }
        result.findSeriesTitle();
        result.sortEpisodesByEpisodeIdentifier();

        return result;
    }

    public static String getFileExtension(String filename) {
        int i = filename.lastIndexOf('.');
        return i > -1 ? filename.substring(i+1) : "";
    }

    private boolean isFileVideoFile(String filename) {
        String extension = getFileExtension(filename);
        return this.videoFileExtensions.contains(extension);
    }

    private boolean isFileSubtitleFile(String filename) {
        String extension = getFileExtension(filename);
        return this.subtitleFileExtensions.contains(extension);
    }
}
