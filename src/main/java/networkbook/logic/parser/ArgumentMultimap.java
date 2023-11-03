package networkbook.logic.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import networkbook.logic.Messages;
import networkbook.logic.parser.exceptions.ParseException;

/**
 * Stores mapping of prefixes to their respective arguments.
 * Each key may be associated with multiple argument values.
 * Values for a given key are stored in a list, and the insertion ordering is maintained.
 * Keys are unique, but the list of argument values may contain duplicate argument values, i.e. the same argument value
 * can be inserted multiple times for the same prefix.
 */
public class ArgumentMultimap {

    /** Prefixes mapped to their respective arguments**/
    private final Map<Prefix, List<String>> argMultimap = new HashMap<>();

    /**
     * Associates the specified argument value with {@code prefix} key in this map.
     * If the map previously contained a mapping for the key, the new value is appended to the list of existing values.
     *
     * @param prefix   Prefix key with which the specified argument value is to be associated
     * @param argValue Argument value to be associated with the specified prefix key
     */
    public void put(Prefix prefix, String argValue) {
        List<String> argValues = getAllValues(prefix);
        argValues.add(argValue);
        argMultimap.put(prefix, argValues);
    }

    /**
     * Returns the last value of {@code prefix}.
     */
    public Optional<String> getValue(Prefix prefix) {
        List<String> values = getAllValues(prefix);
        return values.isEmpty() ? Optional.empty() : Optional.of(values.get(values.size() - 1));
    }

    /**
     * Returns all values of {@code prefix}.
     * If the prefix does not exist or has no values, this will return an empty list.
     * Modifying the returned list will not affect the underlying data structure of the ArgumentMultimap.
     */
    public List<String> getAllValues(Prefix prefix) {
        if (!argMultimap.containsKey(prefix)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(argMultimap.get(prefix));
    }

    /**
     * Returns the preamble (text before the first valid prefix). Trims any leading/trailing spaces.
     */
    public String getPreamble() {
        return getValue(new Prefix("")).orElse("");
    }

    /**
     * Throws a {@code ParseException} if any of the prefixes given in {@code prefixes} appeared more than
     * once among the arguments.
     */
    public void verifyNoDuplicatePrefixesFor(Prefix... prefixes) throws ParseException {
        Prefix[] duplicatedPrefixes = Stream.of(prefixes).distinct()
                .filter(prefix -> argMultimap.containsKey(prefix) && argMultimap.get(prefix).size() > 1)
                .toArray(Prefix[]::new);

        if (duplicatedPrefixes.length > 0) {
            throw new ParseException(Messages.getErrorMessageForDuplicatePrefixes(duplicatedPrefixes));
        }
    }

    /**
     * Verifies that zero or one of the {@code prefixes} is present.
     * @param prefixes
     * @return null if no prefix is present, and the {@code Prefix} if exactly one is present.
     * @throws ParseException when there are more than one prefix present.
     */
    public Prefix verifyAtMostOneIsPresent(Prefix ... prefixes) throws ParseException {
        assert prefixes.length > 0;

        Prefix result = null;
        for (Prefix prefix: prefixes) {
            if (argMultimap.containsKey(prefix)) {
                if (argMultimap.get(prefix).size() > 1) {
                    throw new ParseException(Messages.MESSAGE_DUPLICATE_PREFIX);
                }
                if (result != null) {
                    throw new ParseException(Messages.MESSAGE_EXACTLY_ONE_FIELD);
                }
                result = prefix;
            }
        }
        return result;
    }

    /**
     * Verifies that one and only one of the {@code prefixes} is present.
     */
    public Prefix verifyExactlyOneIsPresent(Prefix ... prefixes) throws ParseException {
        assert prefixes.length > 0;

        Prefix result = verifyAtMostOneIsPresent(prefixes);

        if (result == null) {
            throw new ParseException(Messages.MESSAGE_EXACTLY_ONE_FIELD);
        }

        return result;
    }

    /**
     * Verifies that if one of the {@code prefixesIfPresent} is present,
     * then the {@code prefixThenPresent} must also be present with only one corresponding value.
     * Otherwise, {@code prefixThenPresent} must be absent.
     */
    public void verifyIfPresentThenOnlyOne(Prefix[] prefixesIfPresent, Prefix prefixThenPresent) throws ParseException {
        Prefix firstPresentPrefix = this.firstPresentPrefix(prefixesIfPresent);
        if (firstPresentPrefix != null) {
            if (!argMultimap.containsKey(prefixThenPresent) || argMultimap.get(prefixThenPresent).size() > 1) {
                throw new ParseException(String.format(Messages.MESSAGE_INDEX_MUST_BE_PRESENT, firstPresentPrefix));
            }
        } else {
            if (argMultimap.containsKey(prefixThenPresent)) {
                throw new ParseException(Messages.MESSAGE_INDEX_CANNOT_BE_PRESENT);
            }
        }
    }

    /**
     * Verifies that the {@code prefix} as no value specified in the command.
     * Pre-condition: the prefix has one and only one value.
     * @param prefix
     * @throws ParseException when the prefix has a non-empty value.
     */
    public void verifyPrefixHasEmptyValue(Prefix prefix) throws ParseException {
        assert(argMultimap.get(prefix).size() == 1);
        if (!getValue(prefix).get().isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_VALUE_CANNOT_BE_PRESENT, prefix));
        }
    }

    private Prefix firstPresentPrefix(Prefix[] prefixes) {
        for (Prefix prefix: prefixes) {
            if (argMultimap.containsKey(prefix)) {
                return prefix;
            }
        }
        return null;
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    public static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
