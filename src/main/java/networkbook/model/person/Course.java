package networkbook.model.person;

import static java.util.Objects.requireNonNull;
import static networkbook.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
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
     * Construct a {@code Course}.
     *
     * @param course A valid course description.
     */
    public Course(String course) {
        requireNonNull(course);
        checkArgument(isValidCourse(course), MESSAGE_CONSTRAINTS);
        this.course = course;
        startDate = Optional.empty();
        endDate = Optional.empty();
    }

    /**
     * Construct a {@code Course} with start date
     *
     * @param course A valid course description.
     * @param startDate The date when the contact started taking the course.
     */
    public Course(String course, String startDate) {
        requireNonNull(course);
        requireNonNull(startDate);
        checkArgument(isValidCourse(course), MESSAGE_CONSTRAINTS);
        checkArgument(isValidDate(startDate), DATE_CONSTRAINTS);
        this.course = course;
        this.startDate = Optional.of(startDate);
        this.endDate = Optional.empty();
    }

    /**
     * Construct a {@code Course} with start date and end date
     *
     * @param course A valid course description.
     * @param startDate The date when the contact started taking the course.
     * @param endDate The date after startDate when the contact finished the course.
     */
    public Course(String course, String startDate, String endDate) {
        requireNonNull(course);
        requireNonNull(startDate);
        requireNonNull(endDate);
        checkArgument(isValidCourse(course), MESSAGE_CONSTRAINTS);
        checkArgument(isValidDate(startDate), DATE_CONSTRAINTS);
        checkArgument(isValidDate(endDate), DATE_CONSTRAINTS);
        checkArgument(areChronologicalDates(startDate, endDate), DATE_TIMING_CONSTRAINTS);
        this.course = course;
        this.startDate = Optional.of(startDate);
        this.endDate = Optional.of(endDate);
    }

    /**
     * Return true if a given string is a valid course.
     */
    public static boolean isValidCourse(String test) {
        if (test.startsWith(" ")) {
            return false;
        }
        for (String word : test.split(" ")) {
            if ((word.equals(""))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return true if the given string is a valid date.
     * A valid date must follow the DD-MM-YYYY format to avoid ambiguity, and actually exist.
     *
     * @param date A given string to be tested for date validity and format.
     */
    public static boolean isValidDate(String date) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter
                    .ofPattern("dd-MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            return dtf.format(dtf.parse(date)).equals(date);
        } catch (DateTimeParseException | NullPointerException e) {
            return false;
        }
    }

    /**
     * Return true if the second given string corresponds with a date that is later than the first date.
     *
     * @param start A string representing an earlier date.
     * @param end A string representing a later date.
     * @throws IllegalArgumentException if DD-MM-YYYY format is not followed for either date.
     */
    public static boolean areChronologicalDates(String start, String end) throws IllegalArgumentException {
        try {
            DateTimeFormatter dtf = DateTimeFormatter
                    .ofPattern("dd-MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate startDate = LocalDate.parse(start, dtf);
            LocalDate endDate = LocalDate.parse(end, dtf);
            return endDate.isAfter(startDate);
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("Both dates must follow DD-MM-YYYY format");
        }
    }

    @Override
    public String toString() {
        StringBuilder tsb = new StringBuilder(course);
        String start = startDateExists() ? " (Started: " + startDate.get() + ")" : "";
        String end = endDateExists() ? " (Ended: " + endDate.get() + ")" : "";
        return tsb.append(start).append(end).toString();
    }

    /**
     * Return true if two Courses have the same 'course' field
     *
     * @param other The object that is being compared to for equality
     * @return True if the other object is a Course with the same 'course' field
     */
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

    public String getStartDate() {
        return startDateExists() ? startDate.get() : "";
    }

    public String getEndDate() {
        return endDateExists() ? endDate.get() : "";
    }

    /*
     * Check the existence of start and end dates.
     */
    public boolean startDateExists() {
        return startDate.isPresent();
    }

    public boolean endDateExists() {
        return endDate.isPresent();
    }

}
