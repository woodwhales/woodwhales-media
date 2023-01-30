package cn.woodwhales.media.service.impl;

import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.media.entity.MediaGenreInfo;
import cn.woodwhales.media.mapper.MediaGenreInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author woodwhales on 2023-01-30 12:42
 */
@Slf4j
@Service
public class MediaGenreInfoServiceImpl extends ServiceImpl<MediaGenreInfoMapper, MediaGenreInfo> {

    public OpResult<Void> saveOrUpdate() {
        return OpResult.success();
    }

}
