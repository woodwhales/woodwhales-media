package cn.woodwhales.meida.controller;

import cn.woodwhales.common.model.vo.RespVO;
import cn.woodwhales.meida.model.dto.MediaInfoDto;
import cn.woodwhales.meida.model.param.ParseParam;
import cn.woodwhales.meida.service.ParseMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author woodwhales on 2023-01-29 11:53
 */
@RestController
@RequestMapping("/api")
public class ParseController {

    @Autowired
    private ParseMediaService parseMediaService;

    /**
     * 解析电影
     * @param param
     * @return
     */
    @PostMapping("/parse")
    public RespVO<MediaInfoDto> parse(@RequestBody ParseParam param) {
        return RespVO.resp(parseMediaService.parseMovie(param));
    }

}
