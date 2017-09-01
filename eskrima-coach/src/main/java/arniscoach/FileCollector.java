package arniscoach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileCollector {

    public static List<Path> allFilesFrom(String resourceDirectory) throws IOException {
        Path resourceDirectoryPaths = Paths.get(resourceDirectory);
        return Files.list(resourceDirectoryPaths).collect(Collectors.toList());
    }

}
