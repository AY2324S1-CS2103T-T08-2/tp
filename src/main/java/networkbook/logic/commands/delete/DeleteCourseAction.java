package networkbook.logic.commands.delete;

import java.util.Objects;

import networkbook.commons.core.index.Index;
import networkbook.logic.commands.exceptions.CommandException;

/**
 * Class that represents an action to delete one entry from a person's {@code courses}.
 */
public class DeleteCourseAction implements DeleteFieldAction {
    private Index index;

    public DeleteCourseAction(Index index) {
        this.index = index;
    }

    /**
     * Deletes the course from the descriptor.
     * @param descriptor
     */
    @Override
    public void delete(DeletePersonDescriptor descriptor) throws CommandException {
        descriptor.deleteCourse(index);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof DeleteCourseAction)) {
            return false;
        }

        DeleteCourseAction otherAction = (DeleteCourseAction) object;
        return Objects.equals(this.index, otherAction.index);
    }
}
