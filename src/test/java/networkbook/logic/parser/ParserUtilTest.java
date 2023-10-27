package networkbook.logic.parser;

import static networkbook.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import networkbook.logic.parser.exceptions.ParseException;
import networkbook.model.person.Email;
import networkbook.model.person.Link;
import networkbook.model.person.Name;
import networkbook.model.person.PersonSortComparator.SortField;
import networkbook.model.person.PersonSortComparator.SortOrder;
import networkbook.model.person.Phone;
import networkbook.model.person.Priority;
import networkbook.model.person.Tag;
import networkbook.model.util.UniqueList;
import networkbook.testutil.TypicalIndexes;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_LINK = "facebookcom";
    private static final String INVALID_GRADUATION = "2024";
    private static final String INVALID_COURSE = "";
    private static final String INVALID_SPECIALISATION = "";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_PRIORITY = "hi";
    private static final String INVALID_SORT_FIELD = "";
    private static final String INVALID_SORT_ORDER = "";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_PHONE_2 = "98765432";
    private static final String VALID_LINK = "www.facebook.com/alice";
    private static final String VALID_LINK_2 = "https://www.google.com/?q=haha";
    private static final String VALID_GRADUATION = "AY9900-S2";
    private static final String VALID_COURSE = "Computer Science";
    private static final String VALID_SPECIALISATION = "Game Development";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_EMAIL_2 = "nkn@what.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "cs_god";
    private static final String VALID_TAG_3 = "hyphen-tag and space";
    private static final String VALID_PRIORITY = "meDIuM";
    private static final String VALID_SORT_FIELD = "nAme";
    private static final String VALID_SORT_ORDER = "asC";

    private static final String WHITESPACE = " \t\r\n";


    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(TypicalIndexes.INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(TypicalIndexes.INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone(null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parsePhones_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhones(null));
    }

    @Test
    public void parsePhones_collectionWithInvalidPhones_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhones(Arrays.asList(VALID_PHONE, INVALID_PHONE)));
    }

    @Test
    public void parsePhones_collectionWithDuplicates_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhones(Arrays.asList(VALID_PHONE, VALID_PHONE)));
    }

    @Test
    public void parsePhones_emptyCollection_returnsEmptyListOfPhones() throws Exception {
        assertEquals(new UniqueList<Phone>(), ParserUtil.parsePhones(Arrays.asList()));
    }

    @Test
    public void parsePhones_collectionWithValidPhones_returnsPhoneList() throws Exception {
        UniqueList<Phone> expectedList = new UniqueList<Phone>()
                .setItems(Arrays.asList(new Phone(VALID_PHONE), new Phone(VALID_PHONE_2)));
        UniqueList<Phone> actualList = ParserUtil.parsePhones(Arrays.asList(VALID_PHONE, VALID_PHONE_2));
        assertEquals(expectedList, actualList);
    }

    @Test
    public void parseLink_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseLink(null));
    }

    @Test
    public void parseLink_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseLink(INVALID_LINK));
    }

    @Test
    public void parseLink_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Link expectedLink = new Link(VALID_LINK);
        assertEquals(expectedLink, ParserUtil.parseLink(VALID_LINK));
    }

    @Test
    public void parseLink_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String linkWithWhitespace = WHITESPACE + VALID_LINK + WHITESPACE;
        Link expectedLink = new Link(VALID_LINK);
        assertEquals(expectedLink, ParserUtil.parseLink(linkWithWhitespace));
    }

    @Test
    public void parseLinks_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseLinks(null));
    }

    @Test
    public void parseLinks_collectionWithInvalidLinks_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseLinks(Arrays.asList(VALID_LINK, INVALID_LINK)));
    }

    @Test
    public void parseLinks_collectionWithDuplicates_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseLinks(Arrays.asList(VALID_LINK, VALID_LINK)));
    }

    @Test
    public void parseLinks_emptyCollection_returnsEmptyListOfLinks() throws Exception {
        assertEquals(new UniqueList<Link>(), ParserUtil.parseLinks(Arrays.asList()));
    }

    @Test
    public void parseLinks_collectionWithValidLinks_returnsLinkList() throws Exception {
        UniqueList<Link> expectedList = new UniqueList<Link>()
                .setItems(Arrays.asList(new Link(VALID_LINK), new Link(VALID_LINK_2)));
        UniqueList<Link> actualList = ParserUtil.parseLinks(Arrays.asList(VALID_LINK, VALID_LINK_2));
        assertEquals(expectedList, actualList);
    }

    @Test
    public void parseGraduation_null_returnsNull() throws Exception {
        assertEquals(null, ParserUtil.parseGraduation(null));
    }

    @Test
    public void parseGraduation_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseGraduation(INVALID_GRADUATION));
    }

    @Test
    public void parseCourse_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseCourse(INVALID_COURSE));
    }

    @Test
    public void parseCourses_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseCourses(null));
    }

    @Test
    public void parseCourses_collectionWithInvalidLinks_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseCourses(Arrays.asList(VALID_COURSE, INVALID_COURSE)));
    }

    @Test
    public void parseCourses_collectionWithDuplicates_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseCourses(Arrays.asList(VALID_COURSE, VALID_COURSE)));
    }

    @Test
    public void parseSpecialisation_null_returnsNull() throws Exception {
        assertEquals(null, ParserUtil.parseSpecialisation(null));
    }

    @Test
    public void parseSpecialisation_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseSpecialisation(INVALID_COURSE));
    }

    @Test
    public void parseSpecialisations_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseSpecialisations(null));
    }

    @Test
    public void parseSpecialisations_collectionWithInvalidLinks_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil
                .parseSpecialisations(Arrays.asList(VALID_SPECIALISATION, INVALID_SPECIALISATION)));
    }

    @Test
    public void parseSpecialisations_collectionWithDuplicates_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil
                .parseSpecialisations(Arrays.asList(VALID_SPECIALISATION, VALID_SPECIALISATION)));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseEmails_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmails(null));
    }

    @Test
    public void parseEmails_collectionWithInvalidEmails_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmails(Arrays.asList(VALID_EMAIL, INVALID_EMAIL)));
    }

    @Test
    public void parseEmails_collectionWithDuplicates_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmails(Arrays.asList(VALID_EMAIL, VALID_EMAIL)));
    }

    @Test
    public void parseEmails_emptyCollection_returnsEmptyListOfEmails() throws Exception {
        assertEquals(new UniqueList<Email>(), ParserUtil.parseEmails(Arrays.asList()));
    }

    @Test
    public void parseEmails_collectionWithValidEmails_returnsEmailList() throws Exception {
        UniqueList<Email> expectedList = new UniqueList<Email>()
                .setItems(Arrays.asList(new Email(VALID_EMAIL), new Email(VALID_EMAIL_2)));
        UniqueList<Email> actualList = ParserUtil.parseEmails(Arrays.asList(VALID_EMAIL, VALID_EMAIL_2));
        assertEquals(expectedList, actualList);
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        UniqueList<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2, VALID_TAG_3));
        UniqueList<Tag> expectedTagSet = new UniqueList<>();
        expectedTagSet.add(new Tag(VALID_TAG_1));
        expectedTagSet.add(new Tag(VALID_TAG_2));
        expectedTagSet.add(new Tag(VALID_TAG_3));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parseTags_collectionWithDuplicateTags_parseException() {
        assertThrows(ParseException.class, () ->
                ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2, VALID_TAG_1)));
    }

    @Test
    public void parsePriority_validValue_success() throws Exception {
        Priority expectedPriority = new Priority(VALID_PRIORITY);
        assertEquals(expectedPriority, ParserUtil.parsePriority(VALID_PRIORITY));
    }

    @Test
    public void parsePriority_validValueWithWhitespace_success() throws Exception {
        Priority expectedPriority = new Priority(VALID_PRIORITY);
        assertEquals(expectedPriority, ParserUtil.parsePriority(WHITESPACE + VALID_PRIORITY + WHITESPACE));
    }

    @Test
    public void parsePriority_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePriority(INVALID_PRIORITY));
    }

    @Test
    public void parseSortField_validValue_success() throws Exception {
        SortField expectedField = SortField.NAME;
        assertEquals(expectedField, ParserUtil.parseSortField(VALID_SORT_FIELD));
    }

    @Test
    public void parseSortField_validValueWithWhitespace_success() throws Exception {
        SortField expectedField = SortField.NAME;
        assertEquals(expectedField, ParserUtil.parseSortField(WHITESPACE + VALID_SORT_FIELD + WHITESPACE));
    }

    @Test
    public void parseSortField_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseSortField(INVALID_SORT_FIELD));
    }

    @Test
    public void parseSortField_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseSortField(null));
    }

    @Test
    public void parseSortOrder_validValue_success() throws Exception {
        SortOrder expectedOrder = SortOrder.ASCENDING;
        assertEquals(expectedOrder, ParserUtil.parseSortOrder(VALID_SORT_ORDER));
    }

    @Test
    public void parseSortOrder_validValueWithWhitespace_success() throws Exception {
        SortOrder expectedOrder = SortOrder.ASCENDING;
        assertEquals(expectedOrder, ParserUtil.parseSortOrder(WHITESPACE + VALID_SORT_ORDER + WHITESPACE));
    }

    @Test
    public void parseSortOrder_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseSortOrder(INVALID_SORT_ORDER));
    }

    @Test
    public void parseSortOrder_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseSortOrder(null));
    }
}
