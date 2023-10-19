package networkbook.model.person;

import static java.util.Objects.requireNonNull;
import static networkbook.commons.util.AppUtil.checkArgument;

import java.util.Optional;

/**
 * Represents a Person's course of study in the network book.
 * Guarantees: immutable; is valid as declared in {@link #isValidCourse(String)}
 */
public class Course {

    public static final String MESSAGE_CONSTRAINTS =
            "Courses can take any value, but should not be blank.\n"
            + "Additionally, multiple spaces is not allowed, and the course may not start with spaces";
    public static final String DATE_CONSTRAINTS =
            "Dates must always follow the DD-MM-YYYY format.";
    public static final String DATE_TIMING_CONSTRAINTS =
            "End date of the course must occur after the start date of the course!";

    private final String course;
    private final Optional<String> startDate;
    private final Optional<String> endDate;

    /**
     * Constructs a {@code Course}.
     *
     * @param course A valid course.
     */
    public Course(String course) {
        requireNonNull(course);
        checkArgument(isValidCourse(course), MESSAGE_CONSTRAINTS);
        this.course = course;
        startDate = Optional.empty();
        endDate = Optional.empty();
    }

    /**
     * Constructs a {@code Course} with start date
     */
    public Course(String course, String startDate) {
        requireNonNull(course);
        requireNonNull(startDate);
        checkArgument(isValidCourse(course), MESSAGE_CONSTRAINTS);
        // TODO: check validity of dates
        this.course = course;
        this.startDate = Optional.of(startDate);
        this.endDate = Optional.empty();
    }

    /**
     * Constructs a {@code Course} with start date and end date
     */
    public Course(String course, String startDate, String endDate) {
        requireNonNull(course);
        requireNonNull(startDate);
        checkArgument(isValidCourse(course), MESSAGE_CONSTRAINTS);
        // TODO: check validity of dates and ensure endDate > startDate
        this.course = course;
        this.startDate = Optional.of(startDate);
        this.endDate = Optional.of(endDate);
    }

    /**
     * Returns true if a given string is a valid course.
     */
    public static boolean isValidCourse(String test) {
        for (String word : test.split(" ")) {
            if (!(word.equals(""))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the given string is a valid date.
     * A valid date must follow the DD-MM-YYYY format to avoid ambiguity, and actually exist.
     *
     * @param date A given string to be tested for date validity and format.
     */
    public static boolean isValidDate(String date) {
        return false;
    }

    @Override
    public String toString() {
        return course;
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
        return course.equals(otherCourse.course);
    }

    @Override
    public int hashCode() {
        return course.hashCode();
    }

    public String getCourse() {
        return course;
    }

}
