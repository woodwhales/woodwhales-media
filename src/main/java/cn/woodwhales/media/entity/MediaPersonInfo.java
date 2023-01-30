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
 * 媒体人物信息表
 *
 * @author woodwhales on 2023-01-30 12:42:21
 *
 */
@TableName(value= "media_person_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaPersonInfo implements Serializable {
    
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
     * 中文名&英文名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 中文名
     */
    @TableField(value = "simple_name")
    private String simpleName;

    /**
     * 豆瓣id号
     */
    @TableField(value = "dou_ban_id")
    private String douBanId;

    /**
     * 豆瓣链接
     */
    @TableField(value = "url")
    private String url;

}