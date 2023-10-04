package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import seedu.address.model.Model;

/**
 * Changes the remark of an existing person in the address book.
 */
public class RemarkCommand extends Command {

    public static final String COMMAND_WORD = "remark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the remark of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing remark will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_REMARK + "[REMARK]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_REMARK + "Likes to swim.";

   public static final String MESSAGE_NOT_IMPLEMENTED_YET = "Sorry! The remark command has not been implemented yet.";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_NOT_IMPLEMENTED_YET);
    }
}