package networkbook.logic.commands.filter;

import networkbook.commons.util.ToStringBuilder;
import networkbook.logic.commands.CommandResult;
import networkbook.model.Model;
import networkbook.model.person.filter.TagsContainKeyTermsPredicate;

/**
 * Filters the list of contacts to contacts that have tags that contain
 * at least one course that contains some specified key terms.
 */
public class FilterTagCommand extends FilterCommand {
    public static final String FIELD_NAME = "tag";
    public static final String MESSAGE_SUCCESS = "Here is the list of contacts whose tags contain %1$s:";
    private TagsContainKeyTermsPredicate keyTermsPredicate;

    public FilterTagCommand(TagsContainKeyTermsPredicate keyTermsPredicate) {
        this.keyTermsPredicate = keyTermsPredicate;
    }

    /**
     * Executes the FilterCommand object and returns a message to the user.
     *
     * @param model {@code Model} which the command should operate on.
     */
    public CommandResult execute(Model model) {
        assert model != null : "Model should not be null";
        model.updateDisplayedPersonList(keyTermsPredicate, null);
        String feedback = String.format(MESSAGE_SUCCESS, keyTermsPredicate.getKeyTerms()
                .stream()
                .reduce("", (acc, term) -> acc + " \"" + term + "\"")
                .trim()
                .replace(" ", ", "));
        return new CommandResult(feedback
                + String.format(MESSAGE_PERSONS_FOUND_OVERVIEW, model.getDisplayedPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterTagCommand)) {
            return false;
        }

        FilterTagCommand otherFilterCommand = (FilterTagCommand) other;
        return keyTermsPredicate.equals(otherFilterCommand.keyTermsPredicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", keyTermsPredicate)
                .toString();
    }
}
