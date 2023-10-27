package networkbook.ui;

import static networkbook.logic.commands.CommandTestUtil.VALID_COURSE_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_GRADUATION_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_GRADUATION_FULL_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_SPECIALISATION_BOB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.control.Label;
import networkbook.MainApp;
import networkbook.model.person.Person;
import networkbook.testutil.PersonBuilder;

@ExtendWith(ApplicationExtension.class)
public class PersonCardTest {
    //NOTE: for GUI headless tests, running this single file or test case has issue on Windows,
    //possibly due to issue discussed in https://github.com/Santulator/Santulator/issues/14
    //To avoid the issue, either run gradle test command to perform all tests,
    //or use 'gradlew check coverage' in command line in root folder

    @BeforeAll
    public static void setupForHeadlessTesting() {
        HeadlessHelper.setupForHeadlessTesting();
    }

    @BeforeEach
    public void setup() throws Exception {
        ApplicationTest.launch(MainApp.class);
    }

    @Test
    public void constructor_hasValidGraduation_showsValidGraduation() {
        Person person = new PersonBuilder().withName("Bob").withGraduation(VALID_GRADUATION_BOB).build();
        PersonCard personCard = new PersonCard(person, 1);
        Label graduation = personCard.getGraduation();
        assertEquals("Graduation: " + VALID_GRADUATION_FULL_BOB, graduation.getText());
        assertTrue(graduation.isVisible());
    }

    @Test
    public void constructor_noGraduation_showsBlank() {
        Person person = new PersonBuilder().withName("Bob").withoutOptionalFields().build();
        PersonCard personCard = new PersonCard(person, 1);
        Label graduation = personCard.getGraduation();
        assertEquals("Graduation: -", graduation.getText());
        assertTrue(graduation.isVisible());
    }

    @Test
    public void constructor_hasValidCourse_showsValidCourse() {
        Person person = new PersonBuilder().withName("Bob").withCourses(List.of(VALID_COURSE_BOB)).build();
        PersonCard personCard = new PersonCard(person, 1);
        Label course = personCard.getCourse();
        assertEquals("Courses: [" + VALID_COURSE_BOB + "]", course.getText());
        assertTrue(course.isVisible());
    }

    @Test
    public void constructor_hasValidPhones_showsValidPhones() {
        Person person = new PersonBuilder().withName("Bob").withPhones(List.of(VALID_PHONE_BOB)).build();
        PersonCard personCard = new PersonCard(person, 1);
        Label phones = personCard.getPhones();
        assertEquals("Phone: [" + VALID_PHONE_BOB + "]", phones.getText());
        assertTrue(phones.isVisible());
    }

    @Test
    public void constructor_hasValidSpecialisation_showsValidSpecialisation() {
        Person person = new PersonBuilder().withName("Bob")
                .withSpecialisations(List.of(VALID_SPECIALISATION_BOB))
                .build();
        PersonCard personCard = new PersonCard(person, 1);
        Label specialisations = personCard.getSpecialisations();
        assertEquals("Specialisations: [" + VALID_SPECIALISATION_BOB + "]", specialisations.getText());
        assertTrue(specialisations.isVisible());
    }

    @Test
    public void constructor_hasHighPriority_showsHighPriority() {
        Person person = new PersonBuilder().withName("Bob").withoutOptionalFields().withPriority("High").build();
        PersonCard personCard = new PersonCard(person, 1);
        Label priority = personCard.getPriority();
        assertEquals("Priority: High", priority.getText());
        assertTrue(priority.isVisible());
    }

    @Test
    public void constructor_noPriority_showsBlank() {
        Person person = new PersonBuilder().withName("Bob").build();
        PersonCard personCard = new PersonCard(person, 1);
        Label priority = personCard.getPriority();
        assertEquals("Priority: -", priority.getText());
        assertTrue(priority.isVisible());
    }

}
