package networkbook.model.person;

import static java.util.Objects.requireNonNull;
import static networkbook.commons.util.AppUtil.checkArgument;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * Represents a Person's course of study in the network book.
 * Guarantees: immutable; is valid as declared in {@link #isValidCourse(String)}
 */
public class Course {

    public static final String MESSAGE_CONSTRAINTS =
            "Courses should follow the format [Faculty code + Course number + Variant] [Start date] [End date]"
            + "and adhere to the following constraints:\n"
            + "1. Courses must be alphanumeric characters and in all uppercase\n"
            + "2. Faculty code can only be 2 or 3 letters long\n"
            + "3. Course number must be 4 numbers long\n"
            + "4. Variant code (optional) is a single letter\n"
            + "5. Start date and End date are optional and must follow the format DD-MM-YYYY";
    public static final String DATE_CONSTRAINTS = "End date should occur after the start date.";

    /*
     * The first 2-3 characters of a course must be uppercase.
     * The following 4 characters must be numbered.
     * The course may or may not end with a uppercase variant letter.
     */
    public static final String VALIDATION_REGEX = "^[A-Z]{2,3}[0-9]{4}[A-Z]?$";

    public final String courseCode;
    private Optional<String> startDate;
    private Optional<String> endDate;

    /**
     * Constructs an {@code Course}.
     *
     * @param course A valid course with no start or end date.
     */
    public Course(String course) {
        requireNonNull(course);
        checkArgument(isValidCourse(course), MESSAGE_CONSTRAINTS);
        courseCode = course;
        startDate = Optional.empty();
        endDate = Optional.empty();
    }

    /**
     * Constructs an {@code Course} with start and/or end dates.
     *
     * @param
     */
    public Course(String course, String[] dates) {
        requireNonNull(course);
        checkArgument(isValidCourse(course), MESSAGE_CONSTRAINTS);
        courseCode = course;
        assert (dates.length > 0 && dates.length <= 2);
        startDate = Optional.empty();
        endDate = Optional.empty();
        for (String date : dates) {
            requireNonNull(date);
            checkArgument(isValidDate(date), MESSAGE_CONSTRAINTS);
            if (startDate.isEmpty()) {
                startDate = Optional.ofNullable(date);
            } else {
                endDate = Optional.ofNullable(date);
            }
        }
        if (!endDate.isEmpty()) {
            checkArgument(areChronologicalDates(startDate.get(), endDate.get()), DATE_CONSTRAINTS);;
        }
    }

    /**
     * Returns true if a given string is a valid course.
     */
    public static boolean isValidCourse(String course) {
        return course.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid date.
     * For defensive purposes the format of the date will be limited to DD-MM-YYYY format.
     */
    public static boolean isValidDate(String date) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return dtf.format(dtf.parse(date)).equals(date);
        } catch (DateTimeParseException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Check that endDate occurs after startDate
     */
    public static boolean areChronologicalDates(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return sdf.parse(end).after(sdf.parse(start));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Both dates must follow DD-MM-YYYY format");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Both dates must follow DD-MM-YYYY format");
        }
    }

    @Override
    public String toString() {
        String start = startDate.isEmpty() ? "" : " (Started: " + startDate.get() + ")";
        String end = endDate.isEmpty() ? "" : " (Ended: " + endDate.get() + ")";
        return courseCode + start + end;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Course)) {
            return false;
        }

        Course otherCourse = (Course) other;
        return courseCode.equals(otherCourse.courseCode);
    }

    @Override
    public int hashCode() {
        return courseCode.hashCode();
    }

}
