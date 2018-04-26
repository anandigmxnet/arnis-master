package arniscoach;

import javax.sound.sampled.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Command {
    private String name;
    private final CommandType commandType;
    private final Path audioFile;
    private final CyclicBarrier barrier = new CyclicBarrier(2);

    Command(String name, CommandType commandType, Path audioFile) throws Exception {
        this.name = name;
        this.commandType = commandType;
        this.audioFile = audioFile;
    }

    public void play() throws LineUnavailableException, IOException, UnsupportedAudioFileException, InterruptedException {
        Clip clip = AudioSystem.getClip();
        listenForEndOf(clip);
        clip.open(AudioSystem.getAudioInputStream(audioFile.toFile()));
        clip.start();
        waitForSoundEnd();
        clip.stop();
        clip.drain();
    }

    public String getName() {
        return name;
    }

    public int id() {
      return audioFile.hashCode();
    }

    public CommandType getCommandType() {
        return commandType;
    }

    private void waitForSoundEnd() {
        waitOnBarrier();
    }

    private void listenForEndOf(final Clip clip) {
        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) waitOnBarrier();
        });
    }

    private void waitOnBarrier() {
        try {

            barrier.await();
        }
        catch (final InterruptedException ignored) {
        }
        catch (final BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return ((Command)obj).id() == id();
    }
}