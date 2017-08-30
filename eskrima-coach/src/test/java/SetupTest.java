import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class SetupTest {

    @Test
    public void readFolder() {
        Path resourceDirectory = Paths.get("src/test/resources");
        try {
            final List<Path> list = Files.list(resourceDirectory).map(path -> path.getFileName()).collect(Collectors.toList());
            list.forEach(path -> System.out.println(path.getFileName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(resourceDirectory.toAbsolutePath());
    }
}
