import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class DirectoryVisitor extends SimpleFileVisitor<Path> {
    public ArrayList<String> directories;
    public FileIdentifier fileIdentifier;
    protected HashMap<String, Boolean> directoryHasVideoFiles;

    public DirectoryVisitor() {
        this.directories = new ArrayList<String>();
        this.fileIdentifier = new FileIdentifier();
        this.directoryHasVideoFiles = new HashMap<String, Boolean>();
    }

    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException
    {
        if (this.fileIdentifier.isFileVideoFile(file.getFileName().toString())) {
            this.directoryHasVideoFiles.put(file.toAbsolutePath().getParent().toString(), true);
        }
        return FileVisitResult.CONTINUE;
    }


    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr)
        throws IOException
    {
        if (this.directoryHasVideoFiles.get(dir.toAbsolutePath().toString()) == null) {
            this.directoryHasVideoFiles.put(dir.toAbsolutePath().toString(), false);
        }
        return FileVisitResult.CONTINUE;
    }

    public FileVisitResult postVisitDirectory(Path dir, IOException e)
            throws IOException
    {
        if (!this.directoryHasVideoFiles.get(dir.toAbsolutePath().toString())) {
            if (!dir.getFileName().toString().equals(".trash")) {
                this.directories.add(dir.toAbsolutePath().toString());
            }
        } else {
            Path parent = dir.getParent();
            while (parent != null) {
                this.directories.remove(parent);
                this.directoryHasVideoFiles.put(parent.toString(), true);
                parent = parent.getParent();
            }
        }
        return FileVisitResult.CONTINUE;
    }

}
