package networkbook.model.person;

import static networkbook.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LinkTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Link(null));
    }

    @Test
    public void constructor_invalidLink_throwsIllegalArgumentException() {
        String invalidLink = "";
        assertThrows(IllegalArgumentException.class, () -> new Link(invalidLink));
    }

    @Test
    public void isValidLink() {
        // null email
        assertThrows(NullPointerException.class, () -> Link.isValidLink(null));

        // blank email
        assertFalse(Link.isValidLink("")); // empty string
        assertFalse(Link.isValidLink(" ")); // spaces only

        // missing parts
        assertFalse(Link.isValidLink("example.")); // missing local part
        assertFalse(Link.isValidLink("peterjack@")); // missing domain label
        assertFalse(Link.isValidLink("http://"));

        // invalid parts
        assertFalse(Link.isValidLink("peterjack@-.com")); // invalid domain name
        assertFalse(Link.isValidLink("exam_ple.com")); // underscore in domain name

        // valid link
        assertTrue(Link.isValidLink("pythonanywhere.com/user/test"));
        assertTrue(Link.isValidLink("example-dash.com"));
        assertTrue(Link.isValidLink("example.com.sg"));
        assertTrue(Link.isValidLink("http://xn--stackoverflow.com"));
        assertTrue(Link.isValidLink("www.stackoverflow.xn--com"));
        assertTrue(Link.isValidLink("example.com/test01"));
        assertTrue(Link.isValidLink("www.example.com/test01/test02"));
        assertTrue(Link.isValidLink("google.com/quack-quack"));
        assertTrue(Link.isValidLink("google.com/index.html"));
        assertTrue(Link.isValidLink("https://www.pythonanywhere.com/user/test_underscore/"));
        assertTrue(Link.isValidLink("https://www.google.com/?q=haha"));
        assertTrue(Link.isValidLink("https://www.google.com?q=haha"));
        assertTrue(Link.isValidLink("https://www.google.com/ncr?q=haha"));
    }

    @Test
    public void toRecognisableWebUrl_linkNotStartingWithHttp_linkStartingWithHttps() {
        String originalLinkValue = "google.com";
        String expectedRecognisableValue = "https://google.com";
        assertEquals(expectedRecognisableValue, new Link(originalLinkValue).toRecognisableWebUrl());
    }

    @Test
    public void toRecognisableWebUrl_linkStartingWithHttp_sameLink() {
        String value = "http://google.com";
        assertEquals(value, new Link(value).toRecognisableWebUrl());
    }

    @Test
    public void equals() {
        Link link = new Link("linkedin.com/in/aaron");

        // same values -> returns true
        assertTrue(link.equals(new Link("linkedin.com/in/aaron")));

        // same object -> returns true
        assertTrue(link.equals(link));

        // null -> returns false
        assertFalse(link.equals(null));

        // different types -> returns false
        assertFalse(link.equals(5.0f));

        // different values -> returns false
        assertFalse(link.equals(new Link("linkedin.com/in/colin")));
    }

    @Test
    public void isSame_sameLinks_returnsTrue() {
        assertTrue(new Link("facebook.com/colin").isSame(new Link("facebook.com/colin")));
        assertTrue(new Link("www.google.com").isSame(new Link("www.google.com")));
    }

    @Test
    public void isSame_differentLinks_returnsFalse() {
        assertFalse(new Link("facebook.com/colin").isSame(new Link("facebook.com/aaron")));
        assertFalse(new Link("www.google.com").isSame(new Link("www.exam.com")));
    }
}
