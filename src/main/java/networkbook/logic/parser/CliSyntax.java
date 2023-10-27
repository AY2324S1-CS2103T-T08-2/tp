package networkbook.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("/name");
    public static final Prefix PREFIX_PHONE = new Prefix("/phone");
    public static final Prefix PREFIX_EMAIL = new Prefix("/email");
    public static final Prefix PREFIX_LINK = new Prefix("/link");
    public static final Prefix PREFIX_GRADUATION = new Prefix("/grad");
    public static final Prefix PREFIX_COURSE = new Prefix("/course");
    public static final Prefix PREFIX_SPECIALISATION = new Prefix("/spec");
    public static final Prefix PREFIX_TAG = new Prefix("/tag");
    public static final Prefix PREFIX_PRIORITY = new Prefix("/priority");
    public static final Prefix PREFIX_INDEX = new Prefix("/index");
    public static final Prefix PREFIX_SORT_FIELD = new Prefix("/by");
    public static final Prefix PREFIX_SORT_ORDER = new Prefix("/order");
    public static final Prefix PREFIX_FILTER_FIELD = new Prefix("/with");
    public static final Prefix PREFIX_FILTER_FIN = new Prefix("/taken");
}
