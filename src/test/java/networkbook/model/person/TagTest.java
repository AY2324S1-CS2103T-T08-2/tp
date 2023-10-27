package networkbook.model.person;

import static networkbook.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TagTest {

    private static final String INVALID_TAG_1 = "";
    private static final String INVALID_TAG_2 = "/friends";
    private static final String VALID_TAG_1 = "cs_god";
    private static final String VALID_TAG_2 = "hyphen-tag and space";

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Tag(INVALID_TAG_1));
        assertThrows(IllegalArgumentException.class, () -> new Tag(INVALID_TAG_2));
    }

    @Test
    public void isValidTagName_nullTagName_throwsNullPointerException() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));
    }

    @Test
    public void isSame_sameTagNames_returnsTrue() {
        assertTrue(new Tag(VALID_TAG_1).isSame(new Tag(VALID_TAG_1)));
        assertTrue(new Tag(VALID_TAG_2).isSame(new Tag(VALID_TAG_2)));
    }

    @Test
    public void isSame_differentTagNames_returnsFalse() {
        assertFalse(new Tag(VALID_TAG_1).isSame(new Tag(VALID_TAG_2)));
        assertFalse(new Tag(VALID_TAG_1).isSame(new Tag(" " + VALID_TAG_1)));
    }

    @Test
    public void isSame_secondTagIsNull_returnsFalse() {
        assertFalse(new Tag(VALID_TAG_1).isSame(null));
    }

    @Test
    public void equals_sameTags_returnTrue() {
        assertEquals(new Tag(VALID_TAG_2), new Tag(VALID_TAG_2));
    }

    @Test
    public void equals_differentTags_returnFalse() {
        assertNotEquals(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2));
    }

    @Test
    public void equals_null_returnFalse() {
        assertNotEquals(new Tag(VALID_TAG_1), null);
    }

}
