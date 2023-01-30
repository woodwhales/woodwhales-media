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
 * 媒体人物关系表
 *
 * @author woodwhales on 2023-01-30 12:56:34
 *
 */
@TableName(value= "media_person_relation_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaPersonRelationInfo implements Serializable {
    
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
     * media_person_info表主键
     */
    @TableField(value = "media_person_info_id")
    private Long mediaPersonInfoId;

    /**
     * 媒体类型
     */
    @TableField(value = "media_type_enum")
    private String mediaTypeEnum;

    /**
     * 媒体人物在当前作品的角色
     */
    @TableField(value = "person_type")
    private String personType;

}