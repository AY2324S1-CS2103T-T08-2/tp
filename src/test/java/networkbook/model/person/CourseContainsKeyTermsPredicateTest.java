package networkbook.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import networkbook.model.person.filter.CourseContainsKeyTermsPredicate;
import networkbook.testutil.PersonBuilder;

public class CourseContainsKeyTermsPredicateTest {

    @Test
    public void equals() {
        List<String> firstKeyTermList = Collections.singletonList("first");
        List<String> secondKeyTermList = Arrays.asList("first", "second");

        CourseContainsKeyTermsPredicate firstPredicate = new CourseContainsKeyTermsPredicate(firstKeyTermList);
        CourseContainsKeyTermsPredicate secondPredicate = new CourseContainsKeyTermsPredicate(secondKeyTermList);

        // same object -> returns true
        assertEquals(firstPredicate, firstPredicate);
        assertEquals(secondPredicate, secondPredicate);

        // both lists have the corresponding pairs of items - > returns true
        assertEquals(secondPredicate, new CourseContainsKeyTermsPredicate(secondKeyTermList));

        // lists have different items - > returns false
        assertNotEquals(firstPredicate, secondPredicate);

        // both lists have the same items but in different order -> returns false
        assertNotEquals(secondPredicate, new CourseContainsKeyTermsPredicate(Arrays.asList("second", "first")));

        // different types -> returns false
        assertNotEquals(firstPredicate, 5);

        // null -> return false
        assertNotEquals(firstPredicate, null);
    }

    @Test
    public void test_coursesContainKeyTerms_returnsTrue() {
        Person oneCoursePerson = new PersonBuilder().addCourse("First").build();

        // 1 to 1 complete match
        CourseContainsKeyTermsPredicate predicate = new CourseContainsKeyTermsPredicate(Arrays.asList("First"));
        assertTrue(predicate.test(oneCoursePerson));

        // 1 to 1 mixed case match
        predicate = new CourseContainsKeyTermsPredicate(Arrays.asList("fIRsT"));
        assertTrue(predicate.test(oneCoursePerson));

        // 1 to 1 partial match
        predicate = new CourseContainsKeyTermsPredicate(Arrays.asList("Fir"));
        assertTrue(predicate.test(oneCoursePerson));

        // 1 to many complete match
        Person manyCoursePerson = new PersonBuilder().withCourses(List.of("First", "Second")).build();
        predicate = new CourseContainsKeyTermsPredicate(Arrays.asList("First"));
        assertTrue(predicate.test(manyCoursePerson));

        // 1 to many mixed case match
        predicate = new CourseContainsKeyTermsPredicate(Arrays.asList("fIRsT"));
        assertTrue(predicate.test(manyCoursePerson));

        // 1 to many partial match
        predicate = new CourseContainsKeyTermsPredicate(Arrays.asList("Fir"));
        assertTrue(predicate.test(manyCoursePerson));

        // many to 1 complete match
        predicate = new CourseContainsKeyTermsPredicate(Arrays.asList("First", "Second"));
        assertTrue(predicate.test(oneCoursePerson));

        // many to 1 mixed case match
        predicate = new CourseContainsKeyTermsPredicate(Arrays.asList("fIRsT", "sEcoNd"));
        assertTrue(predicate.test(oneCoursePerson));

        // many to 1 partial match
        predicate = new CourseContainsKeyTermsPredicate(Arrays.asList("Fir", "Sec"));
        assertTrue(predicate.test(oneCoursePerson));

        // many to many; at least one complete match
        predicate = new CourseContainsKeyTermsPredicate(Arrays.asList("Third", "Fourth", "Second"));
        assertTrue(predicate.test(manyCoursePerson));

        // many to many; at least one mixed case match
        predicate = new CourseContainsKeyTermsPredicate(Arrays.asList("fiRSt", "THIRD", "SecONd"));
        assertTrue(predicate.test(manyCoursePerson));

        // many to many; at least one partial match
        predicate = new CourseContainsKeyTermsPredicate(Arrays.asList("Thir", "Fif", "Sec"));
        assertTrue(predicate.test(manyCoursePerson));
    }

    @Test
    public void test_courseDoesNotContainKeyTerms_returnsFalse() {
        // Zero key terms
        CourseContainsKeyTermsPredicate predicate = new CourseContainsKeyTermsPredicate(Collections.emptyList());
        assertFalse(predicate.test(
                new PersonBuilder()
                        .withName("Alice")
                        .withCourses(List.of("Computer Science"))
                        .build()));

        // No matching key terms (one course)
        predicate = new CourseContainsKeyTermsPredicate(Arrays.asList("Drawing", "Painting"));
        assertFalse(predicate.test(
                new PersonBuilder()
                        .withName("Alice")
                        .withCourses(List.of("Computer Science"))
                        .build()));

        // No matching key terms (multiple courses)
        assertFalse(predicate.test(
                new PersonBuilder()
                        .withName("Alice")
                        .withCourses(List.of("Computer Science", "Information Systems"))
                        .build()));

        // Key term matches name but does not match course
        predicate = new CourseContainsKeyTermsPredicate(Arrays.asList("Alice"));
        assertFalse(predicate.test(
                new PersonBuilder()
                        .withName("Alice")
                        .withCourses(List.of("Computer Science", "Information Systems"))
                        .build()));

        // Key term matches start or end date but does not match course
        predicate = new CourseContainsKeyTermsPredicate(Arrays.asList("01-01-2000"));
        assertFalse(predicate.test(
                new PersonBuilder()
                        .withName("Alice")
                        .addCourse("Computer science", "01-01-2000")
                        .build()));
        assertFalse(predicate.test(
                new PersonBuilder()
                        .withName("Alice")
                        .addCourse("Computer science", "01-01-1999", "01-01-2000")
                        .build()));
    }

    @Test
    public void test_personWithNoCourses_returnsFalse() {
        CourseContainsKeyTermsPredicate predicate = new CourseContainsKeyTermsPredicate(List.of("Computers"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));
    }

    @Test
    public void toStringTest() {
        List<String> keyTerms = List.of("First", "Second");
        CourseContainsKeyTermsPredicate predicate = new CourseContainsKeyTermsPredicate(keyTerms);

        String expected = CourseContainsKeyTermsPredicate.class.getCanonicalName() + "{key terms=" + keyTerms + "}";
        assertEquals(predicate.toString(), expected);
    }

    @Test
    public void getKeyTerms() {
        List<String> keyTerms = List.of("First", "Second");
        CourseContainsKeyTermsPredicate predicate = new CourseContainsKeyTermsPredicate(keyTerms);

        assertEquals(keyTerms, predicate.getKeyTerms());
    }

    @Test
    public void getCourses() {
        List<String> keyTerms = List.of("First", "Second", "Third");
        CourseContainsKeyTermsPredicate predicate = new CourseContainsKeyTermsPredicate(keyTerms);

        Person personWithSomeMatchingCourses = new PersonBuilder().addCourse("First").addCourse("Third").build();
        List<Course> expected = List.of(new Course("First"), new Course("Third"));

        assertEquals(expected, predicate.getCourses(personWithSomeMatchingCourses));
    }
}
