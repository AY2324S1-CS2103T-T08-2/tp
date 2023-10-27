package networkbook.logic.commands.delete;

import networkbook.logic.commands.exceptions.CommandException;

/**
 * Interface that represents an abstract action to delete a field of a contact.
 */
public interface DeleteFieldAction {
    void delete(DeletePersonDescriptor descriptor) throws CommandException;
}
