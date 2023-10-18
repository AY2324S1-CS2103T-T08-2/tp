package networkbook.model.person;

import static networkbook.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.Test;


public class CourseTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Course(null));
    }

    @Test
    public void constructor_invalidCourse_throwsIllegalArgumentException() {
        String invalidCourse = "";
        assertThrows(IllegalArgumentException.class, () -> new Course(invalidCourse));
    }

    @Test
    public void isValidCourse() {
        // null course
        assertThrows(NullPointerException.class, () -> Course.isValidCourse(null));

        // invalid courses
        assertFalse(Course.isValidCourse("")); // empty string
        assertFalse(Course.isValidCourse(" ")); // spaces only
        assertFalse(Course.isValidCourse("Computer Science"));
        assertFalse(Course.isValidCourse("-")); // one character
        assertFalse(Course.isValidCourse("Double Degree Programme in Law and Liberal Arts")); // long address
        assertFalse(Course.isValidCourse("C1101")); // 1 letter faculty code
        assertFalse(Course.isValidCourse("CS101")); // Course number is not length 4
        assertFalse(Course.isValidCourse("CS1101AR")); // Variant code is more than one character long

        // valid courses
        assertTrue(Course.isValidCourse("CS1101")); // 2 letter faculty code
        assertTrue(Course.isValidCourse("DSA1201")); // 3 letter faculty code
        assertTrue(Course.isValidCourse("CS1101S")); // 2 letter faculty code + variant code at end
        assertTrue(Course.isValidCourse("PLS8001A")); // 3 letter faculty code + variant code at end
    }

    @Test
    public void isValidDate() {
        String validDate = "01-02-2000";
        String invalidDate = "12-13-2000";
        String nonexistentDate = "30-02-2000";

        // date follows DD-MM-YYYY format -> returns true
        assertTrue(Course.isValidDate(validDate));

        // date exists but does not follow DD-MM-YYYY format -> returns false
        assertFalse(Course.isValidDate(invalidDate));

        // date does not exist -> returns false
        assertFalse(Course.isValidDate(nonexistentDate));

        // null -> returns false
        assertFalse(Course.isValidDate(null));

        // invalid input -> returns false
        assertFalse(Course.isValidDate("12345"));
    }

    @Test
    public void areChronologicalDates(){
        String earlyDate = "01-01-2000";
        String midDate = "01-02-2000";
        String lateDate = "01-03-2000";

        // dates follow DD-MM-YYYY format and second date is after first date -> returns true
        assertTrue(Course.areChronologicalDates(earlyDate, midDate));
        assertTrue(Course.areChronologicalDates(earlyDate, lateDate));
        assertTrue(Course.areChronologicalDates(midDate, lateDate));

        // dates follow format but are not sequential -> returns false
        assertFalse(Course.areChronologicalDates(midDate, earlyDate));
        assertFalse(Course.areChronologicalDates(lateDate, midDate));
        assertFalse(Course.areChronologicalDates(lateDate, earlyDate));

        // same date -> returns false
        assertFalse(Course.areChronologicalDates(earlyDate, earlyDate));

        // one or both invalid data types -> throws exception
        assertThrows(IllegalArgumentException.class, () -> Course.areChronologicalDates(null, null));
        assertThrows(IllegalArgumentException.class, () -> Course.areChronologicalDates(null, "e"));
        assertThrows(IllegalArgumentException.class, () -> Course.areChronologicalDates("1", "e"));
    }

    @Test
    public void equals() {
        Course course = new Course("CS1101");

        // same courseCode -> returns true
        assertTrue(course.equals(new Course("CS1101")));

        // same object -> returns true
        assertTrue(course.equals(course));

        // null -> returns false
        assertFalse(course.equals(null));

        // different types -> returns false
        assertFalse(course.equals(5.0f));

        // different values -> returns false
        assertFalse(course.equals(new Course("CS1231S")));

        // same courseCode but different dates -> returns true
        assertTrue(course.equals(new Course("CS1101", new String[]{"12-04-2000"})));
        assertTrue(course.equals(new Course("CS1101", new String[]{"12-04-2000", "12-08-2000"})));

        // different courseCode but same dates -> return false
        Course courseWithOneDate = new Course("CS1101", new String[]{"12-04-2000"});
        Course courseWithTwoDates = new Course("CS1101", new String[]{"12-04-2000", "12-08-2000"});
        assertFalse(courseWithOneDate.equals(new Course("CS1231", new String[]{"12-04-2000"})));
        assertFalse(courseWithTwoDates.equals(new Course("CS1231", new String[]{"12-04-2000", "12-08-2000"})));
    }
}
