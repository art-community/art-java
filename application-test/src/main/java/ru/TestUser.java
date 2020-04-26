package ru;

public class TestUser {
    public static void main(String[] args) {
        Test test = new Test();
        test.integer = 123;
        test.string = "mew";
        System.out.println(Test.toTest.map(Test.fromTest.map(test)));
    }
}
