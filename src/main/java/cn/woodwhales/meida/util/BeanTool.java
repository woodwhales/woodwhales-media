package cn.woodwhales.meida.util;


import org.springframework.beans.BeanUtils;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author woodwhales on 2023-01-29 14:38
 */
public class BeanTool {
    
    public static <T> T copy(Object object, Supplier<T> supplier) {
        if(Objects.isNull(object)) {
            return supplier.get();
        }
        T t = supplier.get();
        BeanUtils.copyProperties(object, t);
        return t;
    }
    
}
