package cn.woodwhales.media.model.resp;

import cn.woodwhales.media.model.enums.MediaTypeEnum;
import lombok.Data;

/**
 * @author woodwhales on 2023-01-30 15:42
 * @see cn.woodwhales.media.entity.MediaInfo
 */
@Data
public class MediaInfoPageVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private java.util.Date createTime;

    /**
     * 更新时间
     */
    private java.util.Date updateTime;

    /**
     * 媒体类型
     * @see cn.woodwhales.media.model.enums.MediaTypeEnum
     */
    private String mediaTypeEnum;

    /**
     * 媒体类型样式
     */
    private String mediaTypeClass;

    /**
     * 媒体类型名称
     */
    private String mediaTypeName;

    /**
     * 豆瓣链接
     */
    private String url;

    /**
     * 海报链接
     */
    private String imageUrl;

    /**
     * 海报图片base64
     */
    private String imageBase64;

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
     */
    private Integer length;

    /**
     * 豆瓣评分
     */
    private Double douBanScore;

    /**
     * 豆瓣Top250排名
     */
    private String douBanTop250No;

    /**
     * 又名
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
     * 简述
     */
    private String description;

    public String getMediaTypeName() {
        return MediaTypeEnum.getDescByName(this.mediaTypeEnum);
    }
}
