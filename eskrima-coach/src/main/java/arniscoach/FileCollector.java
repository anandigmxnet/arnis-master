package arniscoach;

import static java.nio.file.Files.list;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileCollector {

    public static List<Path> allFilesFrom(String resourceDirectory) throws IOException {
        Path resourceDirectoryPaths = Paths.get(resourceDirectory);
        System.out.println(resourceDirectoryPaths.toAbsolutePath());
        return list(resourceDirectoryPaths.toAbsolutePath()).collect(toList());
    }

}
