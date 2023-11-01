package networkbook.logic.commands;

import org.junit.jupiter.api.Test;

import networkbook.model.Model;
import networkbook.model.ModelManager;
import networkbook.model.UserPrefs;
import networkbook.testutil.TypicalPersons;

public class UndoCommandTest {

    @Test
    public void execute_noPreviousStatesStored_failure() {
        Model firstModel = new ModelManager();
        CommandTestUtil.assertCommandFailure(new UndoCommand(), firstModel, UndoCommand.MESSAGE_UNDO_DISALLOWED);
        Model secondModel = new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());
        CommandTestUtil.assertCommandFailure(new UndoCommand(), secondModel, UndoCommand.MESSAGE_UNDO_DISALLOWED);
    }

    @Test
    public void execute_previousStatesStored_success() {
        Model model = new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());
        model.addPerson(TypicalPersons.IDA);
        Model expectedModel = new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());
        CommandTestUtil.assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
