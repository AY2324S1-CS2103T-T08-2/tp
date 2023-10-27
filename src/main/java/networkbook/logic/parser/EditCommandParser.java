package networkbook.logic.parser;

import static java.util.Objects.requireNonNull;

import networkbook.commons.core.index.Index;
import networkbook.logic.Messages;
import networkbook.logic.commands.edit.EditAction;
import networkbook.logic.commands.edit.EditCommand;
import networkbook.logic.commands.edit.EditCourseAction;
import networkbook.logic.commands.edit.EditEmailAction;
import networkbook.logic.commands.edit.EditGraduationAction;
import networkbook.logic.commands.edit.EditLinkAction;
import networkbook.logic.commands.edit.EditNameAction;
import networkbook.logic.commands.edit.EditPhoneAction;
import networkbook.logic.commands.edit.EditPriorityAction;
import networkbook.logic.commands.edit.EditSpecialisationAction;
import networkbook.logic.commands.edit.EditTagAction;
import networkbook.logic.parser.exceptions.ParseException;
import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.Graduation;
import networkbook.model.person.Link;
import networkbook.model.person.Name;
import networkbook.model.person.Phone;
import networkbook.model.person.Priority;
import networkbook.model.person.Specialisation;
import networkbook.model.person.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
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
                        CliSyntax.PREFIX_PRIORITY,
                        CliSyntax.PREFIX_INDEX
                );

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(
                            Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                            EditCommand.MESSAGE_USAGE
                    ),
                    pe
            );
        }

        Prefix prefix = argMultimap.verifyExactlyOneIsPresent(
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

        argMultimap.verifyIfPresentThenOnlyOne(new Prefix[] {
            CliSyntax.PREFIX_PHONE,
            CliSyntax.PREFIX_EMAIL,
            CliSyntax.PREFIX_LINK,
            CliSyntax.PREFIX_COURSE,
            CliSyntax.PREFIX_SPECIALISATION,
            CliSyntax.PREFIX_TAG
        }, CliSyntax.PREFIX_INDEX);

        EditAction action = generateAction(argMultimap, prefix);

        return new EditCommand(index, action);
    }

    private static EditAction generateAction(ArgumentMultimap argMultimap, Prefix prefix) throws ParseException {
        EditAction result = null;
        if (prefix.equals(CliSyntax.PREFIX_NAME)) {
            result = generateNameAction(argMultimap);
        } else if (prefix.equals(CliSyntax.PREFIX_PHONE)) {
            result = generatePhoneAction(argMultimap);
        } else if (prefix.equals(CliSyntax.PREFIX_EMAIL)) {
            result = generateEmailAction(argMultimap);
        } else if (prefix.equals(CliSyntax.PREFIX_LINK)) {
            result = generateLinkAction(argMultimap);
        } else if (prefix.equals(CliSyntax.PREFIX_GRADUATION)) {
            result = generateGraduationAction(argMultimap);
        } else if (prefix.equals(CliSyntax.PREFIX_COURSE)) {
            result = generateCourseAction(argMultimap);
        } else if (prefix.equals(CliSyntax.PREFIX_SPECIALISATION)) {
            result = generateSpecialisationAction(argMultimap);
        } else if (prefix.equals(CliSyntax.PREFIX_TAG)) {
            result = generateTagAction(argMultimap);
        } else if (prefix.equals(CliSyntax.PREFIX_PRIORITY)) {
            result = generatePriorityAction(argMultimap);
        }

        assert result != null;
        return result;
    }

    private static EditAction generateNameAction(ArgumentMultimap argMultimap) throws ParseException {
        Name name = ParserUtil.parseName(argMultimap.getValue(CliSyntax.PREFIX_NAME).get());
        return new EditNameAction(name);
    }

    private static EditAction generatePhoneAction(ArgumentMultimap argMultimap) throws ParseException {
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(CliSyntax.PREFIX_PHONE).get());
        Index index = ParserUtil.parseIndex(argMultimap.getValue(CliSyntax.PREFIX_INDEX).get());
        return new EditPhoneAction(index, phone);
    }

    private static EditAction generateEmailAction(ArgumentMultimap argMultimap) throws ParseException {
        Email email = ParserUtil.parseEmail(argMultimap.getValue(CliSyntax.PREFIX_EMAIL).get());
        Index index = ParserUtil.parseIndex(argMultimap.getValue(CliSyntax.PREFIX_INDEX).get());
        return new EditEmailAction(index, email);
    }

    private static EditAction generateLinkAction(ArgumentMultimap argMultimap) throws ParseException {
        Link link = ParserUtil.parseLink(argMultimap.getValue(CliSyntax.PREFIX_LINK).get());
        Index index = ParserUtil.parseIndex(argMultimap.getValue(CliSyntax.PREFIX_INDEX).get());
        return new EditLinkAction(index, link);
    }

    private static EditAction generateGraduationAction(ArgumentMultimap argMultimap) throws ParseException {
        Graduation graduation = ParserUtil.parseGraduation(argMultimap.getValue(CliSyntax.PREFIX_GRADUATION).get());
        return new EditGraduationAction(graduation);
    }

    private static EditAction generateCourseAction(ArgumentMultimap argMultimap) throws ParseException {
        Course course = ParserUtil.parseCourse(argMultimap.getValue(CliSyntax.PREFIX_COURSE).get());
        Index index = ParserUtil.parseIndex(argMultimap.getValue(CliSyntax.PREFIX_INDEX).get());
        return new EditCourseAction(index, course);
    }

    private static EditAction generateSpecialisationAction(ArgumentMultimap argMultimap) throws ParseException {
        Specialisation specialisation =
                ParserUtil.parseSpecialisation(argMultimap.getValue(CliSyntax.PREFIX_SPECIALISATION).get());
        Index index = ParserUtil.parseIndex(argMultimap.getValue(CliSyntax.PREFIX_INDEX).get());
        return new EditSpecialisationAction(index, specialisation);
    }

    private static EditAction generateTagAction(ArgumentMultimap argMultimap) throws ParseException {
        Tag tag = ParserUtil.parseTag(argMultimap.getValue(CliSyntax.PREFIX_TAG).get());
        Index index = ParserUtil.parseIndex(argMultimap.getValue(CliSyntax.PREFIX_INDEX).get());
        return new EditTagAction(index, tag);
    }

    private static EditAction generatePriorityAction(ArgumentMultimap argMultimap) throws ParseException {
        Priority priority = ParserUtil.parsePriority(argMultimap.getValue(CliSyntax.PREFIX_PRIORITY).get());
        return new EditPriorityAction(priority);
    }
}
