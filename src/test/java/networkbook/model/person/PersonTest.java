package networkbook.model.person;

import static networkbook.logic.commands.CommandTestUtil.VALID_COURSE_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_GRADUATION_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_LINK_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_SPECIALISATION_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static networkbook.testutil.Assert.assertThrows;
import static networkbook.testutil.Assert.assertThrowsAssertionError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import networkbook.commons.core.index.Index;
import networkbook.testutil.PersonBuilder;
import networkbook.testutil.TypicalPersons;

public class PersonTest {

    @Test
    public void isSame() {
        // same object -> returns true
        assertTrue(TypicalPersons.AMY.isSame(TypicalPersons.AMY));

        // null -> returns false
        assertFalse(TypicalPersons.AMY.isSame(null));

        // same name, all other attributes different -> returns true
        Person editedAmy = new PersonBuilder(TypicalPersons.AMY)
                .withPhones(List.of(VALID_PHONE_BOB))
                .withEmails(List.of(VALID_EMAIL_BOB))
                .withLinks(List.of(VALID_LINK_BOB))
                .withGraduation(VALID_GRADUATION_BOB)
                .withCourses(List.of(VALID_COURSE_BOB))
                .withSpecialisations(List.of(VALID_SPECIALISATION_BOB))
                .withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(TypicalPersons.AMY.isSame(editedAmy));

        // different name, all other attributes same -> returns false
        editedAmy = new PersonBuilder(TypicalPersons.ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(TypicalPersons.ALICE.isSame(editedAmy));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new PersonBuilder(TypicalPersons.BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(TypicalPersons.BOB.isSame(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(TypicalPersons.BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(TypicalPersons.BOB.isSame(editedBob));
    }

    @Test
    public void getValue_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, TypicalPersons.ALICE::getValue);
    }

    @Test
    public void isValidLinkIndex_null_throwsAssertionError() {
        assertThrowsAssertionError(() -> TypicalPersons.JACK.isValidLinkIndex(null));
    }

    @Test
    public void isValidLinkIndex_invalidLink_false() {
        assertFalse(TypicalPersons.JACK.isValidLinkIndex(Index.fromOneBased(3)));
    }

    @Test
    public void isValidLinkIndex_validIndex_true() {
        assertTrue(TypicalPersons.JACK.isValidLinkIndex(Index.fromOneBased(1)));
    }

    @Test
    public void getLink_invalidIndex_throwsAssertionError() {
        assertThrowsAssertionError(() -> TypicalPersons.JACK.getLink(-1));
        assertThrowsAssertionError(() -> TypicalPersons.JACK.getLink(2));
    }

    @Test
    public void getLink_validIndex_returnsCorrectLink() {
        assertEquals(TypicalPersons.JACK.getLink(0), TypicalPersons.JACK_FIRST_LINK);
    }

    @Test
    public void openLink_null_throwsAssertionError() {
        assertThrowsAssertionError(() -> TypicalPersons.JACK.openLink(null));
    }

    @Test
    public void openLink_invalidIndex_throwsAssertionError() {
        assertThrowsAssertionError(() -> TypicalPersons.JACK.openLink(Index.fromOneBased(3)));
    }

    @Test
    public void openLink_validIndex_linkOpened() throws Exception {
        TypicalPersons.JACK.openLink(Index.fromOneBased(1));
        TypicalPersons.JACK.openLink(Index.fromOneBased(2));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(TypicalPersons.AMY).build();
        assertTrue(TypicalPersons.AMY.equals(aliceCopy));

        // same object -> returns true
        assertTrue(TypicalPersons.AMY.equals(TypicalPersons.AMY));

        // null -> returns false
        assertFalse(TypicalPersons.AMY.equals(null));

        // different type -> returns false
        assertFalse(TypicalPersons.AMY.equals(5));

        // different person -> returns false
        assertFalse(TypicalPersons.AMY.equals(TypicalPersons.BOB));

        // different name -> returns false
        Person editedAmy = new PersonBuilder(TypicalPersons.AMY).withName(VALID_NAME_BOB).build();
        assertFalse(TypicalPersons.AMY.equals(editedAmy));

        // different phone -> returns false
        editedAmy = new PersonBuilder(TypicalPersons.AMY).withPhones(List.of(VALID_PHONE_BOB)).build();
        assertFalse(TypicalPersons.AMY.equals(editedAmy));

        // different email -> returns false
        editedAmy = new PersonBuilder(TypicalPersons.AMY).withEmails(List.of(VALID_EMAIL_BOB)).build();
        assertFalse(TypicalPersons.AMY.equals(editedAmy));

        // different link -> returns false
        editedAmy = new PersonBuilder(TypicalPersons.AMY).withLinks(List.of(VALID_LINK_BOB)).build();
        assertFalse(TypicalPersons.AMY.equals(editedAmy));

        // different graduation date -> returns false
        editedAmy = new PersonBuilder(TypicalPersons.AMY).withGraduation(VALID_GRADUATION_BOB).build();
        assertFalse(TypicalPersons.AMY.equals(editedAmy));

        // different course -> returns false
        editedAmy = new PersonBuilder(TypicalPersons.AMY).withCourses(List.of(VALID_COURSE_BOB)).build();
        assertFalse(TypicalPersons.AMY.equals(editedAmy));

        // different specialisation -> returns false
        editedAmy = new PersonBuilder(TypicalPersons.AMY)
                .withSpecialisations(List.of(VALID_SPECIALISATION_BOB))
                .build();
        assertFalse(TypicalPersons.AMY.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new PersonBuilder(TypicalPersons.AMY).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(TypicalPersons.AMY.equals(editedAmy));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName()
                + "{name=" + TypicalPersons.AMY.getName()
                + ", phones=" + TypicalPersons.AMY.getPhones()
                + ", emails=" + TypicalPersons.AMY.getEmails()
                + ", links=" + TypicalPersons.AMY.getLinks()
                + ", graduation=" + TypicalPersons.AMY.getGraduation().get()
                + ", courses=" + TypicalPersons.AMY.getCourses()
                + ", specialisations=" + TypicalPersons.AMY.getSpecialisations()
                + ", tags=" + TypicalPersons.AMY.getTags() + ", priority=" + TypicalPersons.AMY.getPriority().get()
                + "}";
        assertEquals(expected, TypicalPersons.AMY.toString());
    }
}
