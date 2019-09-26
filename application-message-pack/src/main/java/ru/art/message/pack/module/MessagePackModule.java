package ru.art.message.pack.module;

import static ru.art.entity.Entity.*;
import static ru.art.message.pack.descriptor.MessagePackEntityWriter.*;
import java.io.*;
import java.util.*;

public class MessagePackModule {
    public static void main(String[] args) throws IOException {
        System.out.println(Arrays.toString(writeMessagePack(entityBuilder().stringField("Test", "Test").build())));
    }
}
