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
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

public class SetupTest {

    @Test
    public void readFiles() throws IOException {
        final List<Path> list = FileCollector.allFilesFrom("src/test/resources");
        list.forEach(path -> System.out.println(path.getFileName()));
    }

    @Test
    public void tinysound() throws IOException, InterruptedException {
        final List<Path> list = FileCollector.allFilesFrom("src/test/resources");
        TinySound.init();
        //load a sound and music
        //note: you can also load with Files, URLs and InputStreams
        Sound coin = TinySound.loadSound(list.get(0).toFile());
//        Music coin = TinySound.loadMusic(list.get(0).toFile());
        System.out.println("start command");
        coin.play();
//        Thread.sleep(3000);
        System.out.println("stop command");

        //be sure to shutdown TinySound when done
        TinySound.shutdown();
    }

    @Test
    public void playAudio() throws LineUnavailableException, IOException, UnsupportedAudioFileException, InterruptedException {
        final List<Path> list = FileCollector.allFilesFrom("src/test/resources");
        final Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(list.get(1).toFile()));
        clip.addLineListener(new LineListener() {

            @Override
            public void update(LineEvent event) {
                System.out.println(event.getType());
                if (event.getType()==LineEvent.Type.STOP){
                    synchronized(clip){
                        clip.notifyAll();
                    }
                    System.err.println("STOP!");
                }
            }
        });
        clip.start();
        while (true){
            synchronized(clip){
                clip.wait();
            }
            if (!clip.isRunning()){
                break;
            }
        }
    }

    @Test
    public void playCommand() throws Exception {
        final List<Path> list = FileCollector.allFilesFrom("src/test/resources");
        final List<Command> audioFiles = createCommands(list);
        final Command command = audioFiles.get(1);
        command.start();
    }

    @Test
    public void playList() throws Exception {
        final List<Path> list = FileCollector.allFilesFrom("src/test/resources");
        final List<Command> audioFiles = createCommands(list);
        final ArnisCoach arnisCoach = new ArnisCoach("test", audioFiles);
        arnisCoach.setDelayInSec(2);
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
//                final Command command = chooseRandomClip();
                System.out.println("start command");
//                command.start();
                try {
                    sleep(delayInSec * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("stop command");
            }
            System.out.println("stop coach");

        }
    }


    private class Command extends Thread {
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

        @Override
        public void run() {
            try {
                clip.start();
                Thread.currentThread().sleep(clip.getMicrosecondLength());
                audioListener.waitUntilDone();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clip.stop();
        }
    }


    private class AudioListener implements LineListener {
        private boolean done = false;


        @Override public synchronized void update(LineEvent event) {
            LineEvent.Type eventType = event.getType();
            if (eventType == LineEvent.Type.STOP || eventType == LineEvent.Type.CLOSE) {
                System.out.println("done");
                done = true;
                notifyAll();
            }
        }
        public synchronized void waitUntilDone() throws InterruptedException {
            while (!done) {
                System.out.println("wait");
                wait(); }
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
