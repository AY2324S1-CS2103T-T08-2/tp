package networkbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static networkbook.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import networkbook.commons.core.index.Index;
import networkbook.commons.util.ToStringBuilder;
import networkbook.logic.Messages;
import networkbook.logic.commands.EditCommand.EditPersonDescriptor;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.logic.parser.CliSyntax;
import networkbook.model.Model;
import networkbook.model.person.Address;
import networkbook.model.person.Email;
import networkbook.model.person.Name;
import networkbook.model.person.Person;
import networkbook.model.person.Phone;
import networkbook.model.person.Priority;
import networkbook.model.tag.Tag;
import networkbook.model.util.UniqueList;

/**
 * Adds new information about a contact.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds new information about a contact. "
            + "Parameters: "
            + "[" + CliSyntax.PREFIX_PHONE + " PHONE] "
            + "[" + CliSyntax.PREFIX_EMAIL + " EMAIL] "
            + "[" + CliSyntax.PREFIX_ADDRESS + " ADDRESS] "
            + "[" + CliSyntax.PREFIX_TAG + " TAG] "
            + "[" + CliSyntax.PREFIX_PRIORITY + " PRIORITY]...\n"
            + "Example: " + COMMAND_WORD + " "
            + CliSyntax.PREFIX_PHONE + " 98765432 "
            + CliSyntax.PREFIX_EMAIL + " johnd@example.com "
            + CliSyntax.PREFIX_ADDRESS + " 311, Clementi Ave 2, #02-25 "
            + CliSyntax.PREFIX_TAG + " friends "
            + CliSyntax.PREFIX_TAG + " owesMoney";

    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the network book.";
    public static final String MESSAGE_NO_INFO = "At least one field to add must be provided.";
    public static final String MESSAGE_MULTIPLE_UNIQUE_FIELD = "Some fields provided cannot have multiple values.\n"
            + "Please use the 'update' command instead.";
    public static final String MESSAGE_ADD_INFO_SUCCESS = "Added information to this contact: %1$s";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * Creates a CreateCommand to create the specified {@code Person}
     *
     * @param index of the contact to add information about
     * @param editPersonDescriptor details to add to the contact
     */
    public AddCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = editPersonDescriptor;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToAddInfo = lastShownList.get(index.getZeroBased());
        Person personAfterAddingInfo = addInfoToPerson(personToAddInfo, editPersonDescriptor);

        if (!personToAddInfo.isSame(personAfterAddingInfo) && model.hasPerson(personAfterAddingInfo)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setItem(personToAddInfo, personAfterAddingInfo);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_ADD_INFO_SUCCESS, Messages.format(personAfterAddingInfo)));
    }

    /**
     * Creates and returns a {@code Person} with details in {@code editPersonDescriptor}
     * added to {@code personToEdit}.
     */
    private Person addInfoToPerson(Person personToAddInfo, EditPersonDescriptor editPersonDescriptor)
            throws CommandException {
        assert personToAddInfo != null;

        Name updatedName = personToAddInfo.getName();
        Phone updatedPhone = addPhone(personToAddInfo, editPersonDescriptor);
        UniqueList<Email> updatedEmails = addEmails(personToAddInfo, editPersonDescriptor);
        Address updatedAddress = addAddress(personToAddInfo, editPersonDescriptor);
        Set<Tag> updatedTags = addTags(personToAddInfo, editPersonDescriptor);
        Priority updatedPriority = addPriority(personToAddInfo, editPersonDescriptor);

        return new Person(updatedName, updatedPhone, updatedEmails, updatedAddress, updatedTags, updatedPriority);
    }

    // TODO: for non-unique fields, change respective model to use list and append to the list
    // TODO: now it just replaces the old value

    private Phone addPhone(Person personToAddInfo, EditPersonDescriptor editPersonDescriptor) {
        return editPersonDescriptor.getPhone().orElse(personToAddInfo.getPhone());
    }

    private UniqueList<Email> addEmails(Person personToAddInfo, EditPersonDescriptor editPersonDescriptor) {
        return editPersonDescriptor.getEmails().orElse(personToAddInfo.getEmails());
    }

    private Address addAddress(Person personToAddInfo, EditPersonDescriptor editPersonDescriptor) {
        return editPersonDescriptor.getAddress().orElse(personToAddInfo.getAddress());
    }

    private Set<Tag> addTags(Person personToAddInfo, EditPersonDescriptor editPersonDescriptor) {
        return editPersonDescriptor.getTags().orElse(personToAddInfo.getTags());
    }

    private Priority addPriority(Person personToAddInfo, EditPersonDescriptor editPersonDescriptor)
            throws CommandException {
        Optional<Priority> oldPriority = personToAddInfo.getPriority();
        Optional<Priority> newPriority = editPersonDescriptor.getPriority();
        if (oldPriority.isPresent() && newPriority.isPresent()) {
            throw new CommandException(MESSAGE_MULTIPLE_UNIQUE_FIELD);
        }
        return newPriority.orElse(oldPriority.orElse(null));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherCreateCommand = (AddCommand) other;
        return index.equals(otherCreateCommand.index)
                && editPersonDescriptor.equals(otherCreateCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }
}