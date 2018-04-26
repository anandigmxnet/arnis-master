package arniscoach;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CommandFactory {

    public static Command createCommand(CommandType commandType, Path path) throws Exception {
        final Command command = new Command(path.getFileName().toString(), commandType, path);
        return command;
    }

    public static List<Command> toCommands(CommandType commandType, List<Path> paths) throws Exception {
        final List<Command> commands = new ArrayList<>();
        for (Path path : paths) {
            commands.add(createCommand(commandType, path));
        }
        return commands;
    }

}
