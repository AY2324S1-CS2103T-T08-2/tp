package networkbook.logic.commands.edit;

import networkbook.commons.core.index.Index;
import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.Graduation;
import networkbook.model.person.Link;
import networkbook.model.person.Name;
import networkbook.model.person.Phone;
import networkbook.model.person.Priority;
import networkbook.model.person.Specialisation;
import networkbook.model.person.Tag;

/**
 * Contains the values to be used in edit command testing.
 */
public class EditCommandUtil {
    public static final Index VALID_INDEX = Index.fromOneBased(1);
    public static final Index INVALID_INDEX = Index.fromOneBased(10);
    public static final Name VALID_NAME = new Name("Quack");
    public static final Phone VALID_PHONE = new Phone("12309856");
    public static final Email VALID_EMAIL = new Email("nkn@example.com");
    public static final Link VALID_LINK = new Link("www.quack.com");
    public static final Graduation VALID_GRADUATION = new Graduation("AY2324-S1");
    public static final Course VALID_COURSE = new Course("CS3230");
    public static final Specialisation VALID_SPECIALISATION = new Specialisation("Algorithm");
    public static final Tag VALID_TAG = new Tag("Algo godz");
    public static final Priority VALID_PRIORITY = new Priority("low");
}
