package cn.woodwhales.meida.model.dto;

import cn.woodwhales.meida.model.enums.MediaTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * @author woodwhales on 2023-01-22 22:29
 */
@Data
public class MediaInfoDto {

    /**
     * 媒体类型
     */
    private MediaTypeEnum mediaTypeEnum;

    /**
     * 作品名称
     */
    private String name;
    /**
     * 发布时间年份
     */
    private Integer year;

    /**
     * 上映日期
     */
    private String publishDate;

    /**
     * 制片国家/地区
     */
    private String country;

    /**
     * 语言
     */
    private String language;

    /**
     * 片长，单位：分钟
     * 或者
     * 单集片长
     */
    private Integer length;

    /**
     * 别名
     */
    private String otherName;

    /**
     * imdb的id
     */
    private String imdbId;

    /**
     * 集数
     */
    private Integer episodes;
    /**
     * 导演集合
     */
    private List<String> directorList;
    /**
     * 主演集合
     */
    private List<String> starringList;
}
