package networkbook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import networkbook.model.person.Tag;
import networkbook.model.util.UniqueList;
import networkbook.testutil.AddPersonDescriptorBuilder;

public class AddPersonDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        AddCommand.AddPersonDescriptor descriptorWithSameValues =
                new AddCommand.AddPersonDescriptor(CommandTestUtil.DESC_AMY);
        assertTrue(CommandTestUtil.DESC_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(CommandTestUtil.DESC_AMY.equals(CommandTestUtil.DESC_AMY));

        // null -> returns false
        assertFalse(CommandTestUtil.DESC_AMY.equals(null));

        // different types -> returns false
        assertFalse(CommandTestUtil.DESC_AMY.equals(5));

        // different values -> returns false
        assertFalse(CommandTestUtil.DESC_AMY.equals(CommandTestUtil.DESC_BOB));

        // different phone -> returns false
        AddCommand.AddPersonDescriptor editedAmy = new AddPersonDescriptorBuilder(CommandTestUtil.DESC_AMY)
                .withPhone(CommandTestUtil.VALID_PHONE_BOB)
                .build();
        assertFalse(CommandTestUtil.DESC_AMY.equals(editedAmy));

        // different email -> returns false
        editedAmy = new AddPersonDescriptorBuilder(CommandTestUtil.DESC_AMY)
                .withEmail(CommandTestUtil.VALID_EMAIL_BOB)
                .build();
        assertFalse(CommandTestUtil.DESC_AMY.equals(editedAmy));

        // different link -> returns false
        editedAmy = new AddPersonDescriptorBuilder(CommandTestUtil.DESC_AMY)
                .withLink(CommandTestUtil.VALID_LINK_BOB)
                .build();
        assertFalse(CommandTestUtil.DESC_AMY.equals(editedAmy));

        // different graduating year -> returns false
        editedAmy = new AddPersonDescriptorBuilder(CommandTestUtil.DESC_AMY)
                .withGraduation(CommandTestUtil.VALID_GRADUATION_BOB)
                .build();
        assertFalse(CommandTestUtil.DESC_AMY.equals(editedAmy));

        // different course -> returns false
        editedAmy = new AddPersonDescriptorBuilder(CommandTestUtil.DESC_AMY)
                .withCourse(CommandTestUtil.VALID_COURSE_BOB)
                .build();
        assertFalse(CommandTestUtil.DESC_AMY.equals(editedAmy));

        // different specialisation -> returns false
        editedAmy = new AddPersonDescriptorBuilder(CommandTestUtil.DESC_AMY)
                .withSpecialisation(CommandTestUtil.VALID_SPECIALISATION_BOB)
                .build();
        assertFalse(CommandTestUtil.DESC_AMY.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new AddPersonDescriptorBuilder(CommandTestUtil.DESC_AMY)
                .withTags(CommandTestUtil.VALID_TAG_HUSBAND)
                .build();
        assertFalse(CommandTestUtil.DESC_AMY.equals(editedAmy));
    }

    @Test
    public void addTag_addNewTagWhenTagFieldIsNull_success() {
        AddCommand.AddPersonDescriptor descriptor = new AddPersonDescriptorBuilder().build();
        descriptor.addTag(new Tag(CommandTestUtil.VALID_TAG_FRIEND));
        UniqueList<Tag> expectedTagList = new UniqueList<>();
        expectedTagList.add(new Tag(CommandTestUtil.VALID_TAG_FRIEND));
        assertEquals(expectedTagList, descriptor.getTags().get());
    }

    @Test
    public void toStringMethod() {
        AddCommand.AddPersonDescriptor editPersonDescriptor = new AddCommand.AddPersonDescriptor();
        String expected = AddCommand.AddPersonDescriptor.class.getCanonicalName() + "{phones="
                + editPersonDescriptor.getPhones().orElse(null) + ", emails="
                + editPersonDescriptor.getEmails().orElse(null) + ", links="
                + editPersonDescriptor.getLinks().orElse(null) + ", graduation="
                + editPersonDescriptor.getGraduation().orElse(null) + ", courses="
                + editPersonDescriptor.getCourses().orElse(null) + ", specialisations="
                + editPersonDescriptor.getSpecialisations().orElse(null) + ", priority="
                + editPersonDescriptor.getPriority().orElse(null) + ", tags="
                + editPersonDescriptor.getTags().orElse(null) + "}";
        assertEquals(expected, editPersonDescriptor.toString());
    }
}
