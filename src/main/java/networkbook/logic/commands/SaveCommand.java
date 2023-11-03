package networkbook.logic.commands;

import networkbook.model.Model;

/**
 * Class that represents a save command requested by user.
 */
public class SaveCommand extends Command {

    public static final String COMMAND_WORD = "save";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Saves the contact details in a local data file.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Your contact details have been saved locally!\n"
            + "Data is automatically saved after each command, as long as I have write permission to the data file.";

    public SaveCommand() {
        super(true);
    }

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof SaveCommand;
    }
}
