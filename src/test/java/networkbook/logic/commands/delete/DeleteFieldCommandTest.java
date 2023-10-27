package networkbook.logic.commands.delete;

import static networkbook.logic.commands.CommandTestUtil.assertCommandFailure;
import static networkbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static networkbook.logic.commands.delete.DeleteFieldCommand.MESSAGE_DELETE_PERSON_FIELD_SUCCESS;
import static networkbook.testutil.Assert.assertThrowsAssertionError;

import org.junit.jupiter.api.Test;

import networkbook.commons.core.index.Index;
import networkbook.logic.Messages;
import networkbook.model.Model;
import networkbook.model.ModelManager;
import networkbook.model.NetworkBook;
import networkbook.model.UserPrefs;
import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.Graduation;
import networkbook.model.person.Link;
import networkbook.model.person.Name;
import networkbook.model.person.Person;
import networkbook.model.person.Phone;
import networkbook.model.person.Priority;
import networkbook.model.person.Specialisation;
import networkbook.model.person.Tag;
import networkbook.model.util.UniqueList;
import networkbook.testutil.TypicalPersons;

public class DeleteFieldCommandTest {
    private static final Model model =
            new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());
    private static final Index INDEX_ONE = Index.fromOneBased(1);
    private static final Index INVALID_INDEX = Index.fromOneBased(
            model.getFilteredPersonList().size() + 1);
    private static final DeleteCourseAction DELETE_FIRST_COURSE_ACTION = new DeleteCourseAction(INDEX_ONE);
    private static final DeleteEmailAction DELETE_FIRST_EMAIL_ACTION = new DeleteEmailAction(INDEX_ONE);
    private static final DeleteLinkAction DELETE_FIRST_LINK_ACTION = new DeleteLinkAction(INDEX_ONE);
    private static final DeletePhoneAction DELETE_FIRST_PHONE_ACTION = new DeletePhoneAction(INDEX_ONE);
    private static final DeleteSpecialisationAction DELETE_FIRST_SPEC_ACTION =
            new DeleteSpecialisationAction(INDEX_ONE);
    private static final DeleteTagAction DELETE_FIRST_TAG_ACTION = new DeleteTagAction(INDEX_ONE);
    private static final DeletePriorityAction DELETE_PRIORITY_ACTION = new DeletePriorityAction();
    private static final DeleteGraduationAction DELETE_GRADUATION_ACTION = new DeleteGraduationAction();


    @Test
    public void constructor_null_nullPointerException() {
        assertThrowsAssertionError(() -> new DeleteFieldCommand(null, DELETE_PRIORITY_ACTION));
        assertThrowsAssertionError(() -> new DeleteFieldCommand(INDEX_ONE, null));
        assertThrowsAssertionError(() -> new DeleteFieldCommand(null, null));
    }

    @Test
    public void execute_invalidIndexOfPerson_commandException() {
        DeleteFieldCommand command1 = new DeleteFieldCommand(INVALID_INDEX, DELETE_PRIORITY_ACTION);
        assertCommandFailure(command1, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        DeleteFieldCommand command2 = new DeleteFieldCommand(INVALID_INDEX, DELETE_FIRST_TAG_ACTION);
        assertCommandFailure(command2, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexOfOldValue_commandException() {
        int numberOfCourses = model.getFilteredPersonList().get(0).getCourses().size();
        DeleteFieldCommand deleteInvalidCourseCommand = new DeleteFieldCommand(INDEX_ONE,
                new DeleteCourseAction(Index.fromZeroBased(numberOfCourses + 1)));
        assertCommandFailure(deleteInvalidCourseCommand, model, DeletePersonDescriptor.MESSAGE_INVALID_COURSE_INDEX);

        int numberOfPhones = model.getFilteredPersonList().get(0).getPhones().size();
        DeleteFieldCommand deleteInvalidPhoneCommand = new DeleteFieldCommand(INDEX_ONE,
                new DeletePhoneAction(Index.fromZeroBased(numberOfPhones + 1)));
        assertCommandFailure(deleteInvalidPhoneCommand, model, DeletePersonDescriptor.MESSAGE_INVALID_PHONE_INDEX);

        int numberOfEmails = model.getFilteredPersonList().get(0).getEmails().size();
        DeleteFieldCommand deleteInvalidEmailCommand = new DeleteFieldCommand(INDEX_ONE,
                new DeleteEmailAction(Index.fromZeroBased(numberOfEmails + 1)));
        assertCommandFailure(deleteInvalidEmailCommand, model, DeletePersonDescriptor.MESSAGE_INVALID_EMAIL_INDEX);

        int numberOfLinks = model.getFilteredPersonList().get(0).getLinks().size();
        DeleteFieldCommand deleteInvalidLinkCommand = new DeleteFieldCommand(INDEX_ONE,
                new DeleteLinkAction(Index.fromZeroBased(numberOfLinks + 1)));
        assertCommandFailure(deleteInvalidLinkCommand, model, DeletePersonDescriptor.MESSAGE_INVALID_LINK_INDEX);

        int numberOfSpecs = model.getFilteredPersonList().get(0).getSpecialisations().size();
        DeleteFieldCommand deleteInvalidSpecCommand = new DeleteFieldCommand(INDEX_ONE,
                new DeleteSpecialisationAction(Index.fromZeroBased(numberOfSpecs + 1)));
        assertCommandFailure(deleteInvalidSpecCommand, model,
                DeletePersonDescriptor.MESSAGE_INVALID_SPECIALISATION_INDEX);

        int numberOfTags = model.getFilteredPersonList().get(0).getTags().size();
        DeleteFieldCommand deleteInvalidTagCommand = new DeleteFieldCommand(INDEX_ONE,
                new DeleteTagAction(Index.fromZeroBased(numberOfTags + 1)));
        assertCommandFailure(deleteInvalidTagCommand, model, DeletePersonDescriptor.MESSAGE_INVALID_TAG_INDEX);
    }

    @Test
    public void execute_validCommand_success() {
        Model originalModel = new ModelManager(new NetworkBook(model.getNetworkBook()), new UserPrefs());
        originalModel.setItem(model.getFilteredPersonList().get(0), TypicalPersons.JACK);

        Name jackName = TypicalPersons.JACK.getName();
        UniqueList<Phone> jackPhones = TypicalPersons.JACK.getPhones();
        UniqueList<Email> jackEmails = TypicalPersons.JACK.getEmails();
        UniqueList<Link> jackLinks = TypicalPersons.JACK.getLinks();
        UniqueList<Course> jackCourses = TypicalPersons.JACK.getCourses();
        UniqueList<Specialisation> jackSpecialisations = TypicalPersons.JACK.getSpecialisations();
        UniqueList<Tag> jackTags = TypicalPersons.JACK.getTags();
        Graduation jackGraduation = TypicalPersons.JACK.getGraduation().get();
        Priority jackPriority = TypicalPersons.JACK.getPriority().get();

        UniqueList<Phone> jackPhonesWithoutFirst = TypicalPersons.JACK.getPhones();
        UniqueList<Email> jackEmailsWithoutFirst = TypicalPersons.JACK.getEmails();
        UniqueList<Link> jackLinksWithoutFirst = TypicalPersons.JACK.getLinks();
        UniqueList<Course> jackCoursesWithoutFirst = TypicalPersons.JACK.getCourses();
        UniqueList<Specialisation> jackSpecialisationsWithoutFirst = TypicalPersons.JACK.getSpecialisations();
        UniqueList<Tag> jackTagsWithoutFirst = TypicalPersons.JACK.getTags();

        jackPhonesWithoutFirst.removeAtIndex(0);
        jackEmailsWithoutFirst.removeAtIndex(0);
        jackLinksWithoutFirst.removeAtIndex(0);
        jackCoursesWithoutFirst.removeAtIndex(0);
        jackSpecialisationsWithoutFirst.removeAtIndex(0);
        jackTagsWithoutFirst.removeAtIndex(0);

        Person jackWithoutFirstPhone = new Person(
                jackName,
                jackPhonesWithoutFirst,
                jackEmails,
                jackLinks,
                jackGraduation,
                jackCourses,
                jackSpecialisations,
                jackTags,
                jackPriority
        );

        Person jackWithoutFirstEmail = new Person(
                jackName,
                jackPhones,
                jackEmailsWithoutFirst,
                jackLinks,
                jackGraduation,
                jackCourses,
                jackSpecialisations,
                jackTags,
                jackPriority
        );

        Person jackWithoutFirstLink = new Person(
                jackName,
                jackPhones,
                jackEmails,
                jackLinksWithoutFirst,
                jackGraduation,
                jackCourses,
                jackSpecialisations,
                jackTags,
                jackPriority
        );

        Person jackWithoutFirstCourse = new Person(
                jackName,
                jackPhones,
                jackEmails,
                jackLinks,
                jackGraduation,
                jackCoursesWithoutFirst,
                jackSpecialisations,
                jackTags,
                jackPriority
        );

        Person jackWithoutFirstSpec = new Person(
                jackName,
                jackPhones,
                jackEmails,
                jackLinks,
                jackGraduation,
                jackCourses,
                jackSpecialisationsWithoutFirst,
                jackTags,
                jackPriority
        );

        Person jackWithoutFirstTag = new Person(
                jackName,
                jackPhones,
                jackEmails,
                jackLinks,
                jackGraduation,
                jackCourses,
                jackSpecialisations,
                jackTagsWithoutFirst,
                jackPriority
        );

        Person jackWithoutGraduation = new Person(
                jackName,
                jackPhones,
                jackEmails,
                jackLinks,
                null,
                jackCourses,
                jackSpecialisations,
                jackTags,
                jackPriority
        );

        Person jackWithoutPriority = new Person(
                jackName,
                jackPhones,
                jackEmails,
                jackLinks,
                jackGraduation,
                jackCourses,
                jackSpecialisations,
                jackTags,
                null
        );

        DeleteFieldCommand deletePhoneCommand = new DeleteFieldCommand(INDEX_ONE, DELETE_FIRST_PHONE_ACTION);
        Model expectedModelAfterDeletingPhone =
                new ModelManager(new NetworkBook(model.getNetworkBook()), new UserPrefs());
        expectedModelAfterDeletingPhone.setItem(model.getFilteredPersonList().get(0), jackWithoutFirstPhone);
        assertCommandSuccess(deletePhoneCommand, originalModel,
                String.format(MESSAGE_DELETE_PERSON_FIELD_SUCCESS, Messages.format(jackWithoutFirstPhone)),
                expectedModelAfterDeletingPhone);

        originalModel.setItem(originalModel.getFilteredPersonList().get(0), TypicalPersons.JACK);
        DeleteFieldCommand deleteEmailCommand = new DeleteFieldCommand(INDEX_ONE, DELETE_FIRST_EMAIL_ACTION);
        Model expectedModelAfterDeletingEmail =
                new ModelManager(new NetworkBook(model.getNetworkBook()), new UserPrefs());
        expectedModelAfterDeletingEmail.setItem(model.getFilteredPersonList().get(0), jackWithoutFirstEmail);
        assertCommandSuccess(deleteEmailCommand, originalModel,
                String.format(MESSAGE_DELETE_PERSON_FIELD_SUCCESS, Messages.format(jackWithoutFirstEmail)),
                expectedModelAfterDeletingEmail);

        originalModel.setItem(originalModel.getFilteredPersonList().get(0), TypicalPersons.JACK);
        DeleteFieldCommand deleteLinkCommand = new DeleteFieldCommand(INDEX_ONE, DELETE_FIRST_LINK_ACTION);
        Model expectedModelAfterDeletingLink =
                new ModelManager(new NetworkBook(model.getNetworkBook()), new UserPrefs());
        expectedModelAfterDeletingLink.setItem(model.getFilteredPersonList().get(0), jackWithoutFirstLink);
        assertCommandSuccess(deleteLinkCommand, originalModel,
                String.format(MESSAGE_DELETE_PERSON_FIELD_SUCCESS, Messages.format(jackWithoutFirstLink)),
                expectedModelAfterDeletingLink);

        originalModel.setItem(originalModel.getFilteredPersonList().get(0), TypicalPersons.JACK);
        DeleteFieldCommand deleteCourseCommand = new DeleteFieldCommand(INDEX_ONE, DELETE_FIRST_COURSE_ACTION);
        Model expectedModelAfterDeletingCourse =
                new ModelManager(new NetworkBook(model.getNetworkBook()), new UserPrefs());
        expectedModelAfterDeletingCourse.setItem(model.getFilteredPersonList().get(0), jackWithoutFirstCourse);
        assertCommandSuccess(deleteCourseCommand, originalModel,
                String.format(MESSAGE_DELETE_PERSON_FIELD_SUCCESS, Messages.format(jackWithoutFirstCourse)),
                expectedModelAfterDeletingCourse);

        originalModel.setItem(originalModel.getFilteredPersonList().get(0), TypicalPersons.JACK);
        DeleteFieldCommand deleteSpecCommand = new DeleteFieldCommand(INDEX_ONE, DELETE_FIRST_SPEC_ACTION);
        Model expectedModelAfterDeletingSpec =
                new ModelManager(new NetworkBook(model.getNetworkBook()), new UserPrefs());
        expectedModelAfterDeletingSpec.setItem(model.getFilteredPersonList().get(0), jackWithoutFirstSpec);
        assertCommandSuccess(deleteSpecCommand, originalModel,
                String.format(MESSAGE_DELETE_PERSON_FIELD_SUCCESS, Messages.format(jackWithoutFirstSpec)),
                expectedModelAfterDeletingSpec);

        originalModel.setItem(originalModel.getFilteredPersonList().get(0), TypicalPersons.JACK);
        DeleteFieldCommand deleteTagCommand = new DeleteFieldCommand(INDEX_ONE, DELETE_FIRST_TAG_ACTION);
        Model expectedModelAfterDeletingTag =
                new ModelManager(new NetworkBook(model.getNetworkBook()), new UserPrefs());
        expectedModelAfterDeletingTag.setItem(model.getFilteredPersonList().get(0), jackWithoutFirstTag);
        assertCommandSuccess(deleteTagCommand, originalModel,
                String.format(MESSAGE_DELETE_PERSON_FIELD_SUCCESS, Messages.format(jackWithoutFirstTag)),
                expectedModelAfterDeletingTag);

        originalModel.setItem(originalModel.getFilteredPersonList().get(0), TypicalPersons.JACK);
        DeleteFieldCommand deleteGraduationCommand = new DeleteFieldCommand(INDEX_ONE, DELETE_GRADUATION_ACTION);
        Model expectedModelAfterDeletingGraduation =
                new ModelManager(new NetworkBook(model.getNetworkBook()), new UserPrefs());
        expectedModelAfterDeletingGraduation.setItem(model.getFilteredPersonList().get(0), jackWithoutGraduation);
        assertCommandSuccess(deleteGraduationCommand, originalModel,
                String.format(MESSAGE_DELETE_PERSON_FIELD_SUCCESS, Messages.format(jackWithoutGraduation)),
                expectedModelAfterDeletingGraduation);

        originalModel.setItem(originalModel.getFilteredPersonList().get(0), TypicalPersons.JACK);
        DeleteFieldCommand deletePriorityCommand = new DeleteFieldCommand(INDEX_ONE, DELETE_PRIORITY_ACTION);
        Model expectedModelAfterDeletingPriority =
                new ModelManager(new NetworkBook(model.getNetworkBook()), new UserPrefs());
        expectedModelAfterDeletingPriority.setItem(model.getFilteredPersonList().get(0), jackWithoutPriority);
        assertCommandSuccess(deletePriorityCommand, originalModel,
                String.format(MESSAGE_DELETE_PERSON_FIELD_SUCCESS, Messages.format(jackWithoutPriority)),
                expectedModelAfterDeletingPriority);
    }
}
