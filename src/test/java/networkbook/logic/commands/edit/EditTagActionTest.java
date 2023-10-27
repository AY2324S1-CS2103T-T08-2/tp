package networkbook.logic.commands.edit;

import static networkbook.testutil.Assert.assertThrows;
import static networkbook.testutil.Assert.assertThrowsAssertionError;
import static networkbook.testutil.TypicalPersons.JACK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.person.Person;
import networkbook.model.person.Tag;
import networkbook.model.util.UniqueList;

public class EditTagActionTest {
    private static final EditTagAction SAMPLE_VALID_EDIT_TAG_ACTION =
            new EditTagAction(EditCommandUtil.VALID_INDEX, EditCommandUtil.VALID_TAG);
    private static final EditTagAction SAMPLE_INVALID_EDIT_TAG_ACTION =
            new EditTagAction(EditCommandUtil.INVALID_INDEX, EditCommandUtil.VALID_TAG);

    @Test
    public void equalsTest() {
        assertFalse(new EditTagAction(EditCommandUtil.VALID_INDEX, EditCommandUtil.VALID_TAG).equals(1));
        assertFalse(new EditTagAction(EditCommandUtil.VALID_INDEX, EditCommandUtil.VALID_TAG).equals(null));
        assertFalse(new EditTagAction(EditCommandUtil.VALID_INDEX, EditCommandUtil.VALID_TAG).equals(new Object()));

        assertTrue(SAMPLE_VALID_EDIT_TAG_ACTION.equals(SAMPLE_VALID_EDIT_TAG_ACTION));
        assertTrue(new EditTagAction(EditCommandUtil.VALID_INDEX, EditCommandUtil.VALID_TAG)
                .equals(SAMPLE_VALID_EDIT_TAG_ACTION));
    }

    @Test
    public void toStringTest() {
        String expected = EditTagAction.class.getCanonicalName() + "{tag=" + EditCommandUtil.VALID_TAG
                + ", index=" + EditCommandUtil.VALID_INDEX.getOneBased() + "}";
        assertEquals(SAMPLE_VALID_EDIT_TAG_ACTION.toString(), expected);
    }

    @Test
    public void edit_null_throwsAssertionError() {
        assertThrowsAssertionError(() -> SAMPLE_VALID_EDIT_TAG_ACTION.edit(null));
    }

    @Test
    public void edit_validEditAction_success() throws CommandException {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        SAMPLE_VALID_EDIT_TAG_ACTION.edit(actualDescriptor);

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
    public void edit_invalidEditAction_throwsCommandException() {
        EditPersonDescriptor actualDescriptor = new EditPersonDescriptor(JACK);
        assertThrows(CommandException.class, () -> SAMPLE_INVALID_EDIT_TAG_ACTION.edit(actualDescriptor));
    }
}
