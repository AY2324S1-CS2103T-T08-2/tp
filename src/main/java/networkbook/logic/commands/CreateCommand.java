package networkbook.logic.commands;

import static java.util.Objects.requireNonNull;

import networkbook.commons.util.ToStringBuilder;
import networkbook.logic.Messages;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.logic.parser.CliSyntax;
import networkbook.model.Model;
import networkbook.model.person.Person;

/**
 * Creates a new contact in the network book.
 */
public class CreateCommand extends Command {

    public static final String COMMAND_WORD = "create";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Creates a new contact in the network book. "
            + "Parameters: "
            + "[" + CliSyntax.PREFIX_NAME + " NAME] "
            + "[" + CliSyntax.PREFIX_PHONE + " PHONE] "
            + "[" + CliSyntax.PREFIX_EMAIL + " EMAIL] "
            + "[" + CliSyntax.PREFIX_LINK + " LINK] "
            + "[" + CliSyntax.PREFIX_GRADUATION + " GRADUATION DATE] "
            + "[" + CliSyntax.PREFIX_COURSE + " COURSE OF STUDY] "
            + "[" + CliSyntax.PREFIX_SPECIALISATION + " SPECIALISATION] "
            + "[" + CliSyntax.PREFIX_TAG + " TAG] "
            + "[" + CliSyntax.PREFIX_PRIORITY + " PRIORITY]...\n"
            + "Example: " + COMMAND_WORD + " "
            + CliSyntax.PREFIX_NAME + " John Doe "
            + CliSyntax.PREFIX_PHONE + " 98765432 "
            + CliSyntax.PREFIX_EMAIL + " johnd@example.com "
            + CliSyntax.PREFIX_TAG + " friends "
            + CliSyntax.PREFIX_TAG + " owesMoney";

    public static final String MESSAGE_SUCCESS = "Noted, created new contact: \n%1$s";
    public static final String MESSAGE_SUCCESS_INDEX = "\nAt index %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This contact already exists in the network book";

    private final Person toAdd;

    /**
     * Creates a CreateCommand to create the specified {@code Person}.
     * This command is data-changing, so parent constructor is called with true.
     */
    public CreateCommand(Person person) {
        super(true);
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd))
            + String.format(MESSAGE_SUCCESS_INDEX, model.getDisplayedPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CreateCommand)) {
            return false;
        }

        CreateCommand otherCreateCommand = (CreateCommand) other;
        return toAdd.equals(otherCreateCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
