package networkbook.model.person.filter;

import java.util.List;
import java.util.function.Predicate;

import networkbook.commons.util.CollectionUtil;
import networkbook.commons.util.StringUtil;
import networkbook.commons.util.ToStringBuilder;
import networkbook.model.person.Person;

/**
 * Tests that at least one of a Person's specialisations contains one of the given key terms.
 */
public class SpecContainsKeyTermsPredicate implements Predicate<Person> {
    private final List<String> keyTerms;

    /**
     * Creates a predicate that returns true for any Person object that has at least one specialisation that
     * partially matches any of the key terms given.
     */
    public SpecContainsKeyTermsPredicate(List<String> keyTerms) {
        assert keyTerms != null : "List should not be null";
        CollectionUtil.requireAllNonNull(keyTerms);
        this.keyTerms = keyTerms;
    }

    @Override
    public boolean test(Person person) {
        assert person != null : "Person should not be null";
        return keyTerms.stream()
                .anyMatch(keyTerm -> person.getSpecialisations().stream()
                        .anyMatch(spec -> StringUtil.containsTermIgnoreCase(spec.getSpecialisation(), keyTerm)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SpecContainsKeyTermsPredicate)) {
            return false;
        }

        SpecContainsKeyTermsPredicate otherSpecContainsKeyTermsPredicate = (SpecContainsKeyTermsPredicate) other;
        return keyTerms.equals(otherSpecContainsKeyTermsPredicate.keyTerms);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("key terms", keyTerms).toString();
    }

    public List<String> getKeyTerms() {
        return keyTerms;
    }
}
