package converter;

import java.lang.reflect.Field;

public class MetaToVoConverter {

    public static <T> T convert(Object meta, Class<T> voClass) throws Exception {
        T voInstance = voClass.getDeclaredConstructor().newInstance();

        for (Field voField : voClass.getDeclaredFields()) {
            voField.setAccessible(true);
            //检查是否需要映射
            NoNeedMetaMapping annotation = voField.getAnnotation(NoNeedMetaMapping.class);
            if (annotation != null) {
                continue;
            }

            String metaFieldName = "";
            try {
                // 检查是否有 @MetaField 注解,有则获取映射的字段,没有则获取同名的属性
                MetaMapping metaMappingAnnotation = voField.getAnnotation(MetaMapping.class);
                metaFieldName = metaMappingAnnotation != null ? metaMappingAnnotation.value() : voField.getName();

                // 获取 Meta 对象中的对应字段
                Field metaField = meta.getClass().getDeclaredField(metaFieldName);
                metaField.setAccessible(true);

                // 将 Meta 对象的字段值赋给 VO 对象
                voField.set(voInstance, metaField.get(meta));
            } catch (NoSuchFieldException e) {
                throw new NoSuchFieldException("无法映射对应字段 {" + voClass + "." + metaFieldName + "} ,如果不需要映射,请使用 @NoNeedMetaMapping 注解");
            }
        }

        return voInstance;
    }
}