package networkbook.logic.commands.delete;

/**
 * Class that represents an action to delete the {@code priority} field of a person.
 */
public class DeletePriorityAction implements DeleteFieldAction {


    /**
     * Deletes the priority field from the descriptor.
     * @param descriptor
     */
    @Override
    public void delete(DeletePersonDescriptor descriptor) {
        descriptor.deletePriority();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof DeletePriorityAction;
    }
}
