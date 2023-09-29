package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;

import java.util.Optional;

import seedu.address.logic.commands.find.FindEmailCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FindEmailCommand object.
 */
public class FindEmailCommandParser implements Parser<FindEmailCommand> {

    @Override
    public FindEmailCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_EMAIL);
        Optional<String> optionalQuery = argMultimap.getValue(PREFIX_EMAIL);
        if (optionalQuery.isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    FindEmailCommand.MESSAGE_USAGE
            ));
        }

        String queryString = optionalQuery.get();
        return new FindEmailCommand(queryString);
    }
}
