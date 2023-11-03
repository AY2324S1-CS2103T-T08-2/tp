package networkbook.logic.commands;

import static networkbook.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.util.List;

import networkbook.commons.core.index.Index;
import networkbook.commons.util.ToStringBuilder;
import networkbook.logic.Messages;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.logic.parser.CliSyntax;
import networkbook.model.Model;
import networkbook.model.person.Email;
import networkbook.model.person.Person;

/**
 * Represents a command to open email.
 */
public class OpenEmailCommand extends Command {
    public static final String COMMAND_WORD = "email";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": open email box and send email to a contact\n"
            + "Parameters: [LIST INDEX OF CONTACT]"
            + " [" + CliSyntax.PREFIX_INDEX + " index]\n"
            + "Examples:\n"
            + COMMAND_WORD + " 1\n"
            + COMMAND_WORD + " 1 " + CliSyntax.PREFIX_INDEX + " 1\n";
    public static final String MESSAGE_SUCCESS = "Successfully opened email box to send a new email to %s";
    public static final String MESSAGE_CANNOT_OPEN_EMAIL = "Oops, your computer does not allow me to open email box";
    public static final String MESSAGE_INVALID_EMAIL_INDEX =
            "The person at index %d does not have an email at index %d";

    private final Index personIndex;
    private final Index emailIndex;

    /**
     * Creates a new {@code OpenEmailCommand}.
     * @param personIndex The index of the person to open email.
     * @param emailIndex The index of the email in the email list of the person.
     */
    public OpenEmailCommand(Index personIndex, Index emailIndex) {
        super(false);
        requireAllNonNull(personIndex, emailIndex);
        this.personIndex = personIndex;
        this.emailIndex = emailIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        assert model != null : "Model should not be null.";
        List<Person> lastShownList = model.getDisplayedPersonList();
        if (this.personIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        if (!model.isValidEmailIndex(personIndex, emailIndex)) {
            throw new CommandException(String.format(MESSAGE_INVALID_EMAIL_INDEX,
                    personIndex.getOneBased(), emailIndex.getOneBased()));
        }

        try {
            Email openedEmail = model.openEmail(personIndex, emailIndex);
            return new CommandResult(String.format(MESSAGE_SUCCESS, openedEmail.getValue()));
        } catch (IOException e) {
            throw new CommandException(MESSAGE_CANNOT_OPEN_EMAIL);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof OpenEmailCommand)) {
            return false;
        }

        OpenEmailCommand command = (OpenEmailCommand) object;
        return this.personIndex.equals(command.personIndex)
                && this.emailIndex.equals(command.emailIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personIndex", personIndex)
                .add("emailIndex", emailIndex)
                .toString();
    }
}
