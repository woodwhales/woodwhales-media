package cn.woodwhales.media.controller;

import cn.woodwhales.common.model.vo.RespVO;
import cn.woodwhales.media.model.dto.MediaInfoDto;
import cn.woodwhales.media.model.param.ParseParam;
import cn.woodwhales.media.service.ParseMediaService;
import cn.woodwhales.media.service.impl.MediaInfoServiceImpl;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author woodwhales on 2023-01-29 11:53
 */
@Slf4j
@RestController
@RequestMapping("/api/parse")
public class ParseController {

    @Autowired
    private ParseMediaService parseMediaService;

    @Autowired
    private MediaInfoServiceImpl mediaInfoServiceImpl;

    /**
     * 解析电影
     * @param param
     * @return
     */
    @PostMapping("/parse")
    public RespVO<MediaInfoDto> parse(@RequestBody ParseParam param) {
        return RespVO.resp(parseMediaService.parseMovie(param));
    }

    @PostMapping("/saveOrUpdate")
    public RespVO<Void> saveOrUpdate(@RequestBody MediaInfoDto mediaInfoDto) {
        log.info("mediaInfoDto={}", JSON.toJSONString(mediaInfoDto));
        return RespVO.resp(mediaInfoServiceImpl.saveOrUpdate(mediaInfoDto));
    }

    @GetMapping("/syncTop250")
    public RespVO<List<Pair<String, String>>> syncTop250() {
        return RespVO.resp(mediaInfoServiceImpl.syncTop250());
    }

}
