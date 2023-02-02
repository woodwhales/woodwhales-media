package cn.woodwhales.media.controller;

import cn.woodwhales.common.model.vo.PageRespVO;
import cn.woodwhales.common.model.vo.RespVO;
import cn.woodwhales.media.model.dto.MediaInfoDto;
import cn.woodwhales.media.model.param.MediaInfoDeleteParam;
import cn.woodwhales.media.model.param.MediaInfoDetailParam;
import cn.woodwhales.media.model.param.MediaInfoPageParam;
import cn.woodwhales.media.model.param.PageParam;
import cn.woodwhales.media.model.resp.MediaInfoDetailVo;
import cn.woodwhales.media.model.resp.MediaInfoPageVo;
import cn.woodwhales.media.service.impl.MediaInfoServiceImpl;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author woodwhales on 2023-01-30 12:51
 */
@Slf4j
@RestController
@RequestMapping("/api/mediaInfo")
public class MediaInfoController {

    @Autowired
    private MediaInfoServiceImpl mediaInfoServiceImpl;

    @PostMapping("/page")
    public RespVO<PageRespVO<MediaInfoPageVo>> queryPage(@RequestBody PageParam<MediaInfoPageParam> pageParam) {
        return mediaInfoServiceImpl.queryPage(pageParam);
    }

    @PostMapping("/detail")
    public RespVO<MediaInfoDetailVo> detail(@RequestBody MediaInfoDetailParam param) {
        return RespVO.resp(mediaInfoServiceImpl.detail(param));
    }

    @PostMapping("/delete")
    public RespVO<Void> delete(@RequestBody MediaInfoDeleteParam param) {
        return RespVO.resp(mediaInfoServiceImpl.delete(param));
    }

    @PostMapping("/saveOrUpdate")
    public RespVO<Void> saveOrUpdate(@RequestBody MediaInfoDto mediaInfoDto) {
        log.info("mediaInfoDto={}", JSON.toJSONString(mediaInfoDto));
        return RespVO.resp(mediaInfoServiceImpl.saveOrUpdate(mediaInfoDto));
    }

}
