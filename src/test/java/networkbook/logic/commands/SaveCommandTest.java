package networkbook.logic.commands;

import static networkbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import networkbook.model.Model;
import networkbook.model.ModelManager;

public class SaveCommandTest {
    private Model expectedModel = new ModelManager();
    private Model actualModel = new ModelManager();

    @Test
    public void execute_save_success() {
        assertCommandSuccess(new SaveCommand(), actualModel, SaveCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals_test() {
        SaveCommand saveCommand = new SaveCommand();
        assertEquals(saveCommand, saveCommand);
        assertEquals(new SaveCommand(), new SaveCommand());
        assertNotEquals(new SaveCommand(), null);
        assertNotEquals(null, new SaveCommand());
        assertNotEquals(new SaveCommand(), new HelpCommand());
        assertNotEquals(new SaveCommand(), 0.5f);
    }
}
