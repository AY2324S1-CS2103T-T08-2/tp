package networkbook.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import networkbook.commons.core.index.Index;
import networkbook.logic.Messages;
import networkbook.logic.commands.AddCommand;
import networkbook.logic.commands.AddCommand.AddPersonDescriptor;
import networkbook.logic.parser.exceptions.ParseException;
import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.Link;
import networkbook.model.person.Phone;
import networkbook.model.person.Specialisation;
import networkbook.model.person.Tag;
import networkbook.model.util.UniqueList;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {
    public static final String MESSAGE_MULTIPLE_NAMES = "Contact cannot have multiple names.\n"
            + "Please use the 'edit' command instead.";

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args,
                        CliSyntax.PREFIX_NAME,
                        CliSyntax.PREFIX_PHONE,
                        CliSyntax.PREFIX_EMAIL,
                        CliSyntax.PREFIX_LINK,
                        CliSyntax.PREFIX_GRADUATION,
                        CliSyntax.PREFIX_COURSE,
                        CliSyntax.PREFIX_SPECIALISATION,
                        CliSyntax.PREFIX_TAG,
                        CliSyntax.PREFIX_PRIORITY
                );

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(
                            Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                            AddCommand.MESSAGE_USAGE
                    ),
                    pe
            );
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                CliSyntax.PREFIX_NAME,
                CliSyntax.PREFIX_GRADUATION,
                CliSyntax.PREFIX_PRIORITY
        );

        AddPersonDescriptor addPersonDescriptor =
                AddCommandParser.generateAddPersonDescriptor(argMultimap);

        if (!addPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(AddCommand.MESSAGE_NO_INFO);
        }

        return new AddCommand(index, addPersonDescriptor);
    }

    /**
     * Creates an {@code AddPersonDescriptor} based on the arguments provided in an edit or add command.
     * @throws ParseException if the user input does not conform the expected format
     */
    private static AddPersonDescriptor generateAddPersonDescriptor(ArgumentMultimap argMultimap)
            throws ParseException {
        AddPersonDescriptor addPersonDescriptor = new AddCommand.AddPersonDescriptor();

        if (argMultimap.getValue(CliSyntax.PREFIX_NAME).isPresent()) {
            throw new ParseException(MESSAGE_MULTIPLE_NAMES);
        }
        parsePhonesForEdit(argMultimap.getAllValues(CliSyntax.PREFIX_PHONE))
                .ifPresent(addPersonDescriptor::setPhones);
        parseEmailsForEdit(argMultimap.getAllValues(CliSyntax.PREFIX_EMAIL))
                .ifPresent(addPersonDescriptor::setEmails);
        parseLinksForEdit(argMultimap.getAllValues(CliSyntax.PREFIX_LINK))
                .ifPresent(addPersonDescriptor::setLinks);
        if (argMultimap.getValue(CliSyntax.PREFIX_GRADUATION).isPresent()) {
            addPersonDescriptor.setGraduation(
                    ParserUtil.parseGraduation(argMultimap.getValue(CliSyntax.PREFIX_GRADUATION).get()));
        }
        parseCoursesForEdit(argMultimap.getAllValues(CliSyntax.PREFIX_COURSE))
                .ifPresent(addPersonDescriptor::setCourses);
        parseSpecialisationsForEdit(argMultimap.getAllValues(CliSyntax.PREFIX_SPECIALISATION))
                .ifPresent(addPersonDescriptor::setSpecialisations);
        parseTagsForEdit(argMultimap.getAllValues(CliSyntax.PREFIX_TAG))
                .ifPresent(addPersonDescriptor::setTags);
        if (argMultimap.getValue(CliSyntax.PREFIX_PRIORITY).isPresent()) {
            addPersonDescriptor.setPriority(
                    ParserUtil.parsePriority(argMultimap.getValue(CliSyntax.PREFIX_PRIORITY).get()));
        }

        return addPersonDescriptor;
    }

    /**
     * Parses {@code Collection<String> phones} into a {@code UniqueList<Phone>} wrapped in an {@code Optional}.
     */
    private static Optional<UniqueList<Phone>> parsePhonesForEdit(Collection<String> phones) throws ParseException {
        requireNonNull(phones);

        if (phones.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ParserUtil.parsePhones(phones));
    }

    /**
     * Parses {@code Collection<String> emails} into a {@code UniqueList<Email>} wrapped in an {@code Optional}.
     */
    private static Optional<UniqueList<Email>> parseEmailsForEdit(Collection<String> emails) throws ParseException {
        requireNonNull(emails);

        if (emails.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ParserUtil.parseEmails(emails));
    }

    /**
     * Parses {@code Collection<String> links} into a {@code UniqueList<Link>} wrapped in an {@code Optional}.
     */
    private static Optional<UniqueList<Link>> parseLinksForEdit(Collection<String> links) throws ParseException {
        requireNonNull(links);

        if (links.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ParserUtil.parseLinks(links));
    }

    /**
     * Parses {@code Collection<String> courses} into a {@code UniqueList<Course>} wrapped in an {@code Optional}.
     */
    private static Optional<UniqueList<Course>> parseCoursesForEdit(Collection<String> courses) throws ParseException {
        requireNonNull(courses);

        if (courses.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ParserUtil.parseCourses(courses));
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code UniqueList<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code UniqueList<Tag>} containing zero tags.
     */
    private static Optional<UniqueList<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        requireNonNull(tags);

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

    /**
     * Parses {@code Coolection<String> specialisations} into a {@code UniqueList<Specialisation>} wrapped in an
     * {@code Optional}.
     */
    private static Optional<UniqueList<Specialisation>> parseSpecialisationsForEdit(Collection<String> specisalisations)
            throws ParseException {
        requireNonNull(specisalisations);

        if (specisalisations.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ParserUtil.parseSpecialisations(specisalisations));
    }
}
