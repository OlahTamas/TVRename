import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DirectoryParser extends DirectoryParserBase {
    String path;
    ArrayList<String> videoFiles;
    ArrayList<String> subtitleFiles;

    public DirectoryParser(String path) {
        this.path = path;
        this.videoFiles = new ArrayList<String>();
        this.subtitleFiles = new ArrayList<String>();
        this.initLists();
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
}
