package networkbook.model.util;

import networkbook.commons.util.ThrowingIoExceptionConsumer;

/**
 * Manages a {@code ThrowingIoExceptionConsumer}, checking whether an item has been supplied to the consumer.
 */
public class ThrowingIoExceptionConsumerManager {
    private final UniqueNumber target;
    private final ThrowingIoExceptionConsumer<UniqueNumber> consumer = this::consume;
    private boolean isConsumed = false;

    public ThrowingIoExceptionConsumerManager(UniqueNumber target) {
        this.target = target;
    }

    private void consume(UniqueNumber t) {
        if (t.equals(target)) {
            this.isConsumed = true;
        }
    }

    public boolean isConsumed() {
        return this.isConsumed;
    }

    public ThrowingIoExceptionConsumer<UniqueNumber> getConsumer() {
        return this.consumer;
    }
}
