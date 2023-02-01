package cn.woodwhales.media.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 媒体信息表
 *
 * @author woodwhales on 2023-01-30 12:42:21
 *
 */
@TableName(value= "media_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private java.util.Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private java.util.Date updateTime;

    /**
     * 媒体类型
     */
    @TableField(value = "media_type_enum")
    private String mediaTypeEnum;

    /**
     * 豆瓣链接
     */
    @TableField(value = "url")
    private String url;

    /**
     * 海报链接
     */
    @TableField(value = "image_url")
    private String imageUrl;

    /**
     * 海报图片base64
     */
    @TableField(value = "image_base64")
    private String imageBase64;

    /**
     * 作品名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 发布时间年份
     */
    @TableField(value = "year")
    private Integer year;

    /**
     * 上映日期
     */
    @TableField(value = "publish_date")
    private String publishDate;

    /**
     * 制片国家/地区
     */
    @TableField(value = "country")
    private String country;

    /**
     * 语言
     */
    @TableField(value = "language")
    private String language;

    /**
     * 片长，单位：分钟
     */
    @TableField(value = "length")
    private Integer length;

    /**
     * 豆瓣评分
     */
    @TableField(value = "dou_ban_score")
    private Double douBanScore;

    /**
     * 豆瓣Top250排名
     */
    @TableField(value = "dou_ban_top250_no")
    private String douBanTop250No;

    /**
     * 豆瓣Top250排名数值
     */
    @TableField(value = "dou_ban_top250_no_value")
    private Integer douBanTop250NoValue;

    /**
     * 又名
     */
    @TableField(value = "other_name")
    private String otherName;

    /**
     * imdb的id
     */
    @TableField(value = "imdb_id")
    private String imdbId;

    /**
     * 集数
     */
    @TableField(value = "episodes")
    private Integer episodes;

    /**
     * 简述
     */
    @TableField(value = "description")
    private String description;

}