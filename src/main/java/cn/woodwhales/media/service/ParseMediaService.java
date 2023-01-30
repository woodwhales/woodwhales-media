package cn.woodwhales.media.service;

import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.media.model.dto.MediaInfoDto;
import cn.woodwhales.media.model.param.ParseParam;

/**
 * @author woodwhales on 2023-01-22 22:28
 */
public interface ParseMediaService {

    /**
     * 解析电影
     * @param param
     * @return
     */
    OpResult<MediaInfoDto> parseMovie(ParseParam param);
}
