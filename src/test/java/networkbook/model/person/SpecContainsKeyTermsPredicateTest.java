package networkbook.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import networkbook.model.person.filter.SpecContainsKeyTermsPredicate;
import networkbook.testutil.PersonBuilder;

public class SpecContainsKeyTermsPredicateTest {
    @Test
    public void equals() {
        List<String> firstKeyTermList = Collections.singletonList("first");
        List<String> secondKeyTermList = Arrays.asList("first", "second");

        SpecContainsKeyTermsPredicate firstPredicate = new SpecContainsKeyTermsPredicate(firstKeyTermList);
        SpecContainsKeyTermsPredicate secondPredicate = new SpecContainsKeyTermsPredicate(secondKeyTermList);

        // same object -> returns true
        assertEquals(firstPredicate, firstPredicate);
        assertEquals(secondPredicate, secondPredicate);

        // both lists have the corresponding pairs of items - > returns true
        assertEquals(secondPredicate, new SpecContainsKeyTermsPredicate(secondKeyTermList));

        // lists have different items - > returns false
        assertNotEquals(firstPredicate, secondPredicate);

        // both lists have the same items but in different order -> returns false
        assertNotEquals(secondPredicate, new SpecContainsKeyTermsPredicate(Arrays.asList("second", "first")));

        // different types -> returns false
        assertNotEquals(firstPredicate, 5);

        // null -> return false
        assertNotEquals(firstPredicate, null);
    }

    @Test
    public void test_coursesContainKeyTerms_returnsTrue() {
        Person oneCoursePerson = new PersonBuilder().addSpecialisation("First").build();

        // 1 to 1 complete match
        SpecContainsKeyTermsPredicate predicate = new SpecContainsKeyTermsPredicate(Arrays.asList("First"));
        assertTrue(predicate.test(oneCoursePerson));

        // 1 to 1 mixed case match
        predicate = new SpecContainsKeyTermsPredicate(Arrays.asList("fIRsT"));
        assertTrue(predicate.test(oneCoursePerson));

        // 1 to 1 partial match
        predicate = new SpecContainsKeyTermsPredicate(Arrays.asList("Fir"));
        assertTrue(predicate.test(oneCoursePerson));

        // 1 to many complete match
        Person manyCoursePerson = new PersonBuilder().withSpecialisations(List.of("First", "Second")).build();
        predicate = new SpecContainsKeyTermsPredicate(Arrays.asList("First"));
        assertTrue(predicate.test(manyCoursePerson));

        // 1 to many mixed case match
        predicate = new SpecContainsKeyTermsPredicate(Arrays.asList("fIRsT"));
        assertTrue(predicate.test(manyCoursePerson));

        // 1 to many partial match
        predicate = new SpecContainsKeyTermsPredicate(Arrays.asList("Fir"));
        assertTrue(predicate.test(manyCoursePerson));

        // many to 1 complete match
        predicate = new SpecContainsKeyTermsPredicate(Arrays.asList("First", "Second"));
        assertTrue(predicate.test(oneCoursePerson));

        // many to 1 mixed case match
        predicate = new SpecContainsKeyTermsPredicate(Arrays.asList("fIRsT", "sEcoNd"));
        assertTrue(predicate.test(oneCoursePerson));

        // many to 1 partial match
        predicate = new SpecContainsKeyTermsPredicate(Arrays.asList("Fir", "Sec"));
        assertTrue(predicate.test(oneCoursePerson));

        // many to many; at least one complete match
        predicate = new SpecContainsKeyTermsPredicate(Arrays.asList("Third", "Fourth", "Second"));
        assertTrue(predicate.test(manyCoursePerson));

        // many to many; at least one mixed case match
        predicate = new SpecContainsKeyTermsPredicate(Arrays.asList("fiRSt", "THIRD", "SecONd"));
        assertTrue(predicate.test(manyCoursePerson));

        // many to many; at least one partial match
        predicate = new SpecContainsKeyTermsPredicate(Arrays.asList("Thir", "Fif", "Sec"));
        assertTrue(predicate.test(manyCoursePerson));
    }

    @Test
    public void test_doesNotContainKeyTerms_returnsFalse() {
        // Zero key terms
        SpecContainsKeyTermsPredicate predicate = new SpecContainsKeyTermsPredicate(Collections.emptyList());
        assertFalse(predicate.test(
                new PersonBuilder()
                        .withName("Alice")
                        .withSpecialisations(List.of("Computer Science"))
                        .build()));

        // No matching key terms (one course)
        predicate = new SpecContainsKeyTermsPredicate(Arrays.asList("Drawing", "Painting"));
        assertFalse(predicate.test(
                new PersonBuilder()
                        .withName("Alice")
                        .withSpecialisations(List.of("Computer Science"))
                        .build()));

        // No matching key terms (multiple courses)
        assertFalse(predicate.test(
                new PersonBuilder()
                        .withName("Alice")
                        .withSpecialisations(List.of("Computer Science", "Information Systems"))
                        .build()));

        // Key term matches name but does not match course
        predicate = new SpecContainsKeyTermsPredicate(Arrays.asList("Alice"));
        assertFalse(predicate.test(
                new PersonBuilder()
                        .withName("Alice")
                        .withSpecialisations(List.of("Computer Science", "Information Systems"))
                        .build()));
    }

    @Test
    public void test_personWithNoCourses_returnsFalse() {
        SpecContainsKeyTermsPredicate predicate = new SpecContainsKeyTermsPredicate(List.of("Computers"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));
    }

    @Test
    public void toStringTest() {
        List<String> keyTerms = List.of("First", "Second");
        SpecContainsKeyTermsPredicate predicate = new SpecContainsKeyTermsPredicate(keyTerms);

        String expected = SpecContainsKeyTermsPredicate.class.getCanonicalName() + "{key terms=" + keyTerms + "}";
        assertEquals(predicate.toString(), expected);
    }

    @Test
    public void getKeyTerms() {
        List<String> keyTerms = List.of("First", "Second");
        SpecContainsKeyTermsPredicate predicate = new SpecContainsKeyTermsPredicate(keyTerms);

        assertEquals(keyTerms, predicate.getKeyTerms());
    }
}
