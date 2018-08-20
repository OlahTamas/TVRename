import java.nio.file.SimpleFileVisitor;
import java.util.ArrayList;

public class DirectoryParserBase extends SimpleFileVisitor {

    ArrayList<String> videoFileExtensions;
    ArrayList<String> subtitleFileExtensions;

    public static String getFileExtension(String filename) {
        int i = filename.lastIndexOf('.');
        return i > -1 ? filename.substring(i+1) : "";
    }

    protected boolean isFileVideoFile(String filename) {
        String extension = getFileExtension(filename);
        return this.videoFileExtensions.contains(extension);
    }

    protected boolean isFileSubtitleFile(String filename) {
        String extension = getFileExtension(filename);
        return this.subtitleFileExtensions.contains(extension);
    }

    protected void initLists() {
        this.videoFileExtensions = new ArrayList<String>();
        this.videoFileExtensions.add("mkv");
        this.videoFileExtensions.add("mp4");
        this.videoFileExtensions.add("avi");
        this.subtitleFileExtensions = new ArrayList<String>();
        this.subtitleFileExtensions.add("srt");
        this.subtitleFileExtensions.add("sub");
    }

}
