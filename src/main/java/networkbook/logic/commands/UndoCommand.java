package networkbook.logic.commands;

import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.Model;

/**
 * Clears the network book.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Noted, last change successfully undone!";
    public static final String MESSAGE_UNDO_DISALLOWED = "There have been no changes to undo.";
    @Override
    public CommandResult execute(Model model) throws CommandException {
        assert model != null : "Model should not be null";
        model.undoNetworkBook();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
