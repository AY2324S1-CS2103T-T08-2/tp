package networkbook.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import networkbook.model.person.filter.TagsContainKeyTermsPredicate;
import networkbook.testutil.PersonBuilder;

public class TagContainsKeyTermsPredicateTest {
    @Test
    public void equals() {
        List<String> firstKeyTermList = Collections.singletonList("first");
        List<String> secondKeyTermList = Arrays.asList("first", "second");

        TagsContainKeyTermsPredicate firstPredicate = new TagsContainKeyTermsPredicate(firstKeyTermList);
        TagsContainKeyTermsPredicate secondPredicate = new TagsContainKeyTermsPredicate(secondKeyTermList);

        // same object -> returns true
        assertEquals(firstPredicate, firstPredicate);

        // same corresponding pairs of items -> returns true
        assertEquals(firstPredicate, new TagsContainKeyTermsPredicate(firstKeyTermList));

        // lists have different items -> returns false
        assertNotEquals(firstPredicate, secondPredicate);

        // lists have same items in different order -> returns false
        assertNotEquals(secondPredicate, new TagsContainKeyTermsPredicate(List.of("second", "first")));

        // different type -> returns false
        assertNotEquals(firstPredicate, 4);

        // null -> returns false
        assertNotEquals(firstPredicate, null);
    }

    @Test
    public void getKeyTerms() {
        List<String> termList = List.of("A", "B");
        assertEquals(new TagsContainKeyTermsPredicate(termList).getKeyTerms(), termList);
    }

    @Test
    public void toStringTest() {
        List<String> keyTerms = List.of("First", "Second");
        TagsContainKeyTermsPredicate predicate = new TagsContainKeyTermsPredicate(keyTerms);

        String expected = TagsContainKeyTermsPredicate.class.getCanonicalName() + "{key terms=" + keyTerms + "}";
        assertEquals(expected, predicate.toString());
    }

    @Test
    public void test_personHasNoTags_returnsFalse() {
        TagsContainKeyTermsPredicate predicate = new TagsContainKeyTermsPredicate(List.of("A", "B"));
        assertFalse(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void test_predicateHasNoTags_returnsFalse() {
        TagsContainKeyTermsPredicate predicate = new TagsContainKeyTermsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withTags("A", "B", "C").build()));
    }

    @Test
    public void test_unmatchedKeyTerms_returnsFalse() {
        TagsContainKeyTermsPredicate predicate = new TagsContainKeyTermsPredicate(List.of("A"));
        assertFalse(predicate.test(new PersonBuilder().withTags("B").build()));
    }

    @Test
    public void test_atLeastOneMatch_returnsTrue() {
        TagsContainKeyTermsPredicate predicate = new TagsContainKeyTermsPredicate(List.of("A", "B"));
        assertTrue(predicate.test(new PersonBuilder().withTags("B").build()));
    }

    @Test
    public void test_multipleMatch_returnsTrue() {
        TagsContainKeyTermsPredicate predicate = new TagsContainKeyTermsPredicate(List.of("A", "B"));
        assertTrue(predicate.test(new PersonBuilder().withTags("A", "B").build()));
    }

    @Test
    public void test_partialMatch_returnsTrue() {
        TagsContainKeyTermsPredicate predicate = new TagsContainKeyTermsPredicate(List.of("A"));
        assertTrue(predicate.test(new PersonBuilder().withTags("Alpha").build()));
    }

    @Test
    public void test_mixedCaseMatch_returnsTrue() {
        TagsContainKeyTermsPredicate predicate = new TagsContainKeyTermsPredicate(List.of("aLphA"));
        assertTrue(predicate.test(new PersonBuilder().withTags("Alpha").build()));
    }
}
