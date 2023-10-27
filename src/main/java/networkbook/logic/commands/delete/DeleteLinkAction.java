package networkbook.logic.commands.delete;

import java.util.Objects;

import networkbook.commons.core.index.Index;
import networkbook.logic.commands.exceptions.CommandException;

/**
 * Class that represents an action to delete one entry from a person's {@code links}.
 */
public class DeleteLinkAction implements DeleteFieldAction {
    private Index index;

    public DeleteLinkAction(Index index) {
        this.index = index;
    }

    /**
     * Deletes the link from the descriptor.
     * @param descriptor
     */
    @Override
    public void delete(DeletePersonDescriptor descriptor) throws CommandException {
        descriptor.deleteLink(index);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof DeleteLinkAction)) {
            return false;
        }

        DeleteLinkAction otherAction = (DeleteLinkAction) object;
        return Objects.equals(this.index, otherAction.index);
    }
}
