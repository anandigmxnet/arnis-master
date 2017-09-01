import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.junit.Test;

import arniscoach.FileCollector;

public class SetupTest {

    @Test
    public void readFiles() throws IOException {
        final List<Path> list = FileCollector.allFilesFrom("src/test/resources");
        list.forEach(path -> System.out.println(path.getFileName()));
    }

    @Test
    public void playAudio() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        final List<Path> list = FileCollector.allFilesFrom("src/test/resources");
        final Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(list.get(0).toFile()));
        clip.start();
    }

    @Test
    public void playList() throws IOException, LineUnavailableException {
        final List<Path> paths = FileCollector.allFilesFrom("src/test/resources");
        paths.
        final Clip clip = AudioSystem.getClip();

        final ArnisCoach arnisCoach = new ArnisCoach("test", paths);
        arnisCoach.start();

    }

    private class ArnisCoach extends Thread {

        private List<Clip> filesToPlay = new LinkedList<>();

        private Random random = new Random();
        private int delayInSec = 1;

        public ArnisCoach(String name, List<Clip> filesToPlay) {
            super(name);
            this.filesToPlay = filesToPlay;
        }

        private Clip chooseRandomClip() {
            int randomIndex = random.nextInt(filesToPlay.size());
            return filesToPlay.get(randomIndex);
        }

        private Clip createCommandFrom(Path path) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
            final Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(path.toFile()));
            return clip;

        }

        public void setDelayInSec(int delayInSec) {
            this.delayInSec = delayInSec;
        }

        @Override
        public void run() {
            final Clip clip = chooseRandomClip();
            try {
                System.out.println("Start clip");
                clip.start();

                while(clip.isActive()) {
                    System.out.println("playing...");
                    //nop
                }
                System.out.println("End clip");

                sleep(delayInSec * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
