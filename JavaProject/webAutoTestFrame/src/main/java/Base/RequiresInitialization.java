package Base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE) // 保留在源码中，不会包含在编译后的类文件中
@Target({ElementType.METHOD, ElementType.TYPE}) // 注解可以应用于类、接口（包括注解类型）或枚举声明
public @interface RequiresInitialization {
    String function = "init";
}