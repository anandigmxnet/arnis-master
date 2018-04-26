package arniscoach;

import static arniscoach.CommandFactory.toCommands;
import static arniscoach.CommandType.NUMBERS;
import static arniscoach.FileCollector.allFilesFrom;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OneToTenCountTraining implements Program {

    private String name = "Number";

    private List<Command> commands;

    private List<Command> lastCommands = new ArrayList<>();

    private Command lastCommand;

    private Random random = new Random();

    public OneToTenCountTraining() throws Exception {
        loadAllNumberCommands();
    }

    public OneToTenCountTraining(Numbers... numbers) throws Exception {
        loadAllNumberCommands();
        loadCommandsFor(numbers);
    }

    private void loadCommandsFor(Numbers[] numbers) {
        final List<Command> newCommands = new ArrayList<>();
        for (Numbers number : numbers) {
            newCommands.add(findFor(number));
        }
        commands = newCommands;
    }


    private Command findFor(Numbers number) {
        for (Command command : commands) {
            if (command.getName().contains(number.name().toLowerCase())) {
                return command;
            }
        }
        throw new IllegalArgumentException(format("Command for %s not found.", number.name()));
    }

    private void loadAllNumberCommands() throws Exception {
        if (commands == null) {
            commands = new ArrayList<>();
            commands.addAll(toCommands(NUMBERS, allFilesFrom(NUMBERS.getCommandFolder())));
        }
    }
    @Override
    public String getName() {
        return name;
    }

    public Command getNext() {
        /**
        if (lastCommands.isEmpty()) {
            lastCommands.addAll(new ArrayList<>(commands));
            lastCommand =  lastCommands.remove(getRandomIndex());
            return lastCommand;
        }
        Command potentiallyNextCommand = lastCommands.get(getRandomIndex());
        while(isSameThenLast(potentiallyNextCommand)) {
            potentiallyNextCommand = lastCommands.get(getRandomIndex());
        }
        lastCommand = potentiallyNextCommand;
        lastCommands.remove(potentiallyNextCommand);
        return lastCommand;**/
        return commands.get(getRandomIndex());
    }

    private int getRandomIndex() {
        return random.nextInt(commands.size());
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
