package com.vnjohn.easyexcel.util;

import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vnjohn
 * @date 2022/1/5
 */
@Slf4j
public class StarBeanUtils {

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Create class error, please check Construction method!");
        }
    }

    public static <T> T copySingle(Object sourceClazz, Class<T> targetClazz) {
        if (sourceClazz == null) {
            return null;
        }
        T t = newInstance(targetClazz);
        org.springframework.beans.BeanUtils.copyProperties(sourceClazz, t);
        return t;
    }

    public static <T> List<T> copyList(List<?> sourceClazz, Class<T> targetClazz) {
        if (sourceClazz == null) {
            return new ArrayList<T>();
        }
        return toDTO(new ArrayList<>(sourceClazz), targetClazz);
    }

    public static <T> List<T> toDTO(List<?> sourceClazz, Class<T> targetClazz) {
        if (sourceClazz == null) {
            return new ArrayList<>();
        }
        List<T> dtoList = new ArrayList<>();
        for (Object v : sourceClazz) {
            dtoList.add(copySingle(v, targetClazz));
        }
        return dtoList;
    }

}
