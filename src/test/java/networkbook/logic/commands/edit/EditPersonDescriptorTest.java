package networkbook.logic.commands.edit;

import static networkbook.testutil.Assert.assertThrows;
import static networkbook.testutil.Assert.assertThrowsAssertionError;
import static networkbook.testutil.TypicalPersons.IDA;
import static networkbook.testutil.TypicalPersons.JACK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import networkbook.commons.core.index.Index;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.Link;
import networkbook.model.person.Person;
import networkbook.model.person.Phone;
import networkbook.model.person.Specialisation;
import networkbook.model.person.Tag;
import networkbook.model.util.UniqueList;

public class EditPersonDescriptorTest {
    @Test
    public void equals() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor(JACK);
        EditPersonDescriptor sameDescriptor = new EditPersonDescriptor(JACK);
        EditPersonDescriptor differentDescriptor = new EditPersonDescriptor(IDA);

        //  null -> false
        assertFalse(descriptor.equals(null));

        // other object type -> false
        assertFalse(descriptor.equals(1));

        // same reference -> true
        assertTrue(descriptor.equals(descriptor));

        // same descriptor -> true
        assertTrue(descriptor.equals(sameDescriptor));

        // different descriptor -> false
        assertFalse(descriptor.equals(differentDescriptor));
    }

    @Test
    public void setName_null_throwsAssertionError() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        assertThrowsAssertionError(() -> actualDescriptor.setName(null));
    }

    @Test
    public void setName_validName_success() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        actualDescriptor.setName(EditCommandUtil.VALID_NAME);

        Person expectedPerson = new Person(
                EditCommandUtil.VALID_NAME,
                JACK.getPhones(),
                JACK.getEmails(),
                JACK.getLinks(),
                JACK.getGraduation().orElse(null),
                JACK.getCourses(),
                JACK.getSpecialisations(),
                JACK.getTags(),
                JACK.getPriority().orElse(null)
        );
        EditPersonDescriptor expectedDescriptor = new EditPersonDescriptor(expectedPerson);
        assertEquals(expectedDescriptor, actualDescriptor);
    }

    @Test
    public void setPhone_null_throwsAssertionError() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        assertThrowsAssertionError(() -> actualDescriptor.setPhone(null, EditCommandUtil.VALID_PHONE));
        assertThrowsAssertionError(() -> actualDescriptor.setPhone(EditCommandUtil.VALID_INDEX, null));
        assertThrowsAssertionError(() -> actualDescriptor.setPhone(null, null));
    }

    @Test
    public void setPhone_validPhoneAndIndex_success() throws Exception {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        actualDescriptor.setPhone(EditCommandUtil.VALID_INDEX, EditCommandUtil.VALID_PHONE);

        UniqueList<Phone> newPhoneList = JACK.getPhones();
        newPhoneList.setItem(EditCommandUtil.VALID_INDEX.getZeroBased(), EditCommandUtil.VALID_PHONE);

        Person expectedPerson = new Person(
                JACK.getName(),
                newPhoneList,
                JACK.getEmails(),
                JACK.getLinks(),
                JACK.getGraduation().orElse(null),
                JACK.getCourses(),
                JACK.getSpecialisations(),
                JACK.getTags(),
                JACK.getPriority().orElse(null)
        );
        EditPersonDescriptor expectedDescriptor = new EditPersonDescriptor(expectedPerson);
        assertEquals(expectedDescriptor, actualDescriptor);
    }

    @Test
    public void setPhone_duplicatePhone_throwsCommandException() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor(JACK);
        Phone duplicatePhone = new Phone("12345678");
        Index index = Index.fromOneBased(2);
        assertThrows(CommandException.class, () -> editPersonDescriptor.setPhone(index, duplicatePhone));
    }

    @Test
    public void setPhone_invalidIndex_throwsCommandException() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        assertThrows(CommandException.class, ()
                -> actualDescriptor.setPhone(EditCommandUtil.INVALID_INDEX, EditCommandUtil.VALID_PHONE));
    }

    @Test
    public void setEmail_null_throwsAssertionError() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        assertThrowsAssertionError(() -> actualDescriptor.setEmail(null, EditCommandUtil.VALID_EMAIL));
        assertThrowsAssertionError(() -> actualDescriptor.setEmail(EditCommandUtil.VALID_INDEX, null));
        assertThrowsAssertionError(() -> actualDescriptor.setEmail(null, null));
    }

    @Test
    public void setEmail_validEmailAndIndex_success() throws Exception {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        actualDescriptor.setEmail(EditCommandUtil.VALID_INDEX, EditCommandUtil.VALID_EMAIL);

        UniqueList<Email> newEmailList = JACK.getEmails();
        newEmailList.setItem(EditCommandUtil.VALID_INDEX.getZeroBased(), EditCommandUtil.VALID_EMAIL);

        Person expectedPerson = new Person(
                JACK.getName(),
                JACK.getPhones(),
                newEmailList,
                JACK.getLinks(),
                JACK.getGraduation().orElse(null),
                JACK.getCourses(),
                JACK.getSpecialisations(),
                JACK.getTags(),
                JACK.getPriority().orElse(null)
        );
        EditPersonDescriptor expectedDescriptor = new EditPersonDescriptor(expectedPerson);
        assertEquals(expectedDescriptor, actualDescriptor);
    }

    @Test
    public void setEmail_duplicateEmail_throwsCommandException() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor(JACK);
        Email duplicateEmail = new Email("jack@gmail.com");
        Index index = Index.fromOneBased(2);
        assertThrows(CommandException.class, () -> editPersonDescriptor.setEmail(index, duplicateEmail));
    }

    @Test
    public void setEmail_invalidIndex_throwsCommandException() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        assertThrows(CommandException.class, ()
                -> actualDescriptor.setEmail(EditCommandUtil.INVALID_INDEX, EditCommandUtil.VALID_EMAIL));
    }

    @Test
    public void setLink_null_throwsAssertionError() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        assertThrowsAssertionError(() -> actualDescriptor.setLink(null, EditCommandUtil.VALID_LINK));
        assertThrowsAssertionError(() -> actualDescriptor.setLink(EditCommandUtil.VALID_INDEX, null));
        assertThrowsAssertionError(() -> actualDescriptor.setLink(null, null));
    }

    @Test
    public void setLink_validLinkAndIndex_success() throws Exception {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        actualDescriptor.setLink(EditCommandUtil.VALID_INDEX, EditCommandUtil.VALID_LINK);

        UniqueList<Link> newLinkList = JACK.getLinks();
        newLinkList.setItem(EditCommandUtil.VALID_INDEX.getZeroBased(), EditCommandUtil.VALID_LINK);

        Person expectedPerson = new Person(
                JACK.getName(),
                JACK.getPhones(),
                JACK.getEmails(),
                newLinkList,
                JACK.getGraduation().orElse(null),
                JACK.getCourses(),
                JACK.getSpecialisations(),
                JACK.getTags(),
                JACK.getPriority().orElse(null)
        );
        EditPersonDescriptor expectedDescriptor = new EditPersonDescriptor(expectedPerson);
        assertEquals(expectedDescriptor, actualDescriptor);
    }

    @Test
    public void setLink_duplicateLink_throwsCommandException() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor(JACK);
        Link duplicateLink = new Link("nkn.com");
        Index index = Index.fromOneBased(2);
        assertThrows(CommandException.class, () -> editPersonDescriptor.setLink(index, duplicateLink));
    }

    @Test
    public void setLink_invalidIndex_throwsCommandException() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        assertThrows(CommandException.class, ()
                -> actualDescriptor.setLink(EditCommandUtil.INVALID_INDEX, EditCommandUtil.VALID_LINK));
    }

    @Test
    public void setGraduation_null_throwsAssertionError() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        assertThrowsAssertionError(() -> actualDescriptor.setGraduation(null));
    }

    @Test
    public void setGraduation_validGraduation_success() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        actualDescriptor.setGraduation(EditCommandUtil.VALID_GRADUATION);

        Person expectedPerson = new Person(
                JACK.getName(),
                JACK.getPhones(),
                JACK.getEmails(),
                JACK.getLinks(),
                EditCommandUtil.VALID_GRADUATION,
                JACK.getCourses(),
                JACK.getSpecialisations(),
                JACK.getTags(),
                JACK.getPriority().orElse(null)
        );
        EditPersonDescriptor expectedDescriptor = new EditPersonDescriptor(expectedPerson);
        assertEquals(expectedDescriptor, actualDescriptor);
    }

    @Test
    public void setCourse_null_throwsAssertionError() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        assertThrowsAssertionError(() -> actualDescriptor.setCourse(null, EditCommandUtil.VALID_COURSE));
        assertThrowsAssertionError(() -> actualDescriptor.setCourse(EditCommandUtil.VALID_INDEX, null));
        assertThrowsAssertionError(() -> actualDescriptor.setCourse(null, null));
    }

    @Test
    public void setCourse_validCourseAndIndex_success() throws Exception {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        actualDescriptor.setCourse(EditCommandUtil.VALID_INDEX, EditCommandUtil.VALID_COURSE);

        UniqueList<Course> newCourseList = JACK.getCourses();
        newCourseList.setItem(EditCommandUtil.VALID_INDEX.getZeroBased(), EditCommandUtil.VALID_COURSE);

        Person expectedPerson = new Person(
                JACK.getName(),
                JACK.getPhones(),
                JACK.getEmails(),
                JACK.getLinks(),
                JACK.getGraduation().orElse(null),
                newCourseList,
                JACK.getSpecialisations(),
                JACK.getTags(),
                JACK.getPriority().orElse(null)
        );
        EditPersonDescriptor expectedDescriptor = new EditPersonDescriptor(expectedPerson);
        assertEquals(expectedDescriptor, actualDescriptor);
    }

    @Test
    public void setCourse_duplicateCourse_throwsCommandException() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor(JACK);
        Course duplicateCourse = new Course("CS2103T");
        Index index = Index.fromOneBased(2);
        assertThrows(CommandException.class, () -> editPersonDescriptor.setCourse(index, duplicateCourse));
    }

    @Test
    public void setCourse_invalidIndex_throwsCommandException() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        assertThrows(CommandException.class, ()
                -> actualDescriptor.setCourse(EditCommandUtil.INVALID_INDEX, EditCommandUtil.VALID_COURSE));
    }

    @Test
    public void setSpecialisation_null_throwsAssertionError() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        assertThrowsAssertionError(()
                -> actualDescriptor.setSpecialisation(null, EditCommandUtil.VALID_SPECIALISATION));
        assertThrowsAssertionError(()
                -> actualDescriptor.setSpecialisation(EditCommandUtil.VALID_INDEX, null));
        assertThrowsAssertionError(() -> actualDescriptor.setSpecialisation(null, null));
    }

    @Test
    public void setSpecialisation_validCourseAndIndex_success() throws Exception {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        actualDescriptor.setSpecialisation(EditCommandUtil.VALID_INDEX, EditCommandUtil.VALID_SPECIALISATION);

        UniqueList<Specialisation> newSpecialisationList = JACK.getSpecialisations();
        newSpecialisationList.setItem(
                EditCommandUtil.VALID_INDEX.getZeroBased(), EditCommandUtil.VALID_SPECIALISATION);

        Person expectedPerson = new Person(
                JACK.getName(),
                JACK.getPhones(),
                JACK.getEmails(),
                JACK.getLinks(),
                JACK.getGraduation().orElse(null),
                JACK.getCourses(),
                newSpecialisationList,
                JACK.getTags(),
                JACK.getPriority().orElse(null)
        );
        EditPersonDescriptor expectedDescriptor = new EditPersonDescriptor(expectedPerson);
        assertEquals(expectedDescriptor, actualDescriptor);
    }

    @Test
    public void setSpecialisation_duplicateSpecialisation_throwsCommandException() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor(JACK);
        Specialisation duplicateSpecialisation = new Specialisation("Software Engineering");
        Index index = Index.fromOneBased(2);
        assertThrows(CommandException.class, ()
                -> editPersonDescriptor.setSpecialisation(index, duplicateSpecialisation));
    }

    @Test
    public void setSpecialisation_invalidIndex_throwsCommandException() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        assertThrows(CommandException.class, ()
                -> actualDescriptor.setSpecialisation(
                        EditCommandUtil.INVALID_INDEX, EditCommandUtil.VALID_SPECIALISATION));
    }

    @Test
    public void setTag_null_throwsAssertionError() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        assertThrowsAssertionError(() -> actualDescriptor.setTag(null, EditCommandUtil.VALID_TAG));
        assertThrowsAssertionError(() -> actualDescriptor.setTag(EditCommandUtil.VALID_INDEX, null));
        assertThrowsAssertionError(() -> actualDescriptor.setTag(null, null));
    }

    @Test
    public void setTag_validTagAndIndex_success() throws Exception {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        actualDescriptor.setTag(EditCommandUtil.VALID_INDEX, EditCommandUtil.VALID_TAG);

        UniqueList<Tag> newTagList = JACK.getTags();
        newTagList.setItem(EditCommandUtil.VALID_INDEX.getZeroBased(), EditCommandUtil.VALID_TAG);

        Person expectedPerson = new Person(
                JACK.getName(),
                JACK.getPhones(),
                JACK.getEmails(),
                JACK.getLinks(),
                JACK.getGraduation().orElse(null),
                JACK.getCourses(),
                JACK.getSpecialisations(),
                newTagList,
                JACK.getPriority().orElse(null)
        );
        EditPersonDescriptor expectedDescriptor = new EditPersonDescriptor(expectedPerson);
        assertEquals(expectedDescriptor, actualDescriptor);
    }

    @Test
    public void setTag_duplicateTag_throwsCommandException() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor(JACK);
        Tag duplicateTag = new Tag("software eng enthusiast");
        Index index = Index.fromOneBased(2);
        assertThrows(CommandException.class, () -> editPersonDescriptor.setTag(index, duplicateTag));
    }

    @Test
    public void setTag_invalidIndex_throwsCommandException() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        assertThrows(CommandException.class, ()
                -> actualDescriptor.setTag(EditCommandUtil.INVALID_INDEX, EditCommandUtil.VALID_TAG));
    }

    @Test
    public void setPriority_null_throwsAssertionError() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        assertThrowsAssertionError(() -> actualDescriptor.setPriority(null));
    }

    @Test
    public void setPriority_validPriority_success() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        actualDescriptor.setPriority(EditCommandUtil.VALID_PRIORITY);

        Person expectedPerson = new Person(
                JACK.getName(),
                JACK.getPhones(),
                JACK.getEmails(),
                JACK.getLinks(),
                JACK.getGraduation().orElse(null),
                JACK.getCourses(),
                JACK.getSpecialisations(),
                JACK.getTags(),
                EditCommandUtil.VALID_PRIORITY
        );
        EditPersonDescriptor expectedDescriptor = new EditPersonDescriptor(expectedPerson);
        assertEquals(expectedDescriptor, actualDescriptor);
    }
}
