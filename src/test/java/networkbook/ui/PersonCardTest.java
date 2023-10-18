package networkbook.ui;

import static networkbook.logic.commands.CommandTestUtil.VALID_COURSE_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_GRADUATING_YEAR_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_SPECIALISATION_BOB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void constructor_hasValidNumber_showsValidNumber() {
        Person person = new PersonBuilder().withName("Bob").withPhone(VALID_PHONE_BOB).build();
        PersonCard personCard = new PersonCard(person, 1);
        Label phone = personCard.getPhone();
        assertEquals("Phone: " + VALID_PHONE_BOB, phone.getText());
        assertTrue(phone.isVisible());
    }

    @Test
    public void constructor_hasValidGraduatingYear_showsValidGraduatingYear() {
        Person person = new PersonBuilder().withName("Bob").withGraduatingYear(VALID_GRADUATING_YEAR_BOB).build();
        PersonCard personCard = new PersonCard(person, 1);
        Label graduatingYear = personCard.getGraduatingYear();
        assertEquals("Graduating Year: " + VALID_GRADUATING_YEAR_BOB, graduatingYear.getText());
        assertTrue(graduatingYear.isVisible());
    }

    @Test
    public void constructor_hasValidCourse_showsValidCourse() {
        Person person = new PersonBuilder().withName("Bob").withCourse(VALID_COURSE_BOB).build();
        PersonCard personCard = new PersonCard(person, 1);
        Label course = personCard.getCourse();
        assertEquals("Course: " + VALID_COURSE_BOB, course.getText());
        assertTrue(course.isVisible());
    }

    @Test
    public void constructor_hasValidSpecialisation_showsValidSpecialisation() {
        Person person = new PersonBuilder().withName("Bob").withSpecialisation(VALID_SPECIALISATION_BOB).build();
        PersonCard personCard = new PersonCard(person, 1);
        Label specialisation = personCard.getSpecialisation();
        assertEquals("Specialisation: " + VALID_SPECIALISATION_BOB, specialisation.getText());
        assertTrue(specialisation.isVisible());
    }

    @Test
    public void constructor_hasHighPriority_showsHighPriority() {
        Person person = new PersonBuilder().withName("Bob").withPriority("High").build();
        PersonCard personCard = new PersonCard(person, 1);
        Label priority = personCard.getPriority();
        assertEquals("Priority: High", priority.getText());
        assertTrue(priority.isVisible());
    }

    @Test
    public void constructor_noPriority_notVisible() {
        Person person = new PersonBuilder().withName("Bob").build();
        PersonCard personCard = new PersonCard(person, 1);
        Label priority = personCard.getPriority();
        assertNull(null, priority.getText());
        assertFalse(priority.isVisible());
    }

}
