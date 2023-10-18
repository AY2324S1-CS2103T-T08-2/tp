package networkbook.storage;

import static networkbook.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import networkbook.commons.exceptions.IllegalValueException;
import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.GraduatingYear;
import networkbook.model.person.Link;
import networkbook.model.person.Name;
import networkbook.model.person.Phone;
import networkbook.model.person.Specialisation;
import networkbook.model.tag.Tag;
import networkbook.testutil.TypicalPersons;

// TODO: add/amend tests based on individual attribute specification
public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final List<JsonAdaptedProperty<Link>> INVALID_LINKS = List.of(
            new JsonAdaptedProperty<Link>("facebookcom")
    );
    private static final String INVALID_GRADUATING_YEAR = "123a";
    private static final String INVALID_COURSE = "";
    private static final String INVALID_SPECIALISATION = "";
    private static final List<JsonAdaptedProperty<Email>> INVALID_EMAILS =
            List.of(new JsonAdaptedProperty<>("example.com"));
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_PRIORITY = "hi";

    private static final String VALID_NAME = TypicalPersons.BENSON.getName().toString();
    private static final String VALID_PHONE = TypicalPersons.BENSON.getPhone().map(Phone::toString).orElse(null);
    private static final List<JsonAdaptedProperty<Email>> VALID_EMAILS = TypicalPersons.BENSON.getEmails().stream()
            .map(JsonAdaptedProperty::new)
            .collect(Collectors.toList());
    private static final List<JsonAdaptedProperty<Link>> VALID_LINKS = TypicalPersons.BENSON.getLinks().stream()
            .map(JsonAdaptedProperty::new)
            .collect(Collectors.toList());
    private static final String VALID_GRADUATING_YEAR = "2000";
    private static final String VALID_COURSE = "CS1101S";
    private static final String VALID_SPECIALISATION = "Game Development";
    private static final List<JsonAdaptedProperty<Tag>> VALID_TAGS = TypicalPersons.BENSON.getTags().stream()
            .map(JsonAdaptedProperty::new)
            .collect(Collectors.toList());
    private static final String VALID_PRIORITY = "mEDiUM";

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(TypicalPersons.BENSON);
        assertEquals(TypicalPersons.BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAILS,
                        VALID_LINKS, VALID_GRADUATING_YEAR, VALID_COURSE,
                        VALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE, VALID_EMAILS,
                VALID_LINKS, VALID_GRADUATING_YEAR, VALID_COURSE,
                VALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = String.format(
                JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT,
                Name.class.getSimpleName()
        );
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAILS,
                        VALID_LINKS, VALID_GRADUATING_YEAR, VALID_COURSE,
                        VALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmails_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAILS,
                        VALID_LINKS, VALID_GRADUATING_YEAR, VALID_COURSE,
                        VALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidLink_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAILS,
                        INVALID_LINKS, VALID_GRADUATING_YEAR, VALID_COURSE,
                        VALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = Link.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }


    @Test
    public void toModelType_invalidGraduatingYear_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAILS,
                        VALID_LINKS, INVALID_GRADUATING_YEAR, VALID_COURSE,
                        VALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = GraduatingYear.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }
    @Test
    public void toModelType_invalidCourse_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAILS,
                        VALID_LINKS, VALID_GRADUATING_YEAR, INVALID_COURSE,
                        VALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = Course.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidSpecialisation_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAILS,
                        VALID_LINKS, VALID_GRADUATING_YEAR, VALID_COURSE,
                        INVALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = Specialisation.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }


    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedProperty<Tag>> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedProperty<>(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAILS,
                        VALID_LINKS, VALID_GRADUATING_YEAR, VALID_COURSE,
                        VALID_SPECIALISATION, invalidTags, VALID_PRIORITY);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidPriority_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAILS,
                VALID_LINKS, VALID_GRADUATING_YEAR, VALID_COURSE,
                VALID_SPECIALISATION, VALID_TAGS, INVALID_PRIORITY);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_nullPriority_success() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAILS,
                VALID_LINKS, VALID_GRADUATING_YEAR, VALID_COURSE,
                VALID_SPECIALISATION, VALID_TAGS, null);
        try {
            assertEquals(TypicalPersons.BENSON, person.toModelType());
        } catch (IllegalValueException e) {
            fail();
        }
    }

}
