package networkbook.logic.commands.delete;

import java.util.Objects;

import networkbook.commons.core.index.Index;
import networkbook.logic.commands.exceptions.CommandException;


/**
 * Class that represents an action to delete one entry from a person's {@code specialisations}.
 */
public class DeleteSpecialisationAction implements DeleteFieldAction {
    private Index index;

    public DeleteSpecialisationAction(Index index) {
        this.index = index;
    }

    /**
     * Deletes the specialisation from the descriptor.
     * @param descriptor
     */
    @Override
    public void delete(DeletePersonDescriptor descriptor) throws CommandException {
        descriptor.deleteSpecialisation(index);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof DeleteSpecialisationAction)) {
            return false;
        }

        DeleteSpecialisationAction otherAction = (DeleteSpecialisationAction) object;
        return Objects.equals(this.index, otherAction.index);
    }
}
