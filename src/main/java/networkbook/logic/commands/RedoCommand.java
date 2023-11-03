package networkbook.logic.commands;

import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.Model;

/**
 * Redoes the last change to the NetworkBook's full list of contacts and/or list of displayed contacts.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_SUCCESS = "Noted, next change successfully redone!";
    public static final String MESSAGE_REDO_DISALLOWED = "There are no changes to redo.";

    public RedoCommand() {
        super(true);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        assert model != null : "Model should not be null";
        model.redoNetworkBook();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
