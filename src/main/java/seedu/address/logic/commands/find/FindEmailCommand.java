package seedu.address.logic.commands.find;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;
import seedu.address.model.person.EmailContainsKeywordsPredicate;

/**
 * Represents a command to find people by email.
 */
public class FindEmailCommand extends Command {

    public static final String COMMAND_WORD = "find-email";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Find a person by email.\n"
            + "Parameters: "
            + PREFIX_EMAIL + "EMAIL\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_EMAIL + "nknguyentdn@gmail.com";

    private final EmailContainsKeywordsPredicate query;

    public FindEmailCommand(String queryString) {
        this.query = new EmailContainsKeywordsPredicate(queryString);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonListByEmail(this.query);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FindEmailCommand)) {
            return false;
        }

        FindEmailCommand otherFindEmailCommand = (FindEmailCommand) other;
        return this.query.equals(otherFindEmailCommand.query);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("query", query)
                .toString();
    }
}
