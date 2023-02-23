package com.xinder.common.util;

import com.baomidou.mybatisplus.extension.api.R;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author Xinder
 * @date 2023-02-23 12:17
 */
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {
}
