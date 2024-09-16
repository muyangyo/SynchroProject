package org.example;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/16
 * Time: 20:21
 */
//@SelectClasses({JunitTest.class, Main.class})

@Suite
@SelectPackages(value = {"example"})
public class RunTestsSetting {
}









