package networkbook.model.util;

import static networkbook.testutil.Assert.assertThrows;
import static networkbook.testutil.Assert.assertThrowsAssertionError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class UniqueListTest {
    private static final UniqueNumber ITEM1 = new UniqueNumber(1, 1);
    private static final UniqueNumber ITEM1_COPY = new UniqueNumber(1, 1);
    private static final UniqueNumber ITEM1_SAME = new UniqueNumber(1, 2);
    private static final UniqueNumber ITEM2 = new UniqueNumber(2, 1);
    private static final UniqueNumber ITEM3 = new UniqueNumber(3, 0);
    private static final UniqueNumber ITEM_NOT_IN_LIST = new UniqueNumber(4, 0);
    private static final UniqueNumber ITEM_NOT_IN_LIST1 = new UniqueNumber(5, 0);
    private static final int INDEX1 = 0;
    private static final int INDEX2 = 1;
    private static final int INDEX_OUT_OF_BOUND = 3;
    private static final int NEGATIVE_INDEX = -1;
    private static final UniqueList<UniqueNumber> SAMPLE_LIST = new UniqueList<UniqueNumber>().setItems(List.of(
            ITEM1, ITEM2, ITEM3));

    private static UniqueList<UniqueNumber> getSampleList() {
        return SAMPLE_LIST.copy();
    }

    @Test
    public void contains_null_throwsAssertionError() {
        assertThrowsAssertionError(() -> getSampleList().contains(null));
    }

    @Test
    public void contains_elementNotInList_returnsFalse() {
        assertFalse(getSampleList().contains(ITEM_NOT_IN_LIST));
    }

    @Test
    public void contains_elementInList_returnsTrue() {
        assertTrue(getSampleList().contains(ITEM1));
        assertTrue(getSampleList().contains(ITEM1_SAME));
        assertTrue(getSampleList().contains(ITEM1_COPY));
    }

    @Test
    public void containsNotAtIndex_null_throwsAssertionError() {
        assertThrowsAssertionError(() -> getSampleList().containsNotAtIndex(null, INDEX1));
    }

    @Test
    public void containsNotAtIndex_indexOutOfBound_throwsAssertionError() {
        assertThrowsAssertionError(() -> getSampleList().containsNotAtIndex(ITEM1_COPY, NEGATIVE_INDEX));
        assertThrowsAssertionError(() -> getSampleList().containsNotAtIndex(ITEM1_COPY, INDEX_OUT_OF_BOUND));
    }

    @Test
    public void containsNotAtIndex_elementInListNotAtIndex_returnsTrue() {
        assertTrue(getSampleList().containsNotAtIndex(ITEM1, INDEX2));
        assertTrue(getSampleList().containsNotAtIndex(ITEM1_COPY, INDEX2));
        assertTrue(getSampleList().containsNotAtIndex(ITEM1_SAME, INDEX2));
    }

    @Test
    public void containsNotAtIndex_elementInListAtIndex_returnsFalse() {
        assertFalse(getSampleList().containsNotAtIndex(ITEM1, INDEX1));
        assertFalse(getSampleList().containsNotAtIndex(ITEM1_COPY, INDEX1));
        assertFalse(getSampleList().containsNotAtIndex(ITEM1_SAME, INDEX1));
    }

    @Test
    public void containsNotAtIndex_elementNotInList_returnFalse() {
        assertFalse(getSampleList().containsNotAtIndex(ITEM_NOT_IN_LIST, INDEX1));
    }

    @Test
    public void add_null_throwsAssertionError() {
        assertThrowsAssertionError(() -> getSampleList().add(null));
    }

    @Test
    public void add_duplicate_throwsAssertionError() {
        assertThrowsAssertionError(() -> getSampleList().add(ITEM1));
        assertThrowsAssertionError(() -> getSampleList().add(ITEM1_SAME));
        assertThrowsAssertionError(() -> getSampleList().add(ITEM1_COPY));
    }

    @Test
    public void addAll_null_throwsAssertionError() {
        assertThrowsAssertionError(() -> getSampleList().addAllFromList(null));
    }

    @Test
    public void addAll_duplicate_duplicateNotAdded() {
        UniqueList<UniqueNumber> uniqueList = getSampleList();
        UniqueList<UniqueNumber> addList = new UniqueList<UniqueNumber>().setItems(List.of(
                ITEM1_SAME, ITEM_NOT_IN_LIST));
        uniqueList.addAllFromList(addList);
        UniqueList<UniqueNumber> expectedList = getSampleList();
        expectedList.add(ITEM_NOT_IN_LIST);
        assertEquals(expectedList, uniqueList);

        uniqueList = getSampleList();
        addList = new UniqueList<UniqueNumber>().setItems(List.of(
                ITEM1_COPY, ITEM_NOT_IN_LIST));
        uniqueList.addAllFromList(addList);
        assertEquals(expectedList, uniqueList);
    }

    @Test
    public void addAll_noDuplicate_success() {
        UniqueList<UniqueNumber> uniqueList = getSampleList();
        UniqueList<UniqueNumber> addList = new UniqueList<UniqueNumber>().setItems(List.of(ITEM_NOT_IN_LIST));
        uniqueList.addAllFromList(addList);
        UniqueList<UniqueNumber> expectedList = new UniqueList<UniqueNumber>().setItems(List.of(
                ITEM1, ITEM2, ITEM3, ITEM_NOT_IN_LIST));
        assertEquals(expectedList, uniqueList);
    }

    @Test
    public void setItem_nullTarget_throwsAssertionError() {
        assertThrowsAssertionError(()
                -> getSampleList().setItem(null, ITEM_NOT_IN_LIST));
    }

    @Test
    public void setItem_nullEdited_throwsAssertionError() {
        assertThrowsAssertionError(()
                -> getSampleList().setItem(ITEM1, null));
    }

    @Test
    public void setItem_targetNotInList_throwsAssertionError() {
        assertThrowsAssertionError(() -> getSampleList().setItem(ITEM_NOT_IN_LIST, ITEM_NOT_IN_LIST1));
    }

    @Test
    public void setItem_editedEqualToTarget_success() {
        UniqueList<UniqueNumber> uniqueList = getSampleList();
        uniqueList.setItem(ITEM1, ITEM1_COPY);
        UniqueList<UniqueNumber> expectedList = getSampleList();
        assertEquals(expectedList, uniqueList);

        uniqueList.setItem(ITEM1, ITEM1);
        assertEquals(expectedList, uniqueList);
    }

    @Test
    public void setItem_editedIsSameAsTarget_success() {
        UniqueList<UniqueNumber> uniqueList = getSampleList();
        uniqueList.setItem(ITEM1, ITEM1_SAME);
        UniqueList<UniqueNumber> expectedList = new UniqueList<UniqueNumber>().setItems(List.of(
                ITEM1_SAME, ITEM2, ITEM3));
        assertEquals(expectedList, uniqueList);
    }

    @Test
    public void setItem_editedDifferentFromTarget_success() {
        UniqueList<UniqueNumber> uniqueList = getSampleList();
        uniqueList.setItem(ITEM1, ITEM_NOT_IN_LIST);
        UniqueList<UniqueNumber> expectedList = new UniqueList<UniqueNumber>().setItems(List.of(
                ITEM_NOT_IN_LIST, ITEM2, ITEM3));
        assertEquals(expectedList, uniqueList);
    }

    @Test
    public void setItem_editedNotUnique_throwsAssertionError() {
        assertThrowsAssertionError(() -> getSampleList().setItem(ITEM3, ITEM1_SAME));
    }

    @Test
    public void setItemWithIndex_nullEdited_throwsAssertionError() {
        assertThrowsAssertionError(() -> getSampleList().setItem(INDEX1, null));
    }

    @Test
    public void setItemWithIndex_invalidIndex_throwsAssertionError() {
        assertThrowsAssertionError(() -> getSampleList().setItem(NEGATIVE_INDEX, ITEM_NOT_IN_LIST));
        assertThrowsAssertionError(() -> getSampleList().setItem(INDEX_OUT_OF_BOUND, ITEM_NOT_IN_LIST));
    }

    @Test
    public void setItemWithIndex_editedItemAlreadyInList_throwsAssertionError() {
        assertThrowsAssertionError(() -> getSampleList().setItem(INDEX2, ITEM1));
        assertThrowsAssertionError(() -> getSampleList().setItem(INDEX2, ITEM1_COPY));
        assertThrowsAssertionError(() -> getSampleList().setItem(INDEX2, ITEM1_SAME));
    }

    @Test
    public void setItemWithIndex_editedItemEqualTarget_success() {
        UniqueList<UniqueNumber> uniqueList = getSampleList();
        uniqueList.setItem(INDEX1, ITEM1_COPY);
        UniqueList<UniqueNumber> expectedList = getSampleList();
        assertEquals(expectedList, uniqueList);

        uniqueList.setItem(INDEX1, ITEM1);
        assertEquals(expectedList, uniqueList);
    }

    @Test
    public void setItemWithIndex_editedItemSameAsTarget_success() {
        UniqueList<UniqueNumber> uniqueList = getSampleList();
        uniqueList.setItem(INDEX1, ITEM1_SAME);
        UniqueList<UniqueNumber> expectedList = new UniqueList<UniqueNumber>().setItems(List.of(
                ITEM1_SAME, ITEM2, ITEM3));
        assertEquals(expectedList, uniqueList);
    }

    @Test
    public void setItemWithIndex_editedNotInList_success() {
        UniqueList<UniqueNumber> uniqueList = getSampleList();
        uniqueList.setItem(INDEX1, ITEM_NOT_IN_LIST);
        UniqueList<UniqueNumber> expectedList = new UniqueList<UniqueNumber>().setItems(List.of(
                ITEM_NOT_IN_LIST, ITEM2, ITEM3));
        assertEquals(expectedList, uniqueList);
    }

    @Test
    public void remove_itemDoesNotExist_throwsAssertionError() {
        assertThrowsAssertionError(() -> getSampleList().remove(ITEM_NOT_IN_LIST));
    }

    @Test
    public void remove_itemExists_removesItem() {
        UniqueList<UniqueNumber> uniqueList = getSampleList();
        uniqueList.remove(ITEM1);
        UniqueList<UniqueNumber> expected = new UniqueList<UniqueNumber>().setItems(List.of(
                ITEM2, ITEM3));
        assertEquals(expected, uniqueList);

        uniqueList = getSampleList();
        uniqueList.remove(ITEM1_COPY);
        assertEquals(expected, uniqueList);
    }

    @Test
    public void setItems_nullList_throwsAssertionError() {
        assertThrowsAssertionError(() -> new UniqueList<UniqueNumber>().setItems((List<UniqueNumber>) null));
        assertThrowsAssertionError(() -> new UniqueList<UniqueNumber>().setItems((UniqueList<UniqueNumber>) null));
    }

    @Test
    public void setItems_uniqueList_replacesOwnListWithProvided() {
        UniqueList<UniqueNumber> expectedUniqueList = new UniqueList<>();
        List<UniqueNumber> expectedList = List.of(new UniqueNumber(3, 1));
        expectedList.forEach(expectedUniqueList::add);
        assertEquals(expectedUniqueList, new UniqueList<UniqueNumber>().setItems(expectedUniqueList));
        assertNotEquals(new UniqueList<UniqueNumber>(),
                new UniqueList<UniqueNumber>().setItems(expectedUniqueList));
        assertEquals(expectedUniqueList, new UniqueList<UniqueNumber>().setItems(expectedList));
    }

    @Test
    public void setItems_listWithDuplicate_throwsAssertionError() {
        List<UniqueNumber> list = List.of(ITEM1, ITEM1_SAME);
        assertThrowsAssertionError(() -> getSampleList().setItems(list));
    }

    @Test
    public void isEmpty_emptyList_returnsTrue() {
        assertTrue(new UniqueList<UniqueNumber>().isEmpty());
    }

    @Test
    public void isEmpty_notEmptyList_returnsFalse() {
        assertFalse(getSampleList().isEmpty());
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                -> getSampleList()
                .asUnmodifiableObservableList()
                .add(ITEM_NOT_IN_LIST));
    }

    @Test
    public void equals() {
        UniqueList<UniqueNumber> uniqueList = new UniqueList<>();
        UniqueNumber uniqueNumber = new UniqueNumber(1, 0);
        UniqueNumber sameUniqueNumber = new UniqueNumber(1, 0);
        UniqueNumber differentUniqueNumber = new UniqueNumber(1, 10);

        // same reference
        assertTrue(uniqueList.equals(uniqueList));

        // different class
        assertFalse(uniqueList.equals(uniqueNumber));

        // same list
        assertTrue(uniqueList.equals(new UniqueList<UniqueNumber>()));
        uniqueList.add(uniqueNumber);
        UniqueList<UniqueNumber> sameUniqueList = new UniqueList<>();
        sameUniqueList.add(sameUniqueNumber);
        assertTrue(uniqueList.equals(sameUniqueList));

        // different list
        assertFalse(uniqueList.equals(new UniqueList<UniqueNumber>()));
        UniqueList<UniqueNumber> differentUniqueList = new UniqueList<>();
        differentUniqueList.add(differentUniqueNumber);
        assertFalse(uniqueList.equals(differentUniqueList));
    }

    @Test
    public void toStringMethod() {
        assertEquals(getSampleList().asUnmodifiableObservableList().toString(), getSampleList().toString());
    }
}
