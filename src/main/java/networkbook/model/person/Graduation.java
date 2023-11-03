package networkbook.model.person;

import static java.util.Objects.requireNonNull;
import static networkbook.commons.util.AppUtil.checkArgument;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a Person's graduation year and semester in the network book.
 * Guarantees: immutable; is valid as declared in {@link #isValidGraduation(gradString)}
 */
public class Graduation implements Comparable<Graduation> {

    public static final String MESSAGE_CONSTRAINTS = "Graduation dates should be of the format AYxxxx-Sy where:\n"
            + "1. xxxx is the 4-digit representation of the 2 calendar years in the academic year,\n"
            + "    e.g. 2223 for Academic Year 2022/2023. Years must be between 1970-2069 (inclusive).\n"
            + "2. y is either 1 or 2 for Semester 1 and Semester 2 respectively.";

    private static final String AY_REGEX = "AY([0-9]{2})([0-9]{2})-S([12])";
    private static final int YEAR_PART_HI = 2000;
    private static final int YEAR_PART_LO = 1900;
    private static final int YEAR_THRESHOLD = 70;

    /**
     * First calendar year of the academic year, e.g. 2022 for AY2022/2023.
    */
    private final int acadYearStart;

    /**
     * Second calendar year of the academic year, e.g. 2023 for AY2022/2023.
    */
    private final int acadYearEnd;

    /** Semester of graduation */
    private final Semester semester;

    /**
     * Constructs a {@code Graduation}.
     *
     * @param gradString A valid String representation of a graduation year and semester,
     *     e.g. {@code AY2526-S2}.
     */
    public Graduation(String gradString) {
        requireNonNull(gradString);
        checkArgument(isValidGraduation(gradString), MESSAGE_CONSTRAINTS);
        acadYearStart = parseAcadYearStart(gradString);
        acadYearEnd = parseAcadYearEnd(gradString);
        semester = parseSemester(gradString);
    }

    /**
     * Matches input gradString using AY_REGEX and returns specific group within match.
     *
     * @param gradString string to match
     * @param group number of group to match (1-3)
     * @return matched group
     */
    private static String matchGroup(String gradString, int group) {
        if (group < 1 || group > 3) {
            return null;
        }
        String normalizedGradString = gradString.trim().toUpperCase();
        Pattern pattern = Pattern.compile(AY_REGEX);
        Matcher matcher = pattern.matcher(normalizedGradString);
        if (!matcher.matches()) {
            return null;
        }
        String match = matcher.group(group);
        return match;
    }

    /**
     * Parses user input {@code gradString} into an int representing the first calendar year of the academic year.
     *
     * @param gradString provided in user command
     * @return corresponding first calendar year
     */
    public static int parseAcadYearStart(String gradString) {
        String ayStart = matchGroup(gradString, 1);
        String ayEnd = matchGroup(gradString, 2);
        int ayStartShortInt = Integer.parseInt(ayStart);
        int ayEndShortInt = Integer.parseInt(ayEnd);
        int addedPart = (ayStartShortInt < YEAR_THRESHOLD) ? YEAR_PART_HI : YEAR_PART_LO; // 1970 - 2069
        int ayStartFull = ayStartShortInt + addedPart;
        int ayEndFull = ayEndShortInt + addedPart;
        return ayStartFull;
    }


    /**
     * Parses user input {@code gradString} into an int representing the second calendar year of the academic year.
     *
     * @param gradString provided in user command
     * @return corresponding second calendar year
     */
    public static int parseAcadYearEnd(String gradString) {
        String ayStart = matchGroup(gradString, 1);
        String ayEnd = matchGroup(gradString, 2);
        int ayStartShortInt = Integer.parseInt(ayStart);
        int ayEndShortInt = Integer.parseInt(ayEnd);
        int addedPart = (ayEndShortInt <= YEAR_THRESHOLD) ? YEAR_PART_HI : YEAR_PART_LO; // 1971 - 2070
        int ayStartFull = ayStartShortInt + addedPart;
        int ayEndFull = ayEndShortInt + addedPart;
        return ayEndFull;
    }

    /**
     * Parses user input {@code gradString} into a {@code Semester}.
     *
     * @param gradString provided in user command
     * @return corresponding semester
     */
    public static Semester parseSemester(String gradString) {
        String match = matchGroup(gradString, 3);
        if (match == null) {
            return Semester.INVALID;
        }
        switch (match) {
        case "1":
            return Semester.S1;
        case "2":
            return Semester.S2;
        default:
            return Semester.INVALID;
        }
    }

    /**
     * Returns true if a given string representation of
     * a graduation year and semester is valid.
     */
    public static boolean isValidGraduation(String gradString) {
        String normalizedGradString = gradString.trim().toUpperCase();
        if (normalizedGradString.matches(AY_REGEX)) {
            int year1 = parseAcadYearStart(gradString);
            int year2 = parseAcadYearEnd(gradString);
            Semester sem = parseSemester(gradString);
            return isValidGraduationYear(year1, year2) && isValidGraduationSemester(sem);
        } else {
            return false;
        }
    }

    /**
     * Returns true if a pair of calendar years is valid (consecutive).
     */
    public static boolean isValidGraduationYear(int year1, int year2) {
        return year2 - year1 == 1;
    }

    /**
     * Returns true if a given graduation semester is valid.
     */
    public static boolean isValidGraduationSemester(Semester semester) {
        requireNonNull(semester);
        return semester != Semester.INVALID;
    }

    /**
     * Returns human-readable representation of graduation year, e.g. "AY2022/2023 Semester 1".
     */
    public String getFullString() {
        assert isValidGraduationSemester(semester);
        StringBuilder s = new StringBuilder("AY");
        s.append(acadYearStart);
        s.append("/");
        s.append(acadYearEnd);
        s.append(" Semester ");
        s.append(semester.value);
        return s.toString();
    }

    @Override
    public String toString() {
        assert isValidGraduationSemester(semester);
        StringBuilder s = new StringBuilder("AY");
        s.append(String.format("%02d", acadYearStart % 100)); // Get last 2 digits
        s.append(String.format("%02d", acadYearEnd % 100));
        s.append("-S");
        s.append(semester.value);
        return s.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof Graduation)) {
            return false;
        }

        Graduation other = (Graduation) obj;
        if (!Objects.equals(acadYearStart, other.acadYearStart)) {
            return false;
        }
        if (!Objects.equals(acadYearEnd, other.acadYearEnd)) {
            return false;
        }
        if (!Objects.equals(semester, other.semester)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(acadYearStart, acadYearEnd, semester);
    }

    /**
     * Enumerates the possible graduation semesters of a contact.
     */
    public enum Semester {
        S1(1),
        S2(2),
        INVALID(0);

        public final int value;
        private Semester(int value) {
            this.value = value;
        }
    }

    @Override
    public int compareTo(Graduation o) {
        if (acadYearStart < o.acadYearStart) {
            return -1;
        } else if (acadYearStart > o.acadYearStart) {
            return 1;
        } else {
            if (semester.value < o.semester.value) {
                return -1;
            } else if (semester.value > o.semester.value) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public int getGradYear() {
        return semester == Semester.S1 ? acadYearStart : acadYearEnd;
    }
}
