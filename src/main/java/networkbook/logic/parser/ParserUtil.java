package networkbook.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;

import networkbook.commons.core.index.Index;
import networkbook.commons.util.StringUtil;
import networkbook.logic.parser.exceptions.ParseException;
import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.Graduation;
import networkbook.model.person.Link;
import networkbook.model.person.Name;
import networkbook.model.person.PersonSortComparator;
import networkbook.model.person.PersonSortComparator.SortField;
import networkbook.model.person.PersonSortComparator.SortOrder;
import networkbook.model.person.Phone;
import networkbook.model.person.Priority;
import networkbook.model.person.Specialisation;
import networkbook.model.person.Tag;
import networkbook.model.util.UniqueList;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    // TODO: avoid returning null for optional fields
    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_PHONE_DUPLICATE = "Your list of phones contains duplicates.\n"
            + "Please ensure that you do not input the same phone more than once.";
    public static final String MESSAGE_EMAIL_DUPLICATE = "Your list of emails contains duplicates.\n"
            + "Please ensure that you do not input the same email more than once.";
    public static final String MESSAGE_LINK_DUPLICATE = "Your list of links contains duplicates.\n"
            + "Please ensure that you do not input the same link more than once.";

    public static final String MESSAGE_TAG_DUPLICATE = "Your list of tags contains duplicates. \n"
            + "Please ensure that you do not input the same tag more than once.";

    public static final String MESSAGE_SPEC_DUPLICATE = "Your list of specialisations contains duplicates. \n"
            + "Please ensure that you do not input the same specialisation more than once.";

    public static final String MESSAGE_COURSE_DUPLICATE = "Your list of courses contains duplicates. \n"
            + "Please ensure that you do not input the same course more than once.";

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
     * Parses a {@code String phone} into an {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code Collection<String>} of phones into {@code UniqueList<Phone>}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if at least one phone in {@code Collection<String>} is invalid.
     */
    public static UniqueList<Phone> parsePhones(Collection<String> phones) throws ParseException {
        requireNonNull(phones);
        if (!verifyNoDuplicates(phones)) {
            throw new ParseException(MESSAGE_PHONE_DUPLICATE);
        }
        UniqueList<Phone> result = new UniqueList<>();
        for (String phone: phones) {
            result.add(parsePhone(phone));
        }
        return result;
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
     * Parses a {@code String graduation} into a {@code Graduation}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code graduation} is invalid.
     */
    public static Graduation parseGraduation(String graduation) throws ParseException {
        if (graduation == null) {
            return null;
        }
        String normalizedGraduation = graduation.trim().toUpperCase();
        if (!Graduation.isValidGraduation(normalizedGraduation)) {
            throw new ParseException(Graduation.MESSAGE_CONSTRAINTS);
        }
        return new Graduation(normalizedGraduation);
    }

    /**
     * Parses {@code String courses} into {@code UniqueList<Course>}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code course} is invalid.
     */
    public static UniqueList<Course> parseCourses(Collection<String> courses) throws ParseException {
        requireNonNull(courses);
        if (!verifyNoDuplicates(courses)) {
            throw new ParseException(MESSAGE_COURSE_DUPLICATE);
        }
        UniqueList<Course> result = new UniqueList<>();
        for (String link : courses) {
            result.add(parseCourse(link));
        }
        return result;
    }

    /**
     * Parses a {@code course} from {@code String} into a {@code Course}.
     * @throws ParseException When the {@code course} is invalid.
     */
    public static Course parseCourse(String course) throws ParseException {
        requireNonNull(course);
        String trimmedCourse = course.trim();
        if (!Course.isValidCourse(trimmedCourse)) {
            throw new ParseException(Course.MESSAGE_CONSTRAINTS);
        }
        return new Course(trimmedCourse);
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
     * Parses a {@code Collection<String>} of specialisations into {@code UniqueList<Specialisation>}.
     *
     * @throws ParseException if at least one specialisation in {@code Collection<String>} is invalid.
     */
    public static UniqueList<Specialisation> parseSpecialisations(Collection<String> specialisations)
            throws ParseException {
        requireNonNull(specialisations);
        if (!verifyNoDuplicates(specialisations)) {
            throw new ParseException(MESSAGE_SPEC_DUPLICATE);
        }
        UniqueList<Specialisation> result = new UniqueList<>();
        for (String spec : specialisations) {
            result.add(parseSpecialisation(spec));
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
     * Parses {@code Collection<String> tags} into a {@code UniqueList<Tag>}.
     */
    public static UniqueList<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        if (!verifyNoDuplicates(tags)) {
            throw new ParseException(MESSAGE_TAG_DUPLICATE);
        }
        final UniqueList<Tag> tagSet = new UniqueList<>();
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


    /**
     * Parses a {@code String field} into a {@code SortField}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static SortField parseSortField(String field) throws ParseException {
        requireNonNull(field);
        String normalizedField = field.trim().toLowerCase();
        SortField sortField = PersonSortComparator.parseSortField(normalizedField);
        if (!PersonSortComparator.isValidSortField(sortField)) {
            throw new ParseException(PersonSortComparator.MESSAGE_CONSTRAINTS_FIELD);
        }
        return sortField;
    }

    /**
     * Parses a {@code String order} into a {@code SortOrder}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static SortOrder parseSortOrder(String order) throws ParseException {
        requireNonNull(order);
        String normalizedOrder = order.trim().toLowerCase();
        SortOrder sortOrder = PersonSortComparator.parseSortOrder(normalizedOrder);
        if (!PersonSortComparator.isValidSortOrder(sortOrder)) {
            throw new ParseException(PersonSortComparator.MESSAGE_CONSTRAINTS_ORDER);
        }
        return sortOrder;
    }
}
