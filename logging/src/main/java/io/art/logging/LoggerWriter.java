package io.art.logging;

public interface LoggerWriter {
    void write(String thread, String message);
}
