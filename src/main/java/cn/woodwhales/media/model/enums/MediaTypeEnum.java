package cn.woodwhales.media.model.enums;

import cn.woodwhales.common.business.DataTool;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

/**
 * @author woodwhales on 2023-01-22 22:38
 */
@Getter
@AllArgsConstructor
public enum MediaTypeEnum {

    /**
     * 电影
     */
    MOVIE(0, "电影"),
    /**
     * 电视剧
     */
    TV_SERIES(1, "电视剧"),
    ;

    /**
     * code
     */
    private Integer code;

    /**
     * desc
     */
    private String desc;

    public boolean match(Integer code) {
        return Objects.equals(this.code, code);
    }

    private static Map<Integer, MediaTypeEnum> map = DataTool.enumMap(MediaTypeEnum.class, MediaTypeEnum::getCode);

    public static String getDescByCode(Integer code) {
        return map.containsKey(code) ? map.get(code).getDesc() : null;
    }

}
