package networkbook.logic.commands;

import static java.util.Objects.requireNonNull;

import networkbook.commons.util.ToStringBuilder;
import networkbook.model.Model;
import networkbook.model.person.NameContainsKeyTermsPredicate;

/**
 * Finds and lists all persons in network book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    public static final String MESSAGE_SUCCESS = "Here is the list of contacts that contain %1$s:";
    public static final String MESSAGE_PERSONS_FOUND_OVERVIEW = "\n(%1$s contacts found)";

    private final NameContainsKeyTermsPredicate predicate;

    /**
     * Constructor that instantiates a new {@code FindCommand} object.
     * This command is not data-changing, so parent constructor is called with false.
     * @param predicate
     */
    public FindCommand(NameContainsKeyTermsPredicate predicate) {
        super(false);
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateDisplayedPersonList(predicate, null);
        return new CommandResult(
                String.format(MESSAGE_SUCCESS,
                        predicate.getKeyTerms()
                                .stream()
                                .reduce("", (acc, term) -> acc + " \"" + term + "\"")
                                .trim()
                                .replace(" ", ", "))
                        + String.format(MESSAGE_PERSONS_FOUND_OVERVIEW, model.getDisplayedPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
