package networkbook.model.person;

import static java.util.Objects.requireNonNull;
import static networkbook.commons.util.AppUtil.checkArgument;

import networkbook.model.util.Identifiable;

/**
 * Represents a Tag in the network book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag implements Identifiable<Tag> {

    public static final String MESSAGE_CONSTRAINTS = "Tags names should only contain alphanumeric characters, "
            + "white space, hyphen or underscore.";
    public static final String VALIDATION_REGEX = "^[a-zA-Z0-9\\-_ ]+$+";

    private final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        checkArgument(isValidTagName(tagName), MESSAGE_CONSTRAINTS);
        this.tagName = tagName;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equals(otherTag.tagName);
    }

    @Override
    public String getValue() {
        return this.tagName;
    }

    @Override
    public boolean isSame(Tag toCheck) {
        // handle null case
        if (toCheck == null) {
            return false;
        }

        return this.tagName.equals(toCheck.tagName);
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}
