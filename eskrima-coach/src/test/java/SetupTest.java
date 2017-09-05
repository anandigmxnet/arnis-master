import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
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
    public void playList() throws Exception {
        final List<Path> list = FileCollector.allFilesFrom("src/test/resources");
        final List<Command> audioFiles = createCommands(list);
        final ArnisCoach arnisCoach = new ArnisCoach("test", audioFiles);
        arnisCoach.start();

    }

    private List<Command> createCommands(List<Path> audioFiles) throws Exception {
        final List<Command> commands = new LinkedList<>();
        for (Path audioFile : audioFiles) {
            Command command = new Command(audioFile);
            command.initialize();
            commands.add(command);
        }
        return commands;
    }

    private class ArnisCoach extends Thread {

        private List<Command> commands = new LinkedList<>();

        private Random random = new Random();
        private int delayInSec = 1;
        private boolean shoutCommands = true;

        public ArnisCoach(String name, List<Command> commands) {
            super(name);
            this.commands = commands;
        }

        private Command chooseRandomClip() {
            int randomIndex = random.nextInt(commands.size());
            return commands.get(randomIndex);
        }

        public void setDelayInSec(int delayInSec) {
            this.delayInSec = delayInSec;
        }

        @Override
        public void run() {
            System.out.println("start coach");
            while (shoutCommands) {
                final Command command = chooseRandomClip();
                try {
                    System.out.println("start command");
                    command.execute();
                    System.out.println("stop command");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
            System.out.println("stop coach");

        }
    }


    private class Command {
        private final Path audioFile;
        private final Clip clip;
        private final AudioListener audioListener;

        public Command(Path audioFile) throws Exception {
            this.audioFile = audioFile;
            this.clip = AudioSystem.getClip();;
            this.audioListener = new AudioListener();

        }

        public void initialize() throws Exception {
            final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile.toFile());
            clip.open(audioInputStream);
            clip.addLineListener(audioListener);
        }


        public void execute() throws InterruptedException {
                clip.start();
                audioListener.waitUntilDone();
                clip.stop();
        }
    }


    private class AudioListener implements LineListener {
        private boolean done = false;
        @Override public synchronized void update(LineEvent event) {
            LineEvent.Type eventType = event.getType();
            if (eventType == LineEvent.Type.STOP || eventType == LineEvent.Type.CLOSE) {
                done = true;
                notifyAll();
            }
        }
        public synchronized void waitUntilDone() throws InterruptedException {
            while (!done) { wait(); }
        }
    }
}


//    private static void playClip(File clipFile) throws IOException,
//            UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
//
//        AudioListener listener = new AudioListener();
//        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(clipFile);
//        try {
//            Clip clip = AudioSystem.getClip();
//            clip.addLineListener(listener);
//            clip.open(audioInputStream);
//            try {
//                clip.start();
//                listener.waitUntilDone();
//            } finally {
//                clip.close();
//            }
//        } finally {
//            audioInputStream.close();
//        }
//    }
