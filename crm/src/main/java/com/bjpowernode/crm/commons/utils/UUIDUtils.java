package com.bjpowernode.crm.commons.utils;

import java.util.UUID;

/**
 * @author ccstart
 * @Description
 * @create 2022-05-06 18:05
 */
public class UUIDUtils {
    /**
     * 创建一个无重复id
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
