package ru.art.test.specification;

import ru.art.entity.*;
import static ru.art.entity.Entity.*;
import static ru.art.entity.xml.XmlEntityFromEntityConverter.*;
import static ru.art.json.descriptor.JsonEntityWriter.*;
import static ru.art.message.pack.descriptor.MessagePackEntityWriter.*;
import static ru.art.protobuf.descriptor.ProtobufEntityWriter.*;
import static ru.art.xml.descriptor.XmlEntityWriter.*;

public class Test {
    public static void main(String[] args) {
        Entity value = entityBuilder().build();
        System.out.println("Entity elements count: " + 1);

        System.out.println("Json size: " + writeJsonToBytes(value).length);
        System.out.println("MPack size: " + writeMessagePackToBytes(value).length);
        System.out.println("Protobuf size: " + writeProtobufToBytes(value).length);
    }
}
