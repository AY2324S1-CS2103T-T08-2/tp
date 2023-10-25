package networkbook.logic.commands.delete;

import static java.util.Objects.requireNonNull;
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
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the network book.";
    private Index indexOfPerson;
    private DeleteFieldAction action;

    /**
     * Constructor that creates a new DeleteFieldCommand instance.
     * @param indexOfPerson
     * @param action
     */
    public DeleteFieldCommand(Index indexOfPerson, DeleteFieldAction action) {
        this.indexOfPerson = indexOfPerson;
        this.action = action;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (this.indexOfPerson.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDeleteField = lastShownList.get(this.indexOfPerson.getZeroBased());
        DeletePersonDescriptor descriptor = new DeletePersonDescriptor(personToDeleteField);
        action.delete(descriptor);
        Person personWithFieldDeleted = descriptor.toPerson();

        if (!personToDeleteField.isSame(personWithFieldDeleted) && model.hasPerson(personWithFieldDeleted)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setItem(personToDeleteField, personWithFieldDeleted);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
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
