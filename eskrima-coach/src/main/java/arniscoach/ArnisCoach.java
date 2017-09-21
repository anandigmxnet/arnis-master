package arniscoach;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ArnisCoach extends Thread {

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
            command.play();
            try{
                Thread.sleep(delayInSec * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}