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
import networkbook.model.person.Link;
import networkbook.model.person.Person;

/**
 * Represents a command to open a link attached to a contact.
 */
public class OpenLinkCommand extends Command {
    public static final String COMMAND_WORD = "open";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": open a link of a contact\n"
            + "Parameters: [LIST INDEX OF CONTACT]"
            + " [" + CliSyntax.PREFIX_INDEX + " index]\n"
            + "Examples:\n"
            + COMMAND_WORD + " 1\n"
            + COMMAND_WORD + " 1 " + CliSyntax.PREFIX_INDEX + " 1";
    public static final String MESSAGE_SUCCESS = "Successfully opened %s";
    public static final String MESSAGE_CANNOT_OPEN_LINK = "Oops, your computer does not allow me to open a link.";
    public static final String MESSAGE_INVALID_LINK_INDEX = "The person at index %d does not have a link at index %d.";

    private final Index personIndex;
    private final Index linkIndex;

    /**
     * Creates a new {@code OpenLinkCommand}.
     * This command is not data-changing, so parent constructor is called with false.
     * @param personIndex The index of the person to open the link.
     * @param linkIndex The index of the link in the link list of the person.
     */
    public OpenLinkCommand(Index personIndex, Index linkIndex) {
        super(false);
        requireAllNonNull(personIndex, linkIndex);
        this.personIndex = personIndex;
        this.linkIndex = linkIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        assert model != null : "Model should not be null.";
        List<Person> lastShownList = model.getDisplayedPersonList();
        if (this.personIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        if (!model.isValidLinkIndex(personIndex, linkIndex)) {
            throw new CommandException(String.format(MESSAGE_INVALID_LINK_INDEX,
                    personIndex.getOneBased(), linkIndex.getOneBased()));
        }

        try {
            Link openedLink = model.openLink(personIndex, linkIndex);
            return new CommandResult(String.format(MESSAGE_SUCCESS, openedLink.getValue()));
        } catch (IOException e) {
            throw new CommandException(MESSAGE_CANNOT_OPEN_LINK);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof OpenLinkCommand)) {
            return false;
        }

        OpenLinkCommand command = (OpenLinkCommand) object;
        return this.personIndex.equals(command.personIndex)
                && this.linkIndex.equals(command.linkIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personIndex", personIndex)
                .add("linkIndex", linkIndex)
                .toString();
    }
}
