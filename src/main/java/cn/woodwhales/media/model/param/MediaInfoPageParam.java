package cn.woodwhales.media.model.param;

import lombok.Data;

import java.util.List;

/**
 * @author woodwhales on 2023-01-30 15:46
 */
@Data
public class MediaInfoPageParam {

    /**
     * 媒体名称
     */
    private String name;

    /**
     * 媒体类型
     */
    private List<String> mediaTypeList;

}
