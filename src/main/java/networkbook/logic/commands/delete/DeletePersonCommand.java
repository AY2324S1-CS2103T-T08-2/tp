package networkbook.logic.commands.delete;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import networkbook.commons.core.index.Index;
import networkbook.commons.util.ToStringBuilder;
import networkbook.logic.Messages;
import networkbook.logic.commands.Command;
import networkbook.logic.commands.CommandResult;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.Model;
import networkbook.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the network book.
 */
public class DeletePersonCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a person or a piece of information about a person.\n"
            + "Usage 1: " + COMMAND_WORD + " [LIST INDEX OF CONTACT]\n"
            + "This deletes the person identified by the index number used in the displayed person list.\n"
            + "Example: " + COMMAND_WORD + " 1\n"
            + "Usage 2: " + COMMAND_WORD + " [LIST INDEX OF CONTACT] [FIELD PREFIX]\n"
            + "Example: " + COMMAND_WORD + " 1 /priority\n"
            + "If the field can have multiple values, /index must be used to specify index of the entry to delete.\n"
            + "Example: " + COMMAND_WORD + " 1 /email /index 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Noted, deleted contact:\n%1$s";
    public static final String MESSAGE_DELETE_PERSON_INDEX = "\nAt index %1$s";

    private final Index targetIndex;

    /**
     * Constructor that instantiates a new {@code DeletePersonCommand} object.
     * This command is data-changing, so parent constructor is called with true.
     * @param targetIndex is the {@code Index} of the person to delete in the displayed list.
     */
    public DeletePersonCommand(Index targetIndex) {
        super(true);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getDisplayedPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deletePerson(personToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete))
                + String.format(MESSAGE_DELETE_PERSON_INDEX, targetIndex.getOneBased()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeletePersonCommand)) {
            return false;
        }

        DeletePersonCommand otherDeletePersonCommand = (DeletePersonCommand) other;
        return Objects.equals(this.targetIndex, otherDeletePersonCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
