package arniscoach;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Command {
    private final Path audioFile;

    private final CyclicBarrier barrier = new CyclicBarrier(2);

    Command(Path audioFile) throws Exception {
        this.audioFile = audioFile;
    }


    public void play() throws LineUnavailableException, IOException, UnsupportedAudioFileException, InterruptedException {
        Clip clip = AudioSystem.getClip();
        listenForEndOf(clip);
        clip.open(AudioSystem.getAudioInputStream(audioFile.toFile()));
        clip.start();
        waitForSoundEnd();
        clip.stop();
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

    private void waitForSoundEnd() {
        waitOnBarrier();
    }
}