package cn.woodwhales.media.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.woodwhales.common.business.DataTool;
import cn.woodwhales.common.business.collection.CollectionMathResult;
import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.common.model.vo.PageRespVO;
import cn.woodwhales.common.model.vo.RespVO;
import cn.woodwhales.common.mybatisplus.MybatisPlusExecutor;
import cn.woodwhales.media.entity.*;
import cn.woodwhales.media.mapper.MediaInfoMapper;
import cn.woodwhales.media.model.dto.MediaInfoDto;
import cn.woodwhales.media.model.dto.MediaPersonDto;
import cn.woodwhales.media.model.enums.MediaPersonTypeEnum;
import cn.woodwhales.media.model.param.*;
import cn.woodwhales.media.model.resp.MediaInfoDetailVo;
import cn.woodwhales.media.model.resp.MediaInfoPageVo;
import cn.woodwhales.media.service.ParseMediaService;
import cn.woodwhales.media.util.BeanTool;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author woodwhales on 2023-01-30 11:50
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MediaInfoServiceImpl extends ServiceImpl<MediaInfoMapper, MediaInfo> {

    @Autowired
    private MediaGenreInfoServiceImpl mediaGenreInfoServiceImpl;

    @Autowired
    private MediaGenreRelationInfoServiceImpl mediaGenreRelationInfoServiceImpl;

    @Autowired
    private MediaPersonInfoServiceImpl mediaPersonInfoServiceImpl;

    @Autowired
    private MediaPersonRelationInfoServiceImpl mediaPersonRelationInfoServiceImpl;

    public OpResult<Void> saveOrUpdate(MediaInfoDto mediaInfoDto) {
        MediaInfo mediaInfo = this.getOne(Wrappers.<MediaInfo>lambdaQuery()
                .eq(MediaInfo::getUrl, mediaInfoDto.getUrl()));
        Date now = new Date();
        if(Objects.isNull(mediaInfo)) {
            // 创建
            mediaInfo = new MediaInfo();
            mediaInfo.setCreateTime(now);
        }
        BeanUtils.copyProperties(mediaInfoDto, mediaInfo);
        if(StringUtils.isNotBlank(mediaInfo.getDouBanTop250No())) {
            Integer douBanTop250NoValue = Integer.parseInt(StringUtils.substringAfter(mediaInfo.getDouBanTop250No(), "No."));
            mediaInfo.setDouBanTop250NoValue(douBanTop250NoValue);
        }
        mediaInfo.setUpdateTime(now);
        mediaInfo.setMediaTypeEnum(mediaInfoDto.getMediaTypeEnum().name());
        this.saveOrUpdate(mediaInfo);
        // 媒体类型关系
        this.saveOrUpdateGenre(mediaInfoDto, mediaInfo, now);
        // 媒体人物关系
        this.saveOrUpdatePerson(mediaInfoDto, mediaInfo, now);
        return OpResult.success();
    }

    private void saveOrUpdatePerson(MediaInfoDto mediaInfoDto, MediaInfo mediaInfo, Date now) {
        List<MediaPersonInfo> mediaPersonInfoList = this.getMediaPersonInfoList(mediaInfoDto, now);
        Map<String, MediaPersonInfo> mediaPersonInfoMap = DataTool.toMap(mediaPersonInfoList, MediaPersonInfo::getUrl);
        // 导演集合
        this.saveOrUpdateForMediaPersonRelation(mediaInfoDto.getDirectorList(), mediaPersonInfoMap, mediaInfo, now, MediaPersonTypeEnum.DIRECTOR);
        // 编剧集合
        this.saveOrUpdateForMediaPersonRelation(mediaInfoDto.getAuthorList(), mediaPersonInfoMap, mediaInfo, now, MediaPersonTypeEnum.AUTHOR);
        // 演员集合
        this.saveOrUpdateForMediaPersonRelation(mediaInfoDto.getActorList(), mediaPersonInfoMap, mediaInfo, now, MediaPersonTypeEnum.ACTOR);
    }

    private void saveOrUpdateForMediaPersonRelation(List<MediaPersonDto> mediaPersonInfoList,
                                                    Map<String, MediaPersonInfo> mediaPersonInfoMap,
                                                    MediaInfo mediaInfo,
                                                    Date now,
                                                    MediaPersonTypeEnum mediaPersonTypeEnum) {
        List<MediaPersonRelationInfo> mediaRelationInfoList = mediaPersonRelationInfoServiceImpl.list(Wrappers.<MediaPersonRelationInfo>lambdaQuery()
                .eq(MediaPersonRelationInfo::getMediaInfoId, mediaInfo.getId())
                .eq(MediaPersonRelationInfo::getPersonType, mediaPersonTypeEnum.name()));
        List<MediaPersonInfo> personInfoList = DataTool.toList(mediaPersonInfoList, mediaPersonInfo -> mediaPersonInfoMap.get(mediaPersonInfo.getUrl()));
        CollectionMathResult<Long, MediaPersonInfo, MediaPersonRelationInfo> compute =
                CollectionMathResult.compute(personInfoList, MediaPersonInfo::getId, mediaRelationInfoList, MediaPersonRelationInfo::getMediaPersonInfoId);
        List<MediaPersonInfo> positiveDifferenceList = compute.getPositiveDifferenceList();
        if(CollectionUtils.isNotEmpty(positiveDifferenceList)) {
            List<MediaPersonRelationInfo> newMediaPersonRelationInfo = DataTool.toList(positiveDifferenceList, mediaPersonInfo -> {
                MediaPersonRelationInfo mediaPersonRelationInfo = new MediaPersonRelationInfo();
                mediaPersonRelationInfo.setMediaPersonInfoId(mediaPersonInfo.getId());
                mediaPersonRelationInfo.setMediaInfoId(mediaInfo.getId());
                mediaPersonRelationInfo.setMediaTypeEnum(mediaInfo.getMediaTypeEnum());
                mediaPersonRelationInfo.setPersonType(mediaPersonTypeEnum.name());
                mediaPersonRelationInfo.setUpdateTime(now);
                mediaPersonRelationInfo.setCreateTime(now);
                return mediaPersonRelationInfo;
            });
            mediaPersonRelationInfoServiceImpl.saveBatch(newMediaPersonRelationInfo);
        }

        List<MediaPersonRelationInfo> negativeDifferenceList = compute.getNegativeDifferenceList();
        if(CollectionUtils.isNotEmpty(negativeDifferenceList)) {
            mediaPersonRelationInfoServiceImpl.removeBatchByIds(DataTool.toList(negativeDifferenceList, MediaPersonRelationInfo::getId));
        }
    }

    private List<MediaPersonInfo> getMediaPersonInfoList(MediaInfoDto mediaInfoDto, Date now) {
        List<MediaPersonDto> directorList = mediaInfoDto.getDirectorList();
        List<MediaPersonDto> authorList = mediaInfoDto.getAuthorList();
        List<MediaPersonDto> actorList = mediaInfoDto.getActorList();
        Set<MediaPersonDto> mediaPersonDtoSet = new HashSet<>();
        mediaPersonDtoSet.addAll(directorList);
        mediaPersonDtoSet.addAll(authorList);
        mediaPersonDtoSet.addAll(actorList);

        List<MediaPersonInfo> allPersonInfoList = new ArrayList<>();
        if(CollectionUtils.isEmpty(mediaPersonDtoSet)) {
            return allPersonInfoList;
        }

        List<MediaPersonDto> mediaPersonDtoList = new ArrayList<>();
        mediaPersonDtoList.addAll(mediaPersonDtoSet);
        List<MediaPersonInfo> personInfoList = mediaPersonInfoServiceImpl.list(Wrappers.<MediaPersonInfo>lambdaQuery()
                .in(MediaPersonInfo::getUrl, DataTool.toList(mediaPersonDtoList, MediaPersonDto::getUrl)));
        allPersonInfoList.addAll(personInfoList);
        CollectionMathResult<String, MediaPersonDto, MediaPersonInfo> compute =
                CollectionMathResult.compute(mediaPersonDtoList, MediaPersonDto::getUrl, personInfoList, MediaPersonInfo::getUrl);
        List<MediaPersonDto> positiveDifferenceList = compute.getPositiveDifferenceList();
        if(CollectionUtils.isNotEmpty(positiveDifferenceList)) {
            List<MediaPersonInfo> newPersonInfoList = DataTool.toList(positiveDifferenceList, mediaPersonDto -> {
                MediaPersonInfo mediaPersonInfo = new MediaPersonInfo();
                BeanUtils.copyProperties(mediaPersonDto, mediaPersonInfo);
                mediaPersonInfo.setCreateTime(now);
                mediaPersonInfo.setUpdateTime(now);
                return mediaPersonInfo;
            });
            mediaPersonInfoServiceImpl.saveBatch(newPersonInfoList);
            allPersonInfoList.addAll(newPersonInfoList);
        }
        return allPersonInfoList;
    }

    private void saveOrUpdateGenre(MediaInfoDto mediaInfoDto, MediaInfo mediaInfo, Date now) {
        List<MediaGenreInfo> allGenreList = getMediaGenreInfoList(mediaInfoDto, now);
        Map<String, MediaGenreInfo> mediaGenreMap = DataTool.toMap(allGenreList, MediaGenreInfo::getName);

        List<MediaGenreRelationInfo> newMediaGenreRelationInfoList = DataTool.toList(mediaInfoDto.getGenreList(), genre -> {
            MediaGenreRelationInfo mediaGenreRelationInfo = new MediaGenreRelationInfo();
            mediaGenreRelationInfo.setMediaInfoId(mediaInfo.getId());
            mediaGenreRelationInfo.setMediaGenreInfoId(mediaGenreMap.get(genre).getId());
            mediaGenreRelationInfo.setUpdateTime(now);
            mediaGenreRelationInfo.setCreateTime(now);
            return mediaGenreRelationInfo;
        });

        List<MediaGenreRelationInfo> mediaGenreRelationInfoList = mediaGenreRelationInfoServiceImpl.list(Wrappers.<MediaGenreRelationInfo>lambdaQuery()
                .eq(MediaGenreRelationInfo::getMediaInfoId, mediaInfo.getId()));
        CollectionMathResult<Long, MediaGenreRelationInfo, MediaGenreRelationInfo> compute1 = CollectionMathResult.compute(newMediaGenreRelationInfoList, MediaGenreRelationInfo::getMediaGenreInfoId,
                mediaGenreRelationInfoList, MediaGenreRelationInfo::getMediaGenreInfoId);
        List<MediaGenreRelationInfo> positiveDifferenceList = compute1.getPositiveDifferenceList();
        if(CollectionUtils.isNotEmpty(positiveDifferenceList)) {
            mediaGenreRelationInfoServiceImpl.saveBatch(positiveDifferenceList);
        }
        List<MediaGenreRelationInfo> negativeDifferenceList = compute1.getNegativeDifferenceList();
        if(CollectionUtils.isNotEmpty(negativeDifferenceList)) {
            mediaGenreRelationInfoServiceImpl.removeBatchByIds(DataTool.toList(negativeDifferenceList, MediaGenreRelationInfo::getId));
        }
    }

    private List<MediaGenreInfo> getMediaGenreInfoList(MediaInfoDto mediaInfoDto, Date now) {
        List<MediaGenreInfo> allGenreList = new ArrayList<>();

        List<String> genreList = mediaInfoDto.getGenreList();
        List<MediaGenreInfo> mediaGenreList = mediaGenreInfoServiceImpl.list(Wrappers.<MediaGenreInfo>lambdaQuery()
                .in(MediaGenreInfo::getName, genreList));
        CollectionMathResult<String, String, MediaGenreInfo> compute = CollectionMathResult.compute(genreList, Function.identity(),
                mediaGenreList, MediaGenreInfo::getName);
        Set<String> positiveDifferenceKeySet = compute.getPositiveDifferenceKeySet();
        if(CollectionUtils.isNotEmpty(positiveDifferenceKeySet)) {
            List<MediaGenreInfo> newMediaGenreInfoList = positiveDifferenceKeySet.stream().map(genre -> {
                MediaGenreInfo mediaGenreInfo = new MediaGenreInfo();
                mediaGenreInfo.setName(genre);
                mediaGenreInfo.setUpdateTime(now);
                mediaGenreInfo.setCreateTime(now);
                return mediaGenreInfo;
            }).collect(Collectors.toList());
            mediaGenreInfoServiceImpl.saveBatch(newMediaGenreInfoList);
            allGenreList.addAll(newMediaGenreInfoList);
        }
        allGenreList.addAll(mediaGenreList);
        return allGenreList;
    }

    public RespVO<PageRespVO<MediaInfoPageVo>> queryPage(PageParam<MediaInfoPageParam> pageParam) {
        return MybatisPlusExecutor.page(this, pageParam, wrapper -> {
            wrapper.like(MediaInfo::getName, pageParam.getParam().getName());
            wrapper.orderByDesc(MediaInfo::getUpdateTime);
        }, info -> BeanTool.copy(info, MediaInfoPageVo::new));

    }

    public OpResult<MediaInfoDetailVo> detail(MediaInfoDetailParam param) {
        MediaInfo info = this.getById(param.getId());
        MediaInfoDetailVo vo = BeanTool.copy(info, MediaInfoDetailVo::new);
        return OpResult.success(vo);
    }

    @Autowired
    private ParseMediaService parseMediaService;

    /**
     * 同步 top250
     * 调用太频繁会被豆瓣限制IP
     * @return
     */
    public OpResult<Void> syncTop2() {
        List<Pair<String, String>> errorList = new ArrayList<>();
        OpResult<List<Pair<String, String>>> opResult = this.syncTop250();
        List<Pair<String, String>> dataList = opResult.getData();
        for (Pair<String, String> pair : dataList) {
            try {
                log.info("==> 同步：name={}, url={}", pair.getRight(), pair.getLeft());
                MediaInfo one = this.getOne(Wrappers.<MediaInfo>lambdaQuery()
                        .eq(MediaInfo::getUrl, pair.getLeft()));
                if(Objects.isNull(one)) {
                    String url = pair.getKey();
                    ParseParam parseParam = new ParseParam();
                    parseParam.setUrl(url);
                    OpResult<MediaInfoDto> opResult2 = parseMediaService.parseMovie(parseParam);
                    MediaInfoDto mediaInfoDto = opResult2.getData();
                    this.saveOrUpdate(mediaInfoDto);
                }
            } catch (Exception e) {
                errorList.add(pair);
                log.info("==> 同步失败：name={}, url={}, errorMsg={}", pair.getRight(), pair.getLeft(), e.getMessage(), e);
            }
        }
        log.error("errorList={}", JSON.toJSONString(errorList));
        return OpResult.success();
    }

    /**
     * 获取top250
     * 调用太频繁会被豆瓣限制IP
     * @return
     */
    public OpResult<List<Pair<String, String>>> syncTop250() {
        List<Pair<String, String>> top250Container = new ArrayList<>();
        for (int i = 0; i < 250;) {
            String linkUrl = String.format("https://movie.douban.com/top250?start=%s&filter=", i);
            HttpRequest httpRequest = HttpRequest.get(linkUrl);
            HttpResponse httpResponse = httpRequest.execute();
            String html = httpResponse.body();
            Document document = Jsoup.parse(html);
            Elements elements = document.getElementsByClass("grid_view");
            Element element = elements.get(0);
            Elements items = element.select("div[class=hd]");
            for (Element item : items) {
                String url = item.getElementsByTag("a").get(0).attr("href");
                String name = item.getElementsByTag("span").get(0).text();
                top250Container.add(Pair.of(url, name));
            }
            i = i + 25;
        }
        log.info("top250Container = {}", JSON.toJSONString(top250Container));
        return OpResult.success(top250Container);
    }

    public OpResult<Void> delete(MediaInfoDeleteParam param) {
        MediaInfo mediaInfo = this.getById(param.getId());
        if(Objects.isNull(mediaInfo)) {
            return OpResult.success();
        }
        // 删除媒体类型关系
        mediaGenreRelationInfoServiceImpl.remove(Wrappers.<MediaGenreRelationInfo>lambdaQuery()
                .eq(MediaGenreRelationInfo::getMediaInfoId, mediaInfo.getId()));
        // 删除媒体人物关系
        mediaPersonRelationInfoServiceImpl.remove(Wrappers.<MediaPersonRelationInfo>lambdaQuery()
                .eq(MediaPersonRelationInfo::getMediaInfoId, mediaInfo.getId()));
        this.removeById(mediaInfo.getId());
        return OpResult.success();
    }
}
