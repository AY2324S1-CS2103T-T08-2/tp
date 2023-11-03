package networkbook.logic.commands;

import static java.util.Objects.requireNonNull;

import networkbook.model.Model;
import networkbook.model.NetworkBook;

/**
 * Clears the network book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Noted, cleared all contacts!";

    public ClearCommand() {
        super(true);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setNetworkBook(new NetworkBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
