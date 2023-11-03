package networkbook.logic.commands;

import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {
    private boolean isChangingData;

    public Command(boolean isChangingData) {
        this.isChangingData = isChangingData;
    }

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model) throws CommandException;

    public boolean getIsChangingData() {
        return this.isChangingData;
    }

}
