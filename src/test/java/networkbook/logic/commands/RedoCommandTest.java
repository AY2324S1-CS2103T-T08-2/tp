package networkbook.logic.commands;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.Model;
import networkbook.model.ModelManager;
import networkbook.model.UserPrefs;
import networkbook.testutil.TypicalPersons;

public class RedoCommandTest {

    @Test
    public void execute_noSubsequentStatesStored_failure() {
        Model firstModel = new ModelManager();
        CommandTestUtil.assertCommandFailure(new RedoCommand(), firstModel, RedoCommand.MESSAGE_REDO_DISALLOWED);
        Model secondModel = new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());
        CommandTestUtil.assertCommandFailure(new RedoCommand(), secondModel, RedoCommand.MESSAGE_REDO_DISALLOWED);
        secondModel.addPerson(TypicalPersons.IDA);
        CommandTestUtil.assertCommandFailure(new RedoCommand(), secondModel, RedoCommand.MESSAGE_REDO_DISALLOWED);
    }

    @Test
    public void execute_subsequentStatesStored_success() {
        Model model = new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());
        model.addPerson(TypicalPersons.IDA);
        try {
            model.undoNetworkBook();
        } catch (CommandException e) {
            fail();
        }
        Model expectedModel = new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());
        expectedModel.addPerson(TypicalPersons.IDA);
        CommandTestUtil.assertCommandSuccess(new RedoCommand(), model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
