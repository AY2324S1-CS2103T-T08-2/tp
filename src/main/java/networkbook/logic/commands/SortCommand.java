package networkbook.logic.commands;

import static java.util.Objects.requireNonNull;

import networkbook.commons.util.ToStringBuilder;
import networkbook.logic.Messages;
import networkbook.logic.parser.CliSyntax;
import networkbook.model.Model;
import networkbook.model.person.PersonSortComparator;

/**
 * Finds and lists all persons in network book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts your list of contacts.\n"
            + "Parameters: "
            + CliSyntax.PREFIX_SORT_FIELD + " FIELD "
            + "[" + CliSyntax.PREFIX_SORT_ORDER + " ORDER]\n"
            + "Example: " + COMMAND_WORD
            + CliSyntax.PREFIX_SORT_FIELD + " grad "
            + CliSyntax.PREFIX_SORT_ORDER + " descending";

    private final PersonSortComparator comparator;

    /**
     * Constructor that instantiates a new {@code SortCommand} object.
     * This command is not data-changing, so parent constructor is called with false.
     * @param comparator
     */
    public SortCommand(PersonSortComparator comparator) {
        super(false);
        this.comparator = comparator;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateDisplayedPersonList(null, comparator);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_SORTED_OVERVIEW, model.getDisplayedPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SortCommand)) {
            return false;
        }

        SortCommand otherSortCommand = (SortCommand) other;
        return comparator.equals(otherSortCommand.comparator);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("comparator", comparator)
                .toString();
    }

}
