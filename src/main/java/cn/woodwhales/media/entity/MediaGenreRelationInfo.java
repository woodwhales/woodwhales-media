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
 * 媒体类型关系表
 *
 * @author woodwhales on 2023-01-30 12:42:21
 *
 */
@TableName(value= "media_genre_relation_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaGenreRelationInfo implements Serializable {
    
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
     * media_info表主键
     */
    @TableField(value = "media_info_id")
    private Long mediaInfoId;

    /**
     * media_genre_info表主键
     */
    @TableField(value = "media_genre_info_id")
    private Long mediaGenreInfoId;

}