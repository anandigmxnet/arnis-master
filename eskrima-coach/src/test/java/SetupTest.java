import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.junit.Test;

public class SetupTest {

    @Test
    public void readFiles() throws IOException {
        Path resourceDirectory = Paths.get("src/test/resources");
        final List<Path> list = Files.list(resourceDirectory).collect(Collectors.toList());
        list.forEach(path -> System.out.println(path.getFileName()));
        System.out.println(resourceDirectory.toAbsolutePath());
    }

    @Test
    public void playAudio() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        Path resourceDirectory = Paths.get("src/test/resources");
        final List<Path> list = Files.list(resourceDirectory).collect(Collectors.toList());
        final Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(list.get(0).toFile()));
        clip.start();
    }

    public void playList
}
