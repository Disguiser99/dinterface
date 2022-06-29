package com.youdao.dinterface.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 有道精品课
 * dinterface
 * Description:
 * Created by Disguiser on 2022/4/12 14:21
 * Copyright @ 2022 网易有道. All rights reserved.
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DInterface {
    String groupName();
}
