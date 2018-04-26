package arniscoach;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static arniscoach.CommandFactory.toCommands;
import static arniscoach.CommandType.SINAWALI;
import static arniscoach.CommandType.STEP;
import static arniscoach.FileCollector.allFilesFrom;

public class SinawaliTraining implements Program {

    private String name = "Sinawali";

    private List<Command> commands;

    private List<Command> lastCommands = new ArrayList<>();

    private Command lastCommand;

    private Random random = new Random();

    public SinawaliTraining() throws Exception {
        prepare();
    }

    private void prepare() throws Exception {
        if (commands == null) {
            commands = new ArrayList<>();
            commands.addAll(toCommands(SINAWALI, allFilesFrom(SINAWALI.getCommandFolder())));
            commands.addAll(toCommands(STEP, allFilesFrom(STEP.getCommandFolder())));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public Command getNext() {
        if (lastCommands.isEmpty()) {
            lastCommands.addAll(new ArrayList<>(commands));
            lastCommand =  lastCommands.remove(getRandomIndex());
            return lastCommand;
        }
        Command potentiallyNextCommand = lastCommands.get(getRandomIndex());
        while(!isDifferentThenLast(potentiallyNextCommand)) {
            potentiallyNextCommand = lastCommands.get(getRandomIndex());
        }
        lastCommand = potentiallyNextCommand;
        lastCommands.remove(potentiallyNextCommand);
        return lastCommand;
    }

    private int getRandomIndex() {
        return random.nextInt(lastCommands.size());
    }

    private boolean isDifferentThenLast(Command command) {
        return !isSameThenLast(command) && !isOfSameCategoryThenLast(command);
    }

    private boolean isSameThenLast(Command command) {
        return command.id() == lastCommand.id();
    }

    private boolean isOfSameCategoryThenLast(Command command) {
       return command.getCommandType() == lastCommand.getCommandType();
    }


}
