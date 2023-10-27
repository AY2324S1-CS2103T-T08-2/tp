package networkbook.logic.commands.delete;

/**
 * Class that represents an action to delete the {@code graduation} field of a person.
 */
public class DeleteGraduationAction implements DeleteFieldAction {


    /**
     * Deletes the graduation field from the descriptor.
     * @param descriptor
     */
    @Override
    public void delete(DeletePersonDescriptor descriptor) {
        descriptor.deleteGraduation();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof DeleteGraduationAction;
    }
}
