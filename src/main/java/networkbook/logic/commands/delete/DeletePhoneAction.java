package networkbook.logic.commands.delete;

import java.util.Objects;

import networkbook.commons.core.index.Index;
import networkbook.logic.commands.exceptions.CommandException;


/**
 * Class that represents an action to delete one entry from a person's {@code phones}.
 */
public class DeletePhoneAction implements DeleteFieldAction {
    private Index index;

    public DeletePhoneAction(Index index) {
        this.index = index;
    }

    /**
     * Deletes the phone from the descriptor.
     * @param descriptor
     */
    @Override
    public void delete(DeletePersonDescriptor descriptor) throws CommandException {
        descriptor.deletePhone(index);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof DeletePhoneAction)) {
            return false;
        }

        DeletePhoneAction otherAction = (DeletePhoneAction) object;
        return Objects.equals(this.index, otherAction.index);
    }
}
