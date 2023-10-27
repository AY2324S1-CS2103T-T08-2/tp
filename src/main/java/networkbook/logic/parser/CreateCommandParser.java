package networkbook.logic.parser;

import java.util.stream.Stream;

import networkbook.logic.Messages;
import networkbook.logic.commands.CreateCommand;
import networkbook.logic.parser.exceptions.ParseException;
import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.Graduation;
import networkbook.model.person.Link;
import networkbook.model.person.Name;
import networkbook.model.person.Person;
import networkbook.model.person.Phone;
import networkbook.model.person.Priority;
import networkbook.model.person.Specialisation;
import networkbook.model.person.Tag;
import networkbook.model.util.UniqueList;

/**
 * Parses input arguments and creates a new CreateCommand object
 */
public class CreateCommandParser implements Parser<CreateCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the CreateCommand
     * and returns an CreateCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CreateCommand parse(String args) throws ParseException {
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

        if (!arePrefixesPresent(
                argMultimap,
                CliSyntax.PREFIX_NAME
        ) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                                                    CreateCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                CliSyntax.PREFIX_NAME,
                CliSyntax.PREFIX_GRADUATION,
                CliSyntax.PREFIX_PRIORITY
        );

        Name name = ParserUtil.parseName(argMultimap.getValue(CliSyntax.PREFIX_NAME)
                    .orElseThrow(() -> new ParseException(String.format(Messages.MESSAGE_INVALID_CONTACT_NAME))));
        UniqueList<Phone> phones = ParserUtil.parsePhones(argMultimap.getAllValues(CliSyntax.PREFIX_PHONE));
        UniqueList<Email> emails = ParserUtil.parseEmails(argMultimap.getAllValues(CliSyntax.PREFIX_EMAIL));
        UniqueList<Link> links = ParserUtil.parseLinks(argMultimap.getAllValues(CliSyntax.PREFIX_LINK));
        Graduation graduation = ParserUtil.parseGraduation(
                    argMultimap.getValue(CliSyntax.PREFIX_GRADUATION).orElse(null));
        UniqueList<Course> courses = ParserUtil.parseCourses(argMultimap.getAllValues(CliSyntax.PREFIX_COURSE));
        UniqueList<Specialisation> specialisations = ParserUtil
                .parseSpecialisations(argMultimap.getAllValues(CliSyntax.PREFIX_SPECIALISATION));
        UniqueList<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(CliSyntax.PREFIX_TAG));
        Priority priority = ParserUtil.parsePriority(argMultimap.getValue(CliSyntax.PREFIX_PRIORITY).orElse(null));

        Person person = new Person(name, phones, emails, links, graduation, courses, specialisations,
                    tagList, priority);

        return new CreateCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
