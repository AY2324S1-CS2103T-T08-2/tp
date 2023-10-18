package networkbook.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import networkbook.commons.core.index.Index;
import networkbook.commons.util.StringUtil;
import networkbook.logic.parser.exceptions.ParseException;
import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.GraduatingYear;
import networkbook.model.person.Link;
import networkbook.model.person.Name;
import networkbook.model.person.Phone;
import networkbook.model.person.Priority;
import networkbook.model.person.Specialisation;
import networkbook.model.tag.Tag;
import networkbook.model.util.UniqueList;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    // TODO: avoid returning null for optional fields
    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_EMAIL_DUPLICATE = "Your list of emails contains duplicates.\n"
            + "Please ensure that you do not input the same email more than once.";
    public static final String MESSAGE_LINK_DUPLICATE = "Your list of links contains duplicates.\n"
            + "Please ensure that you do not input the same link more than once.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        if (phone == null) {
            return null;
        }
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String link} into an {@code Link}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code link} is invalid.
     */
    public static Link parseLink(String link) throws ParseException {
        requireNonNull(link);
        String trimmedLink = link.trim();
        if (!Link.isValidLink(trimmedLink)) {
            throw new ParseException(Link.MESSAGE_CONSTRAINTS);
        }
        return new Link(trimmedLink);
    }

    /**
     * Parses a {@code Collection<String>} of links into {@code UniqueList<Link>}.
     * @throws ParseException if at least one link in {@code Collection<Link>} is invalid.
     */
    public static UniqueList<Link> parseLinks(Collection<String> links) throws ParseException {
        requireNonNull(links);
        if (!verifyNoDuplicates(links)) {
            throw new ParseException(MESSAGE_LINK_DUPLICATE);
        }
        UniqueList<Link> result = new UniqueList<>();
        for (String link: links) {
            result.add(parseLink(link));
        }
        return result;
    }

    /**
     * Parses a {@code String graduatingYear} into an {@code GraduatingYear}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code graduatingYear} is invalid.
     */
    public static GraduatingYear parseGraduatingYear(String graduatingYear) throws ParseException {
        if (graduatingYear == null) {
            return null;
        }
        String trimmedGraduatingYear = graduatingYear.trim();
        if (!GraduatingYear.isValidGraduatingYear(trimmedGraduatingYear)) {
            throw new ParseException(GraduatingYear.MESSAGE_CONSTRAINTS);
        }
        return new GraduatingYear(trimmedGraduatingYear);
    }

    /**
     * Parses a {@code String course} into an {@code Course}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code course} is invalid.
     */
    public static Course parseCourse(String course) throws ParseException {
        if (course == null) {
            return null;
        }
        String trimmedCourse = course.trim();
        if (!Course.isValidCourse(trimmedCourse)) {
            throw new ParseException(Course.MESSAGE_CONSTRAINTS);
        }
        return new Course(trimmedCourse);
    }

    /**
     * Parse a {@code String[] courseInfo} into a {@code Course}.
     *
     */
    public static Course parseCourseInfo(String[] courseInfo) throws IllegalArgumentException {
        try {
            if (courseInfo.length == 1) {
                return new Course(courseInfo[0]);
            } else if (courseInfo.length == 3) {
                return new Course(courseInfo[0],
                        new String[]{courseInfo[2].substring(0, 10)});
            } else {
                return new Course(courseInfo[0],
                        new String[]{courseInfo[2].substring(0, 10), courseInfo[4].substring(0, 10)});
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Parses a {@code String specialisation} into an {@code Specialisation}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code specialisation} is invalid.
     */
    public static Specialisation parseSpecialisation(String specialisation) throws ParseException {
        if (specialisation == null) {
            return null;
        }
        String trimmedSpecialisation = specialisation.trim();
        if (!Specialisation.isValidSpecialisation(trimmedSpecialisation)) {
            throw new ParseException(Specialisation.MESSAGE_CONSTRAINTS);
        }
        return new Specialisation(specialisation);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code Collection<String>} of emails into {@code UniqueList<Email>}.
     * Leading and trailing whitespaces will be trimmed.
     * @throws ParseException if at least one email in {@code Collection<String>} is invalid.
     */
    public static UniqueList<Email> parseEmails(Collection<String> emails) throws ParseException {
        requireNonNull(emails);
        if (!verifyNoDuplicates(emails)) {
            throw new ParseException(MESSAGE_EMAIL_DUPLICATE);
        }

        UniqueList<Email> result = new UniqueList<>();
        for (String email: emails) {
            result.add(parseEmail(email));
        }
        return result;
    }

    private static boolean verifyNoDuplicates(Collection<String> strings) {
        Object[] stringArr = strings.toArray();
        for (int i = 0; i < stringArr.length; i++) {
            for (int j = i + 1; j < stringArr.length; j++) {
                if (stringArr[i].equals(stringArr[j])) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String priority} into a {@code Priority}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Priority parsePriority(String priority) throws ParseException {
        if (priority == null) {
            return null;
        }
        String trimmedPriority = priority.trim();
        if (!Priority.isValidPriority(Priority.parsePriorityLevel(trimmedPriority))) {
            throw new ParseException(Priority.MESSAGE_CONSTRAINTS);
        }
        return new Priority(trimmedPriority);
    }
}
