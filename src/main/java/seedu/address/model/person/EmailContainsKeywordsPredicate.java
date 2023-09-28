package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a predicate to check whether the email of a person
 * contains the query string.
 */
public class EmailContainsKeywordsPredicate implements Predicate<Person> {
    private final String queryString;

    public EmailContainsKeywordsPredicate(String query) {
        this.queryString = query;
    }

    @Override
    public boolean test(Person person) {
        return StringUtil.containsStringIgnoreCase(person.getEmail().toString(), this.queryString);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EmailContainsKeywordsPredicate)) {
            return false;
        }

        EmailContainsKeywordsPredicate otherEmailContainsKeywordsPredicate =
                (EmailContainsKeywordsPredicate) other;
        return this.queryString.equals(otherEmailContainsKeywordsPredicate.queryString);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("query", this.queryString).toString();
    }
}
