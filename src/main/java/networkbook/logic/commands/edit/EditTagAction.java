package networkbook.logic.commands.edit;

import networkbook.commons.core.index.Index;
import networkbook.commons.util.ToStringBuilder;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.person.Tag;

/**
 * Represents an action to edit tag of a person.
 */
public class EditTagAction implements EditAction {
    private final Tag tag;
    private final Index index;

    /**
     * Constructs a new action to edit tag.
     * @param index The index of the tag in the tag list of the person.
     * @param tag The new value of the tag.
     */
    public EditTagAction(Index index, Tag tag) {
        this.index = index;
        this.tag = tag;
    }

    @Override
    public void edit(EditPersonDescriptor editPersonDescriptor) throws CommandException {
        assert editPersonDescriptor != null : "editPersonDescriptor should not be null";
        editPersonDescriptor.setTag(index, tag);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof EditTagAction)) {
            return false;
        }

        EditTagAction otherAction = (EditTagAction) object;
        return this.tag.equals(otherAction.tag)
                && this.index.equals(otherAction.index);
    }

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toStringBuilder.add("tag", this.tag);
        toStringBuilder.add("index", this.index.getOneBased());
        return toStringBuilder.toString();
    }
}
