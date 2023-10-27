package networkbook.logic.parser;

import static java.util.Objects.requireNonNull;

import networkbook.commons.core.index.Index;
import networkbook.logic.Messages;
import networkbook.logic.commands.Command;
import networkbook.logic.commands.delete.DeleteCourseAction;
import networkbook.logic.commands.delete.DeleteEmailAction;
import networkbook.logic.commands.delete.DeleteFieldAction;
import networkbook.logic.commands.delete.DeleteFieldCommand;
import networkbook.logic.commands.delete.DeleteGraduationAction;
import networkbook.logic.commands.delete.DeleteLinkAction;
import networkbook.logic.commands.delete.DeletePersonCommand;
import networkbook.logic.commands.delete.DeletePhoneAction;
import networkbook.logic.commands.delete.DeletePriorityAction;
import networkbook.logic.commands.delete.DeleteSpecialisationAction;
import networkbook.logic.commands.delete.DeleteTagAction;
import networkbook.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object.
 */
public class DeleteCommandParser implements Parser<Command> {
    public static final String MESSAGE_DELETE_NAME = "Name of a contact cannot be deleted.";

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parse(String args) throws ParseException {
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

        if (argMultimap.getValue(CliSyntax.PREFIX_NAME).isPresent()) {
            throw new ParseException(MESSAGE_DELETE_NAME);
        }

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE), pe);
        }

        Prefix prefix = argMultimap.verifyAtMostOneIsPresent(
            CliSyntax.PREFIX_PHONE,
            CliSyntax.PREFIX_EMAIL,
            CliSyntax.PREFIX_LINK,
            CliSyntax.PREFIX_GRADUATION,
            CliSyntax.PREFIX_COURSE,
            CliSyntax.PREFIX_SPECIALISATION,
            CliSyntax.PREFIX_TAG,
            CliSyntax.PREFIX_PRIORITY
        );

        if (prefix == null) {
            if (argMultimap.getValue(CliSyntax.PREFIX_INDEX).isPresent()) {
                throw new ParseException(
                        String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE));
            }
            return new DeletePersonCommand(index);
        }

        argMultimap.verifyIfPresentThenOnlyOne(new Prefix[] {
            CliSyntax.PREFIX_PHONE,
            CliSyntax.PREFIX_EMAIL,
            CliSyntax.PREFIX_LINK,
            CliSyntax.PREFIX_COURSE,
            CliSyntax.PREFIX_SPECIALISATION,
            CliSyntax.PREFIX_TAG
        }, CliSyntax.PREFIX_INDEX);

        DeleteFieldAction action = generateAction(argMultimap, prefix);

        return new DeleteFieldCommand(index, action);
    }

    private DeleteFieldAction generateAction(ArgumentMultimap argMultimap, Prefix prefix)
            throws ParseException {
        if (prefix.equals(CliSyntax.PREFIX_PHONE)) {
            return generatePhoneAction(argMultimap);
        } else if (prefix.equals(CliSyntax.PREFIX_EMAIL)) {
            return generateEmailAction(argMultimap);
        } else if (prefix.equals(CliSyntax.PREFIX_LINK)) {
            return generateLinkAction(argMultimap);
        } else if (prefix.equals(CliSyntax.PREFIX_GRADUATION)) {
            return generateGraduationAction(argMultimap);
        } else if (prefix.equals(CliSyntax.PREFIX_COURSE)) {
            return generateCourseAction(argMultimap);
        } else if (prefix.equals(CliSyntax.PREFIX_SPECIALISATION)) {
            return generateSpecialisationAction(argMultimap);
        } else if (prefix.equals(CliSyntax.PREFIX_TAG)) {
            return generateTagAction(argMultimap);
        } else if (prefix.equals(CliSyntax.PREFIX_PRIORITY)) {
            return generatePriorityAction(argMultimap);
        } else {
            assert false : "Prefix passed to generateAction is not supported";
            return null;
        }
    }

    private DeletePhoneAction generatePhoneAction(ArgumentMultimap argMultimap) throws ParseException {
        argMultimap.verifyPrefixHasEmptyValue(CliSyntax.PREFIX_PHONE);
        Index index = ParserUtil.parseIndex(argMultimap.getValue(CliSyntax.PREFIX_INDEX).get());
        return new DeletePhoneAction(index);
    }

    private DeleteEmailAction generateEmailAction(ArgumentMultimap argMultimap) throws ParseException {
        argMultimap.verifyPrefixHasEmptyValue(CliSyntax.PREFIX_EMAIL);
        Index index = ParserUtil.parseIndex(argMultimap.getValue(CliSyntax.PREFIX_INDEX).get());
        return new DeleteEmailAction(index);
    }

    private DeleteLinkAction generateLinkAction(ArgumentMultimap argMultimap) throws ParseException {
        argMultimap.verifyPrefixHasEmptyValue(CliSyntax.PREFIX_LINK);
        Index index = ParserUtil.parseIndex(argMultimap.getValue(CliSyntax.PREFIX_INDEX).get());
        return new DeleteLinkAction(index);
    }

    private DeleteCourseAction generateCourseAction(ArgumentMultimap argMultimap) throws ParseException {
        argMultimap.verifyPrefixHasEmptyValue(CliSyntax.PREFIX_COURSE);
        Index index = ParserUtil.parseIndex(argMultimap.getValue(CliSyntax.PREFIX_INDEX).get());
        return new DeleteCourseAction(index);
    }

    private DeleteSpecialisationAction generateSpecialisationAction(ArgumentMultimap argMultimap)
            throws ParseException {
        argMultimap.verifyPrefixHasEmptyValue(CliSyntax.PREFIX_SPECIALISATION);
        Index index = ParserUtil.parseIndex(argMultimap.getValue(CliSyntax.PREFIX_INDEX).get());
        return new DeleteSpecialisationAction(index);
    }

    private DeleteTagAction generateTagAction(ArgumentMultimap argMultimap) throws ParseException {
        argMultimap.verifyPrefixHasEmptyValue(CliSyntax.PREFIX_TAG);
        Index index = ParserUtil.parseIndex(argMultimap.getValue(CliSyntax.PREFIX_INDEX).get());
        return new DeleteTagAction(index);
    }

    private DeleteGraduationAction generateGraduationAction(ArgumentMultimap argMultimap) throws ParseException {
        argMultimap.verifyPrefixHasEmptyValue(CliSyntax.PREFIX_GRADUATION);
        return new DeleteGraduationAction();
    }

    private DeletePriorityAction generatePriorityAction(ArgumentMultimap argMultimap) throws ParseException {
        argMultimap.verifyPrefixHasEmptyValue(CliSyntax.PREFIX_PRIORITY);
        return new DeletePriorityAction();
    }

}
