package cn.woodwhales.media.model.param;

import cn.woodwhales.common.model.param.PageQueryParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author woodwhales on 2023-01-30 15:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageParam<Param> extends PageQueryParam {

    /**
     * 查询参数
     */
    private Param param;

}
