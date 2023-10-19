package networkbook.testutil;


import networkbook.logic.commands.CreateCommand;
import networkbook.logic.commands.EditCommand;
import networkbook.logic.parser.CliSyntax;
import networkbook.model.person.Email;
import networkbook.model.person.Link;
import networkbook.model.person.Person;
import networkbook.model.person.Phone;
import networkbook.model.util.UniqueList;

/**
 * A utility class for Person.
 */
public class PersonUtil {
    //TODO: Add priority
    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        return CreateCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(CliSyntax.PREFIX_NAME + " " + person.getName().fullName + " ");
        person.getPhones().stream().forEach(
                e -> sb.append(CliSyntax.PREFIX_PHONE + " " + e.toString() + " ")
        );
        person.getEmails().stream().forEach(
                e -> sb.append(CliSyntax.PREFIX_EMAIL + " " + e.toString() + " ")
        );
        person.getLinks().stream().forEach(
                e -> sb.append(CliSyntax.PREFIX_LINK + " " + e.toString() + " ")
        );
        person.getGraduation().ifPresent(graduation -> sb.append(CliSyntax.PREFIX_GRADUATION)
                .append(" ").append(graduation).append(" "));
        person.getCourse().ifPresent(course -> sb.append(CliSyntax.PREFIX_COURSE).append(" ")
                .append(course.getCourse()).append(" "));
        person.getSpecialisation().ifPresent(specialisation -> sb.append(CliSyntax.PREFIX_SPECIALISATION)
                .append(" ").append(specialisation.value).append(" "));
        person.getTags().stream().forEach(
            s -> sb.append(CliSyntax.PREFIX_TAG + " " + s.getValue() + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditCommand.EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(CliSyntax.PREFIX_NAME).append(" ")
                                                    .append(name.fullName).append(" "));
        if (descriptor.getPhones().isPresent()) {
            UniqueList<Phone> phones = descriptor.getPhones().get();
            if (phones.isEmpty()) {
                sb.append(CliSyntax.PREFIX_PHONE).append(" ");
            } else {
                phones.forEach(e -> sb.append(CliSyntax.PREFIX_PHONE).append(" ")
                        .append(e.toString()).append(" "));
            }
        }
        if (descriptor.getEmails().isPresent()) {
            UniqueList<Email> emails = descriptor.getEmails().get();
            if (emails.isEmpty()) {
                sb.append(CliSyntax.PREFIX_EMAIL).append(" ");
            } else {
                emails.forEach(e -> sb.append(CliSyntax.PREFIX_EMAIL).append(" ")
                                                    .append(e.toString()).append(" "));
            }
        }
        if (descriptor.getLinks().isPresent()) {
            UniqueList<Link> links = descriptor.getLinks().get();
            if (links.isEmpty()) {
                sb.append(CliSyntax.PREFIX_LINK).append(" ");
            } else {
                links.forEach(e -> sb.append(CliSyntax.PREFIX_LINK).append(" ").append(e.toString()).append(" "));
            }
        }
        descriptor.getGraduation().ifPresent(graduation -> sb.append(CliSyntax.PREFIX_GRADUATION)
                .append(" ").append(graduation).append(" "));
        descriptor.getCourse().ifPresent(course -> sb.append(CliSyntax.PREFIX_COURSE).append(" ")
                .append(course.getCourse()).append(" "));
        descriptor.getSpecialisation().ifPresent(specialisation -> sb.append(CliSyntax.PREFIX_SPECIALISATION)
                .append(" ").append(specialisation.value).append(" "));
        descriptor.getTags().ifPresent((tagList) -> {
            if (tagList.isEmpty()) {
                sb.append(CliSyntax.PREFIX_TAG).append(" ");
            } else {
                tagList.forEach((t) ->
                        sb.append(CliSyntax.PREFIX_TAG).append(" ").append(t.toString()).append(" "));
            }
        });
        return sb.toString();
    }
}
