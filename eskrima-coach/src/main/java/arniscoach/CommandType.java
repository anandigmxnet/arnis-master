package arniscoach;

public enum CommandType {
    STEP("/home/andre/projects/arnis-master/eskrima-coach/src/main/resources/steps"),
    SINAWALI("/home/andre/projects/arnis-master/eskrima-coach/src/main/resources/sinawali"),
    NUMBERS("/home/andre/projects/arnis-master/eskrima-coach/src/main/resources/numbers");

    private String commandFolder;

    CommandType(String commandFolder) {
        this.commandFolder = commandFolder;
    }

    public String getCommandFolder() {
        return commandFolder;
    }
}
