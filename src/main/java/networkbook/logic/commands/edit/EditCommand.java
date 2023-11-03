package networkbook.logic.commands.edit;

import static networkbook.commons.util.CollectionUtil.requireAllNonNull;
import static networkbook.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import networkbook.commons.core.index.Index;
import networkbook.commons.util.ToStringBuilder;
import networkbook.logic.Messages;
import networkbook.logic.commands.Command;
import networkbook.logic.commands.CommandResult;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.logic.parser.CliSyntax;
import networkbook.model.Model;
import networkbook.model.person.Person;

/**
 * Edits the details of an existing person in the network book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: [LIST INDEX OF CONTACT] "
            + "[" + CliSyntax.PREFIX_NAME + " NAME] "
            + "[" + CliSyntax.PREFIX_PHONE + " PHONE] "
            + "[" + CliSyntax.PREFIX_EMAIL + " EMAIL] "
            + "[" + CliSyntax.PREFIX_LINK + " LINK] "
            + "[" + CliSyntax.PREFIX_GRADUATION + " GRADUATION DATE] "
            + "[" + CliSyntax.PREFIX_COURSE + " COURSE OF STUDY] "
            + "[" + CliSyntax.PREFIX_SPECIALISATION + " SPECIALISATION] "
            + "[" + CliSyntax.PREFIX_TAG + " TAG] "
            + "[" + CliSyntax.PREFIX_PRIORITY + " PRIORITY]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + CliSyntax.PREFIX_PHONE + " 91234567 "
            + CliSyntax.PREFIX_EMAIL + " johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the network book.";

    private final Index index;
    private final EditAction editAction;

    /**
     * Constructor that instantiates a new {@code EditCommand} object.
     * This command is data-changing, so parent constructor is called with true.
     * @param index of the person in the filtered person list to edit.
     * @param editAction the action to edit the person.
     */
    public EditCommand(Index index, EditAction editAction) {
        super(true);
        requireAllNonNull(index, editAction);

        this.index = index;
        this.editAction = editAction;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        assert model != null : "Model should not be null";
        List<Person> lastShownList = model.getDisplayedPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor(personToEdit);
        this.editAction.edit(editPersonDescriptor);
        Person editedPerson = editPersonDescriptor.toPerson();
        assert editedPerson != null : "Edited person should not be null";

        if (!personToEdit.isSame(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setItem(personToEdit, editedPerson);
        model.updateDisplayedPersonList(PREDICATE_SHOW_ALL_PERSONS, null);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editAction.equals(otherEditCommand.editAction);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editAction", editAction)
                .toString();
    }
}
