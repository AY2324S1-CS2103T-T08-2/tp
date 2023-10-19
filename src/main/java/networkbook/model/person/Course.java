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
                    + "Additionally, only the first letter of each word may be uppercase,\n"
                    + "and more than one space between each word is not allowed\n"
                    + "Acronyms (all uppercase) are also allowed.";

    /*
     * The first character of the specialisation must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     * Proceeding letters of a word should not be uppercase, unless...
     */
    public static final String NOT_ALL_CAPITALIZED_REGEX = "^[A-Za-z0-9][a-z0-9.,_-]{0,99}$";
    /*
     * The word is in all uppercase, such as for acronyms are also acceptable.
     */
    public static final String ALL_CAPITALIZED_REGEX = "^[A-Z]+$";

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
        if (test.startsWith(" ")) {
            return false;
        }
        for (String word : test.split(" ")) {
            if (!(word.matches(NOT_ALL_CAPITALIZED_REGEX) || word.matches(ALL_CAPITALIZED_REGEX))) {
                return false;
            }
        }
        return true;
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
