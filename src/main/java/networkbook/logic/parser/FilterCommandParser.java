package networkbook.logic.parser;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import networkbook.logic.Messages;
import networkbook.logic.commands.filter.FilterCommand;
import networkbook.logic.commands.filter.FilterCourseCommand;
import networkbook.logic.commands.filter.FilterGradCommand;
import networkbook.logic.commands.filter.FilterSpecCommand;
import networkbook.logic.commands.filter.FilterTagCommand;
import networkbook.logic.parser.exceptions.ParseException;
import networkbook.model.person.filter.CourseContainsKeyTermsPredicate;
import networkbook.model.person.filter.CourseIsStillBeingTakenPredicate;
import networkbook.model.person.filter.GradEqualsOneOfPredicate;
import networkbook.model.person.filter.SpecContainsKeyTermsPredicate;
import networkbook.model.person.filter.TagsContainKeyTermsPredicate;

/**
 * Parses input arguments and creates a new FilterCommand object.
 */
public class FilterCommandParser implements Parser<FilterCommand> {
    public static final String MISSING_FIELD = "Filter command must have a /by and /with field, and both fields cannot"
            + " be empty!";
    public static final String UNKNOWN_FIELD = "Can only filter by spec, course, tag, or grad year!";

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
                        CliSyntax.PREFIX_FILTER_ARGS
                );

        if (!ArgumentMultimap.arePrefixesPresent(argMultimap, CliSyntax.PREFIX_FILTER_FIELD)
                || !ArgumentMultimap.arePrefixesPresent(argMultimap, CliSyntax.PREFIX_FILTER_ARGS)) {
            if (!ArgumentMultimap.arePrefixesPresent(argMultimap, CliSyntax.PREFIX_FILTER_FIELD)
                    && !ArgumentMultimap.arePrefixesPresent(argMultimap, CliSyntax.PREFIX_FILTER_ARGS)) {
                throw new ParseException(FilterCommand.MESSAGE_USAGE);
            }
            throw new ParseException(MISSING_FIELD);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                CliSyntax.PREFIX_FILTER_FIELD,
                CliSyntax.PREFIX_FILTER_ARGS);

        String field = argMultimap.getValue(CliSyntax.PREFIX_FILTER_FIELD).orElse("").trim();
        String compArgs = argMultimap.getValue(CliSyntax.PREFIX_FILTER_ARGS).orElse("");

        if (compArgs.equals("")) {
            throw new ParseException(MISSING_FIELD);
        }

        switch (field) {
        case FilterSpecCommand.FIELD_NAME:
            return parseSpec(compArgs);
        case FilterCourseCommand.FIELD_NAME:
            return parseCourse(compArgs);
        case FilterGradCommand.FIELD_NAME:
            return parseGrad(compArgs);
        case FilterTagCommand.FIELD_NAME:
            return parseTag(compArgs);
        default:
            break;
        }
        throw new ParseException(UNKNOWN_FIELD);
    }

    /**
     * Parses the text and create a FilterCourseCommand object.
     *
     * @param course
     * @return A FilterCourseCommand
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FilterCommand parseCourse(String course) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        course,
                        CliSyntax.PREFIX_FILTER_FIN
                );

        argMultimap.verifyNoDuplicatePrefixesFor(
                CliSyntax.PREFIX_FILTER_FIN
        );

        Optional<String> fieldString = Optional.ofNullable(argMultimap.getPreamble());

        if ((fieldString.get().startsWith(CliSyntax.PREFIX_FILTER_FIN.getPrefix()))) {
            throw new ParseException(MISSING_FIELD);
        }

        String[] predicateTerms = fieldString.get().trim().split("\\s+");
        if (Arrays.stream(predicateTerms).anyMatch(s -> s.equals(""))) {
            throw new ParseException(
                    String.format(MISSING_FIELD)
            );
        }

        Optional<String> booleanToCheck = argMultimap.getValue(CliSyntax.PREFIX_FILTER_FIN);
        String booleanToCheckString = booleanToCheck.orElse("false").trim().toLowerCase();

        if (!(booleanToCheckString.equals("true") || booleanToCheckString.equals("false"))) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        return new FilterCourseCommand(
                new CourseContainsKeyTermsPredicate(List.of(predicateTerms)),
                new CourseIsStillBeingTakenPredicate(LocalDate.now()),
                Boolean.parseBoolean(booleanToCheck.orElse("false")));
    }

    /**
     * Parses the text and create a FilterSpecCommand object.
     *
     * @param spec
     * @return A FilterSpecCommand
     */
    public FilterCommand parseSpec(String spec) {
        Optional<String> fieldString = Optional.ofNullable(spec);
        String[] predicateTerms = fieldString.get().trim().split("\\s+");

        return new FilterSpecCommand(new SpecContainsKeyTermsPredicate(List.of(predicateTerms)));
    }

    /**
     * Parses the text and create a FilterTagCommand object.
     *
     * @param tag
     * @return A FilterTagCommand
     */
    public FilterCommand parseTag(String tag) {
        Optional<String> fieldString = Optional.ofNullable(tag);
        String[] predicateTerms = fieldString.get().trim().split("\\s+");

        return new FilterTagCommand(new TagsContainKeyTermsPredicate(List.of(predicateTerms)));
    }

    /**
     * Parses the text and create a FilterGradCommand object.
     *
     * @param grad
     * @return A FilterGradCommand
     */
    public FilterCommand parseGrad(String grad) throws ParseException {
        Optional<String> fieldString = Optional.ofNullable(grad);
        String[] predicateTerms = fieldString.get().trim().split("\\s+");
        if (Arrays.stream(predicateTerms).anyMatch(s -> s.equals(""))) {
            throw new ParseException(MISSING_FIELD);
        }
        try {
            List<Integer> yearsPredicate = Arrays.stream(predicateTerms)
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            return new FilterGradCommand(new GradEqualsOneOfPredicate(yearsPredicate));
        } catch (NumberFormatException e) {
            throw new ParseException(FilterGradCommand.ALL_NUMBERS);
        }
    }
}
