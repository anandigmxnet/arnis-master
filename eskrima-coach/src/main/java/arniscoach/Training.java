package arniscoach;

import static arniscoach.Numbers.*;

public class Training {

    public static void main(String[] args) throws Exception {
        new Training().startTraining();
    }

    public void startTraining() throws Exception {
        //Program program = new SinawaliTraining();
        //Program program = new NumberTraining();
        Program program = new OneToTenCountTraining(DALAWA, TATLO, ANIM, SHIAM, PITO);
        ArnisCoach arnisCoach = new ArnisCoach(program);
        arnisCoach.setPauseBetweenCommandsInSec(1);
        arnisCoach.start();
    }
}
