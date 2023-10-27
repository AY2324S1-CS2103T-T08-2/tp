package networkbook.ui;

import static java.util.Objects.requireNonNull;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import networkbook.model.person.Graduation;
import networkbook.model.person.Person;
import networkbook.model.person.Priority;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String PHONES_HEADER = "Phone: ";
    private static final String EMAILS_HEADER = "Emails: ";
    private static final String LINKS_HEADER = "Links: ";
    private static final String GRADUATION_HEADER = "Graduation: ";
    private static final String COURSE_HEADER = "Courses: ";
    private static final String SPECIALISATION_HEADER = "Specialisations: ";
    private static final String PRIORITY_HEADER = "Priority: ";
    private static final String EMPTY_FIELD = "-";

    private static final Logger LOGGER = Logger.getLogger("PersonCard");

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phones;
    @FXML
    private Label linksHeader;
    @FXML
    private FlowPane links;
    @FXML
    private Label graduation;
    @FXML
    private Label courses;
    @FXML
    private Label specialisations;
    @FXML
    private Label emailsHeader;
    @FXML
    private FlowPane emails;
    @FXML
    private FlowPane tags;
    @FXML
    private Label priority;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        requireNonNull(person);
        this.person = person;

        // Name and ID
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);

        // Phone numbers
        phones.setText(PHONES_HEADER + person.getPhones().toString());

        // Email addresses
        emailsHeader.setText(EMAILS_HEADER);
        // TODO: implement actual link opening
        person.getEmails().stream()
                .forEach(email -> emails.getChildren().add(new FieldHyperlink(email.getValue(), () -> {
                    LOGGER.log(Level.INFO, "Opening email: " + email.getValue());
                })));

        // Website links
        linksHeader.setText(LINKS_HEADER);
        // TODO: implement actual link opening
        person.getLinks().stream()
                .forEach(link -> links.getChildren().add(new FieldHyperlink(link.getValue(), () -> {
                    LOGGER.log(Level.INFO, "Opening link: " + link.getValue());
                })));

        // Graduation
        person.getGraduation().ifPresentOrElse((Graduation g) ->
                graduation.setText(GRADUATION_HEADER + g.getFullString()), () ->
                graduation.setText(GRADUATION_HEADER + EMPTY_FIELD));

        // Courses
        courses.setText(COURSE_HEADER + person.getCourses().toString());

        // Specialisations
        specialisations.setText(SPECIALISATION_HEADER + person.getSpecialisations().toString());

        // Tags
        person.getTags().stream()
                .forEach(tag -> tags.getChildren().add(new Label(tag.getValue())));
        person.getPriority().ifPresentOrElse((Priority p) ->
                        priority.setText(PRIORITY_HEADER + p), () -> priority.setText(PRIORITY_HEADER + EMPTY_FIELD));
    }

    public Label getPhones() {
        return phones;
    }
    public Label getGraduation() {
        return graduation;
    }
    public Label getCourse() {
        return courses;
    }
    public Label getSpecialisations() {
        return specialisations;
    }
    public Label getPriority() {
        return priority; // getter method for testing
    }
}
