package networkbook.logic.commands.delete;

import static java.util.Objects.requireNonNull;
import static networkbook.commons.util.CollectionUtil.requireAllNonNull;
import static networkbook.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import networkbook.commons.core.index.Index;
import networkbook.logic.Messages;
import networkbook.logic.commands.Command;
import networkbook.logic.commands.CommandResult;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.Model;
import networkbook.model.person.Person;

/**
 * Class that represents a user command to delete a field of a person.
 */
public class DeleteFieldCommand extends Command {
    public static final String MESSAGE_DELETE_PERSON_FIELD_SUCCESS = "Deleted some information of person: %1$s";
    private Index indexOfPerson;
    private DeleteFieldAction action;

    /**
     * Constructor that creates a new DeleteFieldCommand instance.
     * This command is data-changing, so parent constructor is called with true.
     * @param indexOfPerson
     * @param action
     */
    public DeleteFieldCommand(Index indexOfPerson, DeleteFieldAction action) {
        super(true);
        requireAllNonNull(indexOfPerson, action);
        this.indexOfPerson = indexOfPerson;
        this.action = action;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getDisplayedPersonList();

        if (this.indexOfPerson.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDeleteField = lastShownList.get(this.indexOfPerson.getZeroBased());
        DeletePersonDescriptor descriptor = new DeletePersonDescriptor(personToDeleteField);
        action.delete(descriptor);
        Person personWithFieldDeleted = descriptor.toPerson();

        model.setItem(personToDeleteField, personWithFieldDeleted);
        model.updateDisplayedPersonList(PREDICATE_SHOW_ALL_PERSONS, null);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_FIELD_SUCCESS,
                Messages.format(personWithFieldDeleted)));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof DeleteFieldCommand)) {
            return false;
        }

        DeleteFieldCommand otherCommand = (DeleteFieldCommand) object;
        return this.indexOfPerson.equals(otherCommand.indexOfPerson)
                && this.action.equals(otherCommand.action);
    }
}
