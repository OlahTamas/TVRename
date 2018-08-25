import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class DeepDirectoryParser extends DirectoryParserBase {
    private String path;
    private String filterExpression;
    private ArrayList<String> results;

    public DeepDirectoryParser(String path, String filterExpression) {
        this.path = path;
        this.filterExpression = filterExpression;
        this.results = new ArrayList<String>();
        this.initLists();
    }

    public void parseDirectory() throws IOException {
        Path startingPath = Paths.get(this.path);
        DeepDirectoryParserFileVisitor visitor = new DeepDirectoryParserFileVisitor();
        visitor.setFilterExpression(this.filterExpression);
        Files.walkFileTree(startingPath, visitor);
        for (String entry: visitor.files) {
            if (isFileVideoFile(entry)) {
                this.results.add(entry);
            }
        }
    }

    public void parseDirectoryForSubDirectories() throws IOException {
        Path startingPath = Paths.get(this.path);
        DirectoryVisitor visitor = new DirectoryVisitor();
        Files.walkFileTree(startingPath, visitor);
        for (String entry: visitor.directories) {
            this.results.add(entry);
        }

    }

    public ArrayList<String> getResults() {
        return this.results;
    }
}
