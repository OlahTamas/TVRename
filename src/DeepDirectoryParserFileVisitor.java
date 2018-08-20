import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class DeepDirectoryParserFileVisitor extends SimpleFileVisitor<Path> {
    public ArrayList<String> files;
    public String filterExpression;

    public DeepDirectoryParserFileVisitor() {
        this.files = new ArrayList<String>();
    }

    public void setFilterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
    }

    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
        if (file.getFileName().toString().toLowerCase().matches(".*".concat(this.filterExpression.toLowerCase()).concat(".*"))) {
            this.files.add(file.toString());
        }
        return FileVisitResult.CONTINUE;
    }

}
