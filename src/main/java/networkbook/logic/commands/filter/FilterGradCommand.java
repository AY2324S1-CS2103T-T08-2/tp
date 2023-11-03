package networkbook.logic.commands.filter;

import networkbook.commons.util.ToStringBuilder;
import networkbook.logic.commands.CommandResult;
import networkbook.model.Model;
import networkbook.model.person.filter.GradEqualsOneOfPredicate;

/**
 * Filters the list of contacts to contacts that have grad years
 * equal to one of the given years.
 */
public class FilterGradCommand extends FilterCommand {
    public static final String FIELD_NAME = "grad";
    public static final String ALL_NUMBERS = "Grad years must all be whole numbers!";

    public static final String MESSAGE_SUCCESS = "Here is the list of contacts whose grad years equals %1$s:";
    private GradEqualsOneOfPredicate yearsPredicate;
    public FilterGradCommand(GradEqualsOneOfPredicate yearsPredicate) {
        this.yearsPredicate = yearsPredicate;
    }

    /**
     * Executes the FilterCommand object and returns a message to the user.
     *
     * @param model {@code Model} which the command should operate on.
     */
    public CommandResult execute(Model model) {
        assert model != null : "Model should not be null";
        model.updateDisplayedPersonList(yearsPredicate, null);
        String feedback = String.format(MESSAGE_SUCCESS, yearsPredicate.getGradYears()
                .stream()
                .map(integer -> integer.toString())
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
        if (!(other instanceof FilterGradCommand)) {
            return false;
        }

        FilterGradCommand otherFilterCommand = (FilterGradCommand) other;
        return yearsPredicate.equals(otherFilterCommand.yearsPredicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", yearsPredicate)
                .toString();
    }
}
