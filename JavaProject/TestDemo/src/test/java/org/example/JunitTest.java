package org.example;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/16
 * Time: 17:14
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JunitTest {
/*    @Test
    @Order(2)
    void test1() {
        System.out.println("测试1");
    }

    @Test
    @Order(0)
    void test2() {
        System.out.println("测试2");
    }*/

/*
    @BeforeAll
    static void beforeAll() {
        System.out.println("在全部测试用例开始前,执行一次");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("在全部测试用例执行完后,执行一次");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("在每个方法执行前都执行一次");
    }

    @AfterEach
    void afterEach() {
        System.out.println("在每个方法执行后都执行一次");
    }*/

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void paramTest(int i) {
        System.out.println("第 " + i + " 次执行这个方法");
    }

    @ParameterizedTest
    @CsvSource({"'123',18"})
    void paramTest(String str, int id) {
        System.out.println(str + ":" + id);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "Demo1.csv")
    void paramTest01(String str, int id, char c) {
        System.out.println(str + ":" + id + ":" + c);
    }

    @ParameterizedTest
    @MethodSource("productParam")
    void paramTest02(String name, int age) {
        System.out.println(name + " " + age);
    }

    public static Stream<Arguments> productParam() {
        return Stream.of(
                Arguments.arguments("张三", 12),
                Arguments.arguments("lisi", 0)
        );
    }


}
