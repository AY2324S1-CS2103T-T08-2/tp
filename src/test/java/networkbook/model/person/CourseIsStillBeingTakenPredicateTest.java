package networkbook.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import org.junit.jupiter.api.Test;

import networkbook.model.person.filter.CourseIsStillBeingTakenPredicate;

public class CourseIsStillBeingTakenPredicateTest {
    @Test
    public void dateEqualOrAfter() {
        DateTimeFormatter dtf = DateTimeFormatter
                .ofPattern("dd-MM-uuuu")
                .withResolverStyle(ResolverStyle.STRICT);

        LocalDate earlyDate = LocalDate.parse("01-01-2000", dtf);
        LocalDate lateDate = LocalDate.parse("02-01-2000", dtf);

        // same date -> returns true
        assertTrue(CourseIsStillBeingTakenPredicate.dateEqualOrAfter(earlyDate, earlyDate));


        // first date is after second date -> returns true
        assertTrue(CourseIsStillBeingTakenPredicate.dateEqualOrAfter(lateDate, earlyDate));

        // first date is before second date -> returns false
        assertFalse(CourseIsStillBeingTakenPredicate.dateEqualOrAfter(earlyDate, lateDate));
    }

    @Test
    public void dateEqualOrBefore() {
        DateTimeFormatter dtf = DateTimeFormatter
                .ofPattern("dd-MM-uuuu")
                .withResolverStyle(ResolverStyle.STRICT);

        LocalDate earlyDate = LocalDate.parse("01-01-2000", dtf);
        LocalDate lateDate = LocalDate.parse("02-01-2000", dtf);

        // same date -> returns true
        assertTrue(CourseIsStillBeingTakenPredicate.dateEqualOrBefore(earlyDate, earlyDate));

        // first date is after second date -> returns false
        assertFalse(CourseIsStillBeingTakenPredicate.dateEqualOrBefore(lateDate, earlyDate));

        // first date is before second date -> returns true
        assertTrue(CourseIsStillBeingTakenPredicate.dateEqualOrBefore(earlyDate, lateDate));
    }

    @Test
    public void test_courseHasNoStartDate_returnsTrue() {
        Course course = new Course("Valid Course");
        CourseIsStillBeingTakenPredicate predicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 1));

        assertTrue(predicate.test(course));
    }

    @Test
    public void test_givenDateBeforeCourseStartDateAndNoEndDate_returnsFalse() {
        Course course = new Course("Valid Course", "02-01-2000");
        CourseIsStillBeingTakenPredicate predicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 1));

        assertFalse(predicate.test(course));
    }

    @Test
    public void test_givenDateEqualsCourseStartDateAndNoEndDate_returnsTrue() {
        Course course = new Course("Valid Course", "01-01-2000");
        CourseIsStillBeingTakenPredicate predicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 1));

        assertTrue(predicate.test(course));
    }

    @Test
    public void test_givenDateAfterCourseStartDateAndNoEndDate_returnsTrue() {
        Course course = new Course("Valid Course", "01-01-2000");
        CourseIsStillBeingTakenPredicate predicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 2));

        assertTrue(predicate.test(course));
    }

    @Test
    public void test_givenDateBetweenStartDateAndEndDate_returnsTrue() {
        Course course = new Course("Valid Course", "01-01-2000", "03-01-2000");
        CourseIsStillBeingTakenPredicate startPredicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 1));
        CourseIsStillBeingTakenPredicate midPredicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 2));
        CourseIsStillBeingTakenPredicate endPredicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 3));

        assertTrue(startPredicate.test(course));
        assertTrue(midPredicate.test(course));
        assertTrue(endPredicate.test(course));
    }

    @Test
    public void test_givenDateNotBetweenStartDateAndEndDate_returnsFalse() {
        Course course = new Course("Valid Course", "02-01-2000", "03-01-2000");
        CourseIsStillBeingTakenPredicate beforeStartPredicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 1));
        CourseIsStillBeingTakenPredicate afterEndPredicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 4));

        assertFalse(beforeStartPredicate.test(course));
        assertFalse(afterEndPredicate.test(course));
    }

    @Test
    public void equals() {
        CourseIsStillBeingTakenPredicate predicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 1));

        // Same object -> returns true
        assertEquals(predicate, predicate);

        // Same date -> returns true
        assertEquals(predicate, new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 1)));

        // Different date -> returns false
        assertNotEquals(predicate, new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 2)));

        // Different type -> returns false
        assertNotEquals(predicate, 5);

        // null -> returns false
        assertNotEquals(predicate, null);
    }
}
