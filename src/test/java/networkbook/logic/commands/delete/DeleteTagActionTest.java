package networkbook.logic.commands.delete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import networkbook.commons.core.index.Index;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.person.Person;
import networkbook.model.person.Tag;
import networkbook.model.util.UniqueList;
import networkbook.testutil.TypicalPersons;

public class DeleteTagActionTest {
    private static final Person JACK = TypicalPersons.JACK;
    private static final Index firstIndex = Index.fromOneBased(1);
    private static final Index tenthIndex = Index.fromOneBased(10);
    private static final DeletePersonDescriptor jackWithoutFirstTag = new DeletePersonDescriptor(new Person(
            JACK.getName(),
            JACK.getPhones(),
            JACK.getEmails(),
            JACK.getLinks(),
            JACK.getGraduation().get(),
            JACK.getCourses(),
            JACK.getSpecialisations(),
            new UniqueList<Tag>().setItems(List.of(new Tag("AI enthusiast"))),
            JACK.getPriority().get()));

    private static final DeletePersonDescriptor jackWithoutAnyTag = new DeletePersonDescriptor(new Person(
            JACK.getName(),
            JACK.getPhones(),
            JACK.getEmails(),
            JACK.getLinks(),
            JACK.getGraduation().get(),
            JACK.getCourses(),
            JACK.getSpecialisations(),
            new UniqueList<Tag>(),
            JACK.getPriority().get()));

    @Test
    public void delete_deleteTagValidIndex_success() throws CommandException {
        DeletePersonDescriptor descriptor = new DeletePersonDescriptor(JACK);
        DeleteTagAction action = new DeleteTagAction(firstIndex);
        action.delete(descriptor);
        assertEquals(descriptor, jackWithoutFirstTag);
        action.delete(descriptor);
        assertEquals(descriptor, jackWithoutAnyTag);
    }

    @Test
    public void delete_deleteTagInvalidIndex_commandException() throws CommandException {
        DeleteTagAction deleteFirstAction = new DeleteTagAction(firstIndex);
        DeletePersonDescriptor descriptorWithoutTag = new DeletePersonDescriptor(new Person(
                JACK.getName(),
                JACK.getPhones(),
                JACK.getEmails(),
                JACK.getLinks(),
                JACK.getGraduation().get(),
                JACK.getCourses(),
                JACK.getSpecialisations(),
                new UniqueList<Tag>(),
                JACK.getPriority().get()));
        assertThrows(CommandException.class, () -> deleteFirstAction.delete(descriptorWithoutTag));

        DeleteTagAction deleteTenthAction = new DeleteTagAction(tenthIndex);
        DeletePersonDescriptor descriptorWithTwoTags = new DeletePersonDescriptor(JACK);
        assertThrows(CommandException.class, () -> deleteTenthAction.delete(descriptorWithTwoTags));
    }

    @Test
    public void delete_deleteNull_nullPointerException() {
        DeleteTagAction deleteFirstAction = new DeleteTagAction(firstIndex);
        assertThrows(NullPointerException.class, () -> deleteFirstAction.delete(null));
    }

    @Test
    public void equalsTest() {
        DeleteTagAction deleteFirstAction1 = new DeleteTagAction(firstIndex);
        DeleteTagAction deleteFirstAction2 = new DeleteTagAction(firstIndex);
        DeleteTagAction deleteTenthAction = new DeleteTagAction(tenthIndex);
        assertEquals(deleteFirstAction1, deleteFirstAction1);
        assertEquals(deleteFirstAction1, deleteFirstAction2);
        assertNotEquals(deleteFirstAction1, null);
        assertNotEquals(null, deleteFirstAction1);
        assertNotEquals(deleteFirstAction1, new DeletePriorityAction());
        assertNotEquals(deleteFirstAction1, deleteTenthAction);
    }
}
