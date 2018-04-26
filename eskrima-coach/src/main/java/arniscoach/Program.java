package arniscoach;

import java.io.IOException;
import java.util.List;

public interface Program {

    Command getNext();

    String getName();

}
