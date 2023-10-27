package networkbook.logic.commands.delete;

import java.util.Objects;

import networkbook.commons.core.index.Index;
import networkbook.logic.commands.exceptions.CommandException;

/**
 * Class that represents an action to delete one entry from a person's {@code emails}.
 */
public class DeleteEmailAction implements DeleteFieldAction {
    private Index index;

    public DeleteEmailAction(Index index) {
        this.index = index;
    }

    /**
     * Deletes the email from the descriptor.
     * @param descriptor
     */
    @Override
    public void delete(DeletePersonDescriptor descriptor) throws CommandException {
        descriptor.deleteEmail(index);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof DeleteEmailAction)) {
            return false;
        }

        DeleteEmailAction otherAction = (DeleteEmailAction) object;
        return Objects.equals(this.index, otherAction.index);
    }
}
