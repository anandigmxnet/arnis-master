package arniscoach;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ArnisCoach extends Thread {

    private int delayInSec = 1;
    private boolean shoutCommands = true;
    private Program program;

    public ArnisCoach(Program program) {
        super(program.getName());
        this.program = program;
    }

    public void setPauseBetweenCommandsInSec(int delayInSec) {
        this.delayInSec = delayInSec;
    }

    @Override
    public void run() {
        System.out.println("start coach");
        while (shoutCommands) {
            final Command command = program.getNext();
            try {
                command.play();
                Thread.sleep(delayInSec * 1000);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}