package networkbook.commons.util;

import java.io.IOException;

/**
 * Represents a consumer that may throw an IOException.
 * @param <T> The type of parameter that this consumer accepts.
 */
public interface ThrowingIoExceptionConsumer<T> {
    void accept(T item) throws IOException;
}
