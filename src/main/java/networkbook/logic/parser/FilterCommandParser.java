package networkbook.logic.parser;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import networkbook.logic.Messages;
import networkbook.logic.commands.filter.FilterCommand;
import networkbook.logic.parser.exceptions.ParseException;
import networkbook.model.person.filter.CourseContainsKeyTermsPredicate;
import networkbook.model.person.filter.CourseIsStillBeingTakenPredicate;

/**
 * Parses input arguments and creates a new FilterCommand object.
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Parses the given string of arguments
     *
     * @param args
     * @return a FilterCommand object for execution
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FilterCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args,
                        CliSyntax.PREFIX_FILTER_FIELD,
                        CliSyntax.PREFIX_FILTER_FIN
                );

        argMultimap.verifyNoDuplicatePrefixesFor(
                CliSyntax.PREFIX_FILTER_FIELD,
                CliSyntax.PREFIX_FILTER_FIN
        );

        Optional<String> fieldString = argMultimap.getValue(CliSyntax.PREFIX_FILTER_FIELD);
        if (fieldString.isEmpty()) {
            throw new ParseException(
                    String.format(
                            Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                            FilterCommand.MESSAGE_USAGE
                    )
            );
        }

        String[] predicateTerms = fieldString.get().trim().split("\\s+");
        if (Arrays.stream(predicateTerms).anyMatch(s -> s.equals(""))) {
            throw new ParseException(
                    String.format(
                            Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                            FilterCommand.MESSAGE_USAGE
                    )
            );
        }

        Optional<String> booleanToCheck = argMultimap.getValue(CliSyntax.PREFIX_FILTER_FIN);
        String booleanToCheckString = booleanToCheck.orElse("false").trim().toLowerCase();

        if (!(booleanToCheckString.equals("true") || booleanToCheckString.equals("false"))) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        return new FilterCommand(
                new CourseContainsKeyTermsPredicate(List.of(predicateTerms)),
                new CourseIsStillBeingTakenPredicate(LocalDate.now()),
                Boolean.parseBoolean(booleanToCheck.orElse("false")));
    }
}
