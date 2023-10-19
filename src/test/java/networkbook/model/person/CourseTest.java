package networkbook.model.person;

import static networkbook.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CourseTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Course(null));
        assertThrows(NullPointerException.class, () -> new Course(null, "01-01-2000"));
        assertThrows(NullPointerException.class, () -> new Course("Valid Course", null));
        assertThrows(NullPointerException.class, () ->
                new Course("Valid Course", "01-01-2000", null));
        assertThrows(NullPointerException.class, () ->
                new Course("Valid Course", null, "01-01-2000"));
        assertThrows(NullPointerException.class, () ->
                new Course(null, "01-01-1999", "01-01-2000"));
    }

    @Test
    public void constructor_invalidCourse_throwsIllegalArgumentException() {
        // empty course
        assertThrows(IllegalArgumentException.class, () -> new Course(""));

        // course starts with empty space or consists of empty space only
        assertThrows(IllegalArgumentException.class, () -> new Course(" "));
        assertThrows(IllegalArgumentException.class, () -> new Course("     "));
        assertThrows(IllegalArgumentException.class, () -> new Course(" Computer Science"));

        // course contains multiple spacing between words
        assertThrows(IllegalArgumentException.class, () -> new Course("Computer  Science"));
    }

    @Test
    public void constructor_invalidDates_throwIllegalArgumentException() {
        String validCourse = "Valid Course";
        String validDate = "01-01-2000";

        // empty dates
        assertThrows(IllegalArgumentException.class, () -> new Course(validCourse, ""));
        assertThrows(IllegalArgumentException.class, () -> new Course(validCourse, validDate, ""));
        assertThrows(IllegalArgumentException.class, () -> new Course(validCourse, "", validDate));

        // other cases covered by isValidDate and areChronologicalDates
    }

    @Test
    public void isValidCourse() {
        // null course
        assertThrows(NullPointerException.class, () -> Course.isValidCourse(null));

        // invalid courses
        assertFalse(Course.isValidCourse("")); // empty string
        assertFalse(Course.isValidCourse(" ")); // spaces only
        assertFalse(Course.isValidCourse(" Computer Science")); // begins with empty space
        assertFalse(Course.isValidDate("Computer  Science")); // multiple space between words

        // valid courses
        assertTrue(Course.isValidCourse("Computer Science"));
        assertTrue(Course.isValidCourse("-")); // one character
        assertTrue(Course.isValidCourse("Double Degree Programme in Law and Liberal Arts")); // long course name
        assertTrue(Course
                .isValidCourse(",./;'[]{}:<>? +_)(*&%$#@!")); // symbols are allowed while spacing rule is obeyed
    }

    @Test
    public void isValidDate() {
        String validDate = "01-01-2000";
        String incorrectlyFormattedDate = "02-13-2000";
        String nonExistentDate = "30-02-2000";

        // null date returns false
        // Never happens in standard execution; checks for null values are done before this method is called.
        assertFalse(Course.isValidDate(null));

        // invalid dates
        assertFalse(Course.isValidDate(incorrectlyFormattedDate)); // date exists but is not DD-MM-YYYY format.
        assertFalse(Course.isValidDate(nonExistentDate)); // date is correctly formatted but doesn't exist.

        // anything that isn't a date or isn't the correct format is also invalid
        assertFalse(Course.isValidDate("Thursday"));
        assertFalse(Course.isValidDate("12 Dec 2012"));
        assertFalse(Course.isValidDate(""));
        assertFalse(Course.isValidDate("Computer"));
        assertFalse(Course.isValidDate("12-21-02001"));

        // valid dates
        assertTrue(Course.isValidDate(validDate));
        assertTrue(Course.isValidDate("12-12-2001"));
    }

    @Test
    public void areChronologicalDates() {
        String earlyDate = "01-01-2000";
        String midDate = "02-01-2000";
        String lateDate = "03-01-2000";

        // start date is later than end date -> return false
        assertFalse(Course.areChronologicalDates(lateDate, midDate));
        assertFalse(Course.areChronologicalDates(midDate, earlyDate));
        assertFalse(Course.areChronologicalDates(lateDate, earlyDate));

        // start and end dates are the same -> return false
        assertFalse(Course.areChronologicalDates(earlyDate, earlyDate));

        // start date is earlier than end date -> return true
        assertTrue(Course.areChronologicalDates(earlyDate, midDate));
        assertTrue(Course.areChronologicalDates(earlyDate, lateDate));
        assertTrue(Course.areChronologicalDates(midDate, lateDate));

        // invalid format -> throws exception
        assertThrows(IllegalArgumentException.class, () -> Course.areChronologicalDates(earlyDate, ""));
        assertThrows(IllegalArgumentException.class, () -> Course.areChronologicalDates(earlyDate, null));
        assertThrows(IllegalArgumentException.class, () -> Course.areChronologicalDates(earlyDate, "a"));
        assertThrows(IllegalArgumentException.class, () -> Course.areChronologicalDates(earlyDate, "20-02-02001"));
        assertThrows(IllegalArgumentException.class, () -> Course.areChronologicalDates(earlyDate, "12-13-2000"));
    }

    @Test
    public void toStringTest() {
        Course course = new Course("Valid Course");
        Course courseWithStart = new Course("Valid Course", "01-01-2000");
        Course courseWithBothDates = new Course("Valid Course", "01-01-2000", "02-01-2000");

        // equals to self
        assertEquals(course.toString(), course.toString());
        assertEquals(courseWithStart.toString(), courseWithStart.toString());
        assertEquals(courseWithBothDates.toString(), courseWithBothDates.toString());
        assertEquals(course.toString(), new Course("Valid Course").toString());
        assertEquals(courseWithBothDates.toString(),
                new Course("Valid Course", "01-01-2000", "02-01-2000").toString());

        // toString can produce equal outputs for unequal courses
        // not an issue since toString is not used to test equality
        assertEquals(courseWithStart.toString(), new Course("Valid Course (Started: 01-01-2000)").toString());

        // for most other scenarios the output is not equal
        assertNotEquals(course.toString(), courseWithStart.toString());
        assertNotEquals(course.toString(), courseWithBothDates.toString());
        assertNotEquals(courseWithStart.toString(), courseWithBothDates.toString());
        assertNotEquals(course.toString(), new Course("Other Valid Course").toString());
    }

    @Test
    public void equals() {
        Course course = new Course("Valid Course");
        Course courseWithStart = new Course("Valid Course", "01-01-2000");
        Course courseWithBothDates = new Course("Valid Course", "01-01-2000", "02-01-2000");

        // same course -> returns true
        assertEquals(course, new Course("Valid Course"));
        assertEquals(course, courseWithStart);
        assertEquals(course, courseWithBothDates);
        assertEquals(courseWithStart, courseWithBothDates);

        // same object -> returns true
        assertEquals(course, course);

        // null -> returns false
        assertNotEquals(course, null);

        // different types -> returns false
        assertNotEquals(course, 5.0f);

        // different course field -> returns false
        assertNotEquals(course, new Course("Other Valid Course"));
        assertNotEquals(courseWithStart, new Course("Valid Course (Started: 01-01-2000)"));
    }

    @Test
    public void hashcode() {
        // almost identical to equals
        Course course = new Course("Valid Course");
        Course courseWithStart = new Course("Valid Course", "01-01-2000");
        Course courseWithBothDates = new Course("Valid Course", "01-01-2000", "02-01-2000");

        // same course -> returns true
        assertEquals(course.hashCode(), new Course("Valid Course").hashCode());
        assertEquals(course.hashCode(), courseWithStart.hashCode());
        assertEquals(course.hashCode(), courseWithBothDates.hashCode());
        assertEquals(courseWithStart.hashCode(), courseWithBothDates.hashCode());

        // same object -> returns true
        assertEquals(course.hashCode(), course.hashCode());

        // 0 -> returns false
        assertNotEquals(course.hashCode(), 0);

        // different types -> returns false
        assertNotEquals(course.hashCode(), 5.0f);

        // different course field -> returns false
        assertNotEquals(course.hashCode(), new Course("Other Valid Course").hashCode());
        assertNotEquals(courseWithStart.hashCode(), new Course("Valid Course (Started: 01-01-2000)").hashCode());
    }

    @Test
    public void getCourse() {
        Course course = new Course("Valid Course");
        Course courseWithStart = new Course("Valid Course", "01-01-2000");
        Course courseWithBothDates = new Course("Valid Course", "01-01-2000", "02-01-2000");

        // similar to equals
        assertEquals(course.getCourse(), courseWithStart.getCourse());
        assertEquals(course.getCourse(), courseWithBothDates.getCourse());

        assertNotEquals(course.getCourse(), new Course("Other Valid Course").getCourse());
    }

    @Test
    public void getStartDate() {
        Course course = new Course("Valid Course");
        Course courseWithStart = new Course("Valid Course", "01-01-2000");
        Course courseWithBothDates = new Course("Valid Course", "01-01-2000", "02-01-2000");

        // no start date -> returns empty string
        assertEquals(course.getStartDate(), "");
        assertEquals(courseWithStart.getStartDate(), "01-01-2000");
        assertEquals(courseWithBothDates.getStartDate(), courseWithStart.getStartDate());

        assertEquals(courseWithStart.getStartDate(),
                new Course("Other Valid Course", "01-01-2000").getStartDate());

        assertNotEquals(courseWithStart.getStartDate(),
                new Course("Valid Course", "02-01-2000").getStartDate());
    }

    @Test
    public void getEndDate() {
        Course course = new Course("Valid Course");
        Course courseWithStart = new Course("Valid Course", "01-01-2000");
        Course courseWithBothDates = new Course("Valid Course", "01-01-2000", "02-01-2000");

        // no end date -> returns empty string
        assertEquals(course.getEndDate(), "");
        assertEquals(course.getEndDate(), courseWithStart.getEndDate());
        assertEquals(courseWithBothDates.getEndDate(), "02-01-2000");

        assertEquals(courseWithBothDates.getEndDate(),
                new Course("Other Valid Course", "01-01-2000", "02-01-2000").getEndDate());

        assertNotEquals(courseWithBothDates.getEndDate(),
                new Course("Valid Course", "01-01-2000", "03-01-2000").getEndDate());
    }

    @Test
    public void startDateExists() {
        Course course = new Course("Valid Course");
        Course courseWithStart = new Course("Valid Course", "01-01-2000");
        Course courseWithBothDates = new Course("Valid Course", "01-01-2000", "02-01-2000");

        assertFalse(course.startDateExists());
        assertTrue(courseWithStart.startDateExists());
        assertTrue(courseWithBothDates.startDateExists());
    }

    @Test
    public void endDateExists() {
        Course course = new Course("Valid Course");
        Course courseWithStart = new Course("Valid Course", "01-01-2000");
        Course courseWithBothDates = new Course("Valid Course", "01-01-2000", "02-01-2000");

        assertFalse(course.endDateExists());
        assertFalse(courseWithStart.endDateExists());
        assertTrue(courseWithBothDates.endDateExists());
    }
}
