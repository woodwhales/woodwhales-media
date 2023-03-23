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
    MOVIE(0, "电影", "success"),
    /**
     * 电视剧
     */
    TV_SERIES(1, "电视剧", "primary"),
    /**
     * 记录片
     */
    DOCUMENTARY(2, "记录片", "warning"),
    ;

    /**
     * code
     */
    private Integer code;

    /**
     * desc
     */
    private String desc;

    /**
     * 前端影视类型样式
     */
    private String typeClassForUi;

    public boolean match(Integer code) {
        return Objects.equals(this.code, code);
    }

    private static Map<Integer, MediaTypeEnum> map = DataTool.enumMap(MediaTypeEnum.class, MediaTypeEnum::getCode);
    private static Map<String, MediaTypeEnum> nameMap = DataTool.enumMap(MediaTypeEnum.class, MediaTypeEnum::name);

    public static String getDescByName(String mediaTypeEnumName) {
        return nameMap.containsKey(mediaTypeEnumName) ? nameMap.get(mediaTypeEnumName).getDesc() : "";
    }

    public static String getClassForUiBy(String mediaTypeEnumName) {
        return nameMap.containsKey(mediaTypeEnumName) ? nameMap.get(mediaTypeEnumName).getTypeClassForUi() : "primary";
    }

    public static String getDescByCode(Integer code) {
        return map.containsKey(code) ? map.get(code).getDesc() : null;
    }

}
