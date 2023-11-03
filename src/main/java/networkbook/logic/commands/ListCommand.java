package networkbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static networkbook.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import networkbook.model.Model;

/**
 * Lists all persons in the network book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "Here is your complete list of contacts:"
            + "\n(%1$d persons listed)";

    public ListCommand() {
        super(false);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateDisplayedPersonList(PREDICATE_SHOW_ALL_PERSONS, null);
        return new CommandResult(String.format(MESSAGE_PERSONS_LISTED_OVERVIEW,
                model.getDisplayedPersonList().size()));
    }
}
