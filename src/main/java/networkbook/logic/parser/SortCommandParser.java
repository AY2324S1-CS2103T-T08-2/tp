package networkbook.logic.parser;

import java.util.Optional;

import networkbook.logic.Messages;
import networkbook.logic.commands.SortCommand;
import networkbook.logic.parser.exceptions.ParseException;
import networkbook.model.person.PersonSortComparator;
import networkbook.model.person.PersonSortComparator.SortField;
import networkbook.model.person.PersonSortComparator.SortOrder;

/**
 * Parses input arguments and creates a new SortCommand object
 */
public class SortCommandParser implements Parser<SortCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand
     * and returns an SortCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SortCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args,
                        CliSyntax.PREFIX_SORT_FIELD,
                        CliSyntax.PREFIX_SORT_ORDER
                );

        argMultimap.verifyNoDuplicatePrefixesFor(
                CliSyntax.PREFIX_SORT_FIELD,
                CliSyntax.PREFIX_SORT_ORDER
        );

        Optional<String> fieldString = argMultimap.getValue(CliSyntax.PREFIX_SORT_FIELD);
        if (fieldString.isEmpty()) {
            throw new ParseException(
                String.format(Messages.MESSAGE_MISSING_FIELD, CliSyntax.PREFIX_SORT_FIELD)
            );
        }
        SortField field = ParserUtil.parseSortField(fieldString.get());

        Optional<String> orderString = argMultimap.getValue(CliSyntax.PREFIX_SORT_ORDER);
        SortOrder order = ParserUtil.parseSortOrder(orderString.orElse("asc"));

        PersonSortComparator comparator = new PersonSortComparator(field, order);
        return new SortCommand(comparator);
    }

}
