package cn.woodwhales.media.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author woodwhales on 2023-01-30 14:08
 */
@Getter
@AllArgsConstructor
public enum MediaPersonTypeEnum {

    /**
     * 导演
     */
    DIRECTOR(0, "导演"),
    /**
     * 编剧
     */
    AUTHOR(1, "编剧"),
    /**
     * 演员
     */
    ACTOR(2, "演员"),
    ;

    /**
     * code
     */
    private Integer code;

    /**
     * desc
     */
    private String desc;
}
