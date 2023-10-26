package networkbook.logic.parser;

import java.util.Arrays;

import networkbook.logic.Messages;
import networkbook.logic.commands.FindCommand;
import networkbook.logic.parser.exceptions.ParseException;
import networkbook.model.person.NameContainsKeyTermsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_EMPTY_FIELD, FindCommand.COMMAND_WORD));
        }

        String[] nameKeyTerms = trimmedArgs.split("\\s+");

        return new FindCommand(new NameContainsKeyTermsPredicate(Arrays.asList(nameKeyTerms)));
    }

}
