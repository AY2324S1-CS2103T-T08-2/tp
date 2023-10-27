package networkbook.model.person.filter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.function.Predicate;

import networkbook.model.person.Course;

/**
 * Checks that a course is still being taken by the given date.
 *
 * Assumption: No start date -> Course is still being taken.
 * Otherwise returns true when the given date is between the
 */
public class CourseIsStillBeingTakenPredicate implements Predicate<Course> {
    private final LocalDate dateOfFinish;
    private final DateTimeFormatter dtf = DateTimeFormatter
            .ofPattern("dd-MM-uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Creates a predicate that returns true for a course whenever the given date lies in the range
     * between the course's start and end date.
     */
    public CourseIsStillBeingTakenPredicate(LocalDate dateOfFinish) {
        assert dateOfFinish != null : "Date should not be null";
        this.dateOfFinish = dateOfFinish;
    }

    /**
     * Returns true if the first date is after or the same date as the second date.
     */
    public static boolean dateEqualOrAfter(LocalDate date1, LocalDate date2) {
        assert date1 != null || date2 != null : "Dates should not be null";
        return date1.isAfter(date2) || date1.isEqual(date2);
    }

    /**
     * Returns true if the first date is before or the same date as the second date.
     */
    public static boolean dateEqualOrBefore(LocalDate date1, LocalDate date2) {
        assert date1 != null || date2 != null : "Dates should not be null";
        return date1.isBefore(date2) || date1.isEqual(date2);
    }

    /**
     * Tests given course's start and end date and returns true if the
     * specified date of this predicate is between the start and end date.
     *
     * If there is no start and end date, the function always returns true.
     */
    public boolean test(Course course) {
        assert course != null : "Course should not be null";
        if (!course.startDateExists()) {
            return true;
        }
        if (!course.endDateExists()) {
            return dateEqualOrAfter(dateOfFinish, LocalDate.parse(course.getStartDate(), dtf));
        }
        return dateEqualOrAfter(dateOfFinish, LocalDate.parse(course.getStartDate(), dtf))
                && dateEqualOrBefore(dateOfFinish, LocalDate.parse(course.getEndDate(), dtf));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CourseIsStillBeingTakenPredicate)) {
            return false;
        }

        CourseIsStillBeingTakenPredicate otherPredicate = (CourseIsStillBeingTakenPredicate) other;
        return dateOfFinish.isEqual(otherPredicate.dateOfFinish);
    }
}
