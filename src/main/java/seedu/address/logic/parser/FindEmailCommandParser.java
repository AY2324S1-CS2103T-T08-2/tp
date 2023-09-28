package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;

import seedu.address.logic.commands.find.FindEmailCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FindEmailCommand object.
 */
public class FindEmailCommandParser implements Parser<FindEmailCommand> {

    @Override
    public FindEmailCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty() || !trimmedArgs.startsWith(PREFIX_EMAIL.toString())) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    FindEmailCommand.MESSAGE_USAGE
            ));
        }

        String queryString = trimmedArgs.split(PREFIX_EMAIL.toString())[1];
        return new FindEmailCommand(queryString);
    }
}
