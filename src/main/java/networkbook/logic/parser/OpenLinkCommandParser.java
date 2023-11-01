package networkbook.logic.parser;

import networkbook.commons.core.index.Index;
import networkbook.logic.Messages;
import networkbook.logic.commands.OpenLinkCommand;
import networkbook.logic.parser.exceptions.ParseException;

/**
 * Represents a parser to parse input into an {@code OpenLinkCommand}.
 */
public class OpenLinkCommandParser implements Parser<OpenLinkCommand> {
    /**
     * Parses the user input into an {@code OpenLinkCommand}.
     * @throws ParseException If the user input is invalid.
     */
    public OpenLinkCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_INDEX);

        Index personIndex;

        try {
            personIndex = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, OpenLinkCommand.MESSAGE_USAGE),
                    pe
            );
        }

        argMultimap.verifyNoDuplicatePrefixesFor(CliSyntax.PREFIX_INDEX);

        Index linkIndex = ParserUtil.parseIndex(argMultimap.getValue(CliSyntax.PREFIX_INDEX).orElse("1"));

        return new OpenLinkCommand(personIndex, linkIndex);
    }
}
