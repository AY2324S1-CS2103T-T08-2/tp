package networkbook.model.person;

import static java.util.Objects.requireNonNull;
import static networkbook.commons.util.AppUtil.checkArgument;

import networkbook.model.util.Identifiable;

/**
 * Represents a Person's web link in the network book.
 * Guarantees: immutable; is valid as declared in {@link #isValidLink(String)}
 */
public class Link implements Identifiable<Link> {

    public static final String MESSAGE_CONSTRAINTS = "Links should follow the format: domain/path "
            + "and adhere to the following constraints:\n"
            + "1. The domain may start with protocol http:// or https://\n"
            + "2. Except from the protocol, domain should only contain alphanumeric characters and dots\n"
            + "separated by hyphens if needed.\n"
            + "3. The domain must contain at least one dot.\n"
            + "4. The path, if included, should be a valid URI path.";
    private static final String SPECIAL_CHARACTERS = "+_.-";

    // regex adapted from https://regexr.com/3au3g
    private static final String DOMAIN_NAME_REGEX =
            "^(http://|https://)?(?:[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]{0,61}[a-z0-9]";

    // reference: https://stackoverflow.com/questions/4669692
    //          /valid-characters-for-directory-part-of-a-url-for-short-links
    private static final String PATH_REGEX = "(/[a-zA-Z0-9_\\-.~!$&'()*+,;=:@]*)*"; // Valid URI path

    // regex reused from https://stackoverflow.com/questions/23959352/validate-url-query-string-with-regex
    private static final String QUERY_REGEX = "\\?([\\w-]+(=[\\w-]*)?(&[\\w-]+(=[\\w-]*)?)*)?";

    public static final String VALIDATION_REGEX = DOMAIN_NAME_REGEX
            + "(" + PATH_REGEX + ")?" + "(" + QUERY_REGEX + ")?$";

    private final String value;

    /**
     * Constructs an {@code Link}.
     *
     * @param link A valid web link.
     */
    public Link(String link) {
        requireNonNull(link);
        checkArgument(isValidLink(link), MESSAGE_CONSTRAINTS);
        value = link;
    }

    /**
     * Returns if a given string is a valid link.
     */
    public static boolean isValidLink(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean isSame(Link another) {
        return this.value.equals(another.value);
    }

    @Override
    public String getValue() {
        return this.value;
    }

    /**
     * Returns a string that is recognisable as a weblink,
     * to prevent java from opening from local file.
     */
    public String toRecognisableWebUrl() {
        if (this.value.startsWith("http")) {
            return this.value;
        } else {
            return "https://" + this.value;
        }
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Link)) {
            return false;
        }

        Link otherLink = (Link) other;
        return value.equals(otherLink.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
