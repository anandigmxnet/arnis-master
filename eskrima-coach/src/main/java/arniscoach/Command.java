package arniscoach;

import java.nio.file.Path;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;

public class Command {
    private final Path audioFile;

    Command(Path audioFile) throws Exception {
        this.audioFile = audioFile;
    }


    public void play() {
        //initialize TinySound
        TinySound.init();
        //load a sound and music
        //note: you can also load with Files, URLs and InputStreams
        Music coin = TinySound.loadMusic(audioFile.toFile());
        System.out.println("start command");
        coin.play(true);
        System.out.println("stop command");

        //be sure to shutdown TinySound when done
        TinySound.shutdown();
    }
}