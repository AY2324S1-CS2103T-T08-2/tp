package networkbook.logic.commands.delete;

import java.util.Objects;

import networkbook.commons.core.index.Index;
import networkbook.logic.commands.exceptions.CommandException;


/**
 * Class that represents an action to delete one entry from a person's {@code tags}.
 */
public class DeleteTagAction implements DeleteFieldAction {
    private Index index;

    public DeleteTagAction(Index index) {
        this.index = index;
    }

    /**
     * Deletes the tag from the descriptor.
     * @param descriptor
     */
    @Override
    public void delete(DeletePersonDescriptor descriptor) throws CommandException {
        descriptor.deleteTag(index);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof DeleteTagAction)) {
            return false;
        }

        DeleteTagAction otherAction = (DeleteTagAction) object;
        return Objects.equals(this.index, otherAction.index);
    }
}
