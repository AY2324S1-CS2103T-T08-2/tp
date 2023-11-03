package networkbook.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import networkbook.model.person.filter.GradEqualsOneOfPredicate;
import networkbook.model.util.UniqueList;
import networkbook.testutil.PersonBuilder;

public class GradEqualsOneOfPredicateTest {
    @Test
    public void equals() {
        List<Integer> firstYearList = Collections.singletonList(2022);
        List<Integer> secondYearList = Arrays.asList(2022, 2023);

        GradEqualsOneOfPredicate firstPredicate = new GradEqualsOneOfPredicate(firstYearList);
        GradEqualsOneOfPredicate secondPredicate = new GradEqualsOneOfPredicate(secondYearList);

        // same object -> returns true
        assertEquals(firstPredicate, firstPredicate);
        assertEquals(secondPredicate, secondPredicate);

        // both lists have the corresponding pairs of items - > returns true
        assertEquals(secondPredicate, new GradEqualsOneOfPredicate(secondYearList));

        // lists have different items - > returns false
        assertNotEquals(firstPredicate, secondPredicate);

        // both lists have the same items but in different order -> returns false
        assertNotEquals(secondPredicate, new GradEqualsOneOfPredicate(Arrays.asList(2023, 2022)));

        // different types -> returns false
        assertNotEquals(firstPredicate, 5);

        // null -> return false
        assertNotEquals(firstPredicate, null);
    }

    @Test
    public void test_noMatchingYears_returnsFalse() {
        GradEqualsOneOfPredicate predicate = new GradEqualsOneOfPredicate(List.of(2022, 2023));
        assertFalse(predicate.test(new PersonBuilder().withGraduation("AY2021-S1").build()));
    }

    @Test
    public void test_matchingYear_returnsTrue() {
        GradEqualsOneOfPredicate predicate = new GradEqualsOneOfPredicate(List.of(2022, 2023));
        assertTrue(predicate.test(new PersonBuilder().withGraduation("AY2324-S1").build()));
    }

    @Test
    public void test_noYears_returnsFalse() {
        assertFalse(new GradEqualsOneOfPredicate(Collections.emptyList())
                .test(new PersonBuilder().withGraduation("AY2324-S1").build()));
        assertFalse(new GradEqualsOneOfPredicate(List.of(2022, 2023))
                .test(new Person(new Name("Ok"),
                        new UniqueList<>(),
                        new UniqueList<>(),
                        new UniqueList<>(),
                        null,
                        new UniqueList<>(),
                        new UniqueList<>(),
                        new UniqueList<>(),
                        null)));
    }

    @Test
    public void toStringTest() {
        List<Integer> years = List.of(2022, 2023);
        GradEqualsOneOfPredicate predicate = new GradEqualsOneOfPredicate(years);

        String expected = GradEqualsOneOfPredicate.class.getCanonicalName() + "{years=" + years + "}";
        assertEquals(predicate.toString(), expected);
    }

    @Test
    public void getGradYears() {
        List<Integer> years = List.of(2022, 2023);
        GradEqualsOneOfPredicate predicate = new GradEqualsOneOfPredicate(years);

        assertEquals(years, predicate.getGradYears());
    }
}
