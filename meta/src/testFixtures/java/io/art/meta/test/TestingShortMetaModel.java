package io.art.meta.test;


import lombok.*;

@Builder
@Getter
@ToString
@AllArgsConstructor
public class TestingShortMetaModel {
    int id;
    String name;
    Inner inner;

    @Builder
    @Getter
    @ToString
    @AllArgsConstructor
    public static class Inner {
        int id;
        String name;
    }
}

