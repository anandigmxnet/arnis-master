package arniscoach;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class Training {

    public static void main(String[] args) throws Exception {
        new Training().startTraining();
    }

    public void startTraining() throws Exception {
        final List<Path> list = FileCollector.allFilesFrom("/home/andre/projects/arnis-master/eskrima-coach/src/main/resources");
        final List<Command> commands = createCommands(list);
        ArnisCoach arnisCoach = new ArnisCoach("Andre", commands);
        arnisCoach.setDelayInSec(2);
        arnisCoach.start();
    }

    private List<Command> createCommands(List<Path> audioFiles) throws Exception {
        final List<Command> commands = new LinkedList<>();
        for (Path audioFile : audioFiles) {
            Command command = new Command(audioFile);
            commands.add(command);
        }
        return commands;
    }

}
