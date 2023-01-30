package cn.woodwhales.media.service.impl;

import cn.hutool.http.HttpRequest;
import cn.woodwhales.common.business.DataTool;
import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.media.model.dto.MediaInfoDto;
import cn.woodwhales.media.model.dto.douban.DouBanInfoDto;
import cn.woodwhales.media.model.dto.douban.DouBanPersonDto;
import cn.woodwhales.media.model.enums.MediaTypeEnum;
import cn.woodwhales.media.model.param.ParseParam;
import cn.woodwhales.media.service.ParseMediaService;
import cn.woodwhales.media.util.BeanTool;
import cn.woodwhales.media.util.ImageTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author woodwhales on 2023-01-22 22:28
 */
@Service
public class DouBanParseMediaServiceImpl implements ParseMediaService {

    @Override
    public OpResult<MediaInfoDto> parseMovie(ParseParam param) {
        if(isBlank(param.getUrl()) && isBlank(param.getHtml())) {
            return OpResult.error("请求报文不允许为空");
        }

        if(isNotBlank(param.getUrl()) && isBlank(param.getHtml())) {
            param.setHtml(HttpRequest.get(param.getUrl()).execute().body());
        }

        Document document = Jsoup.parse(param.getHtml());
        String jsonData = document.select("script[type=application/ld+json]").get(0).data();
        JSONObject jsonObject = JSONObject.parseObject(jsonData);

        // 媒体类型
        MediaTypeEnum mediaTypeEnum = null;
        if(StringUtils.equals(jsonObject.get("@type").toString(), "TVSeries")) {
            mediaTypeEnum = MediaTypeEnum.TV_SERIES;
        } else if(StringUtils.equals(jsonObject.get("@type").toString(), "Movie")) {
            mediaTypeEnum = MediaTypeEnum.MOVIE;
        }

        // 电影名称
        String name = StringEscapeUtils.unescapeHtml4(jsonObject.get("name").toString());

        // 导演列表
        List<Map<String, String>> directorList = JSONArray.parseObject(JSON.toJSONString(jsonObject.get("director")), List.class);

        // 编剧列表
        List<Map<String, String>> authorList = JSONArray.parseObject(JSON.toJSONString(jsonObject.get("author")), List.class);

        // 演员列表
        List<Map<String, String>> actorList = JSONArray.parseObject(JSON.toJSONString(jsonObject.get("actor")), List.class);

        // 上映时间
        String datePublished = jsonObject.get("datePublished").toString();

        // 类型列表
        List<String> genreList = JSONArray.parseObject(jsonObject.get("genre").toString(), List.class);

        // 剧情简介
        String description = "";
        Elements all = document.select("span[class='all hidden']");
        if(CollectionUtils.isNotEmpty(all)) {
            description = StringUtils.replace(all.get(0).html(), "<br>", "\n\n");
        } else {
            description = StringUtils.replace(document.select("span[property=v:summary]").html(), "<br>", "\n");
        }
        description = StringUtils.replace(description, "　　", "");
        description = StringUtils.replace(description, " ", "");

        // 国家
        String country = StringUtils.substringBetween(param.getHtml(), "制片国家/地区:</span> ", "<br");

        // 语言
        String language = StringUtils.substringBetween(param.getHtml(), "语言:</span> ", "<br");

        // 又名
        String otherName = StringUtils.substringBetween(param.getHtml(), "又名:</span> ", "<br");

        // imdb的id
        String imdbId = StringUtils.substringBetween(param.getHtml(), "IMDb:</span> ", "<br");

        // 片长
        String runtime = "";
        // 集数
        Integer episodes = null;
        if(MediaTypeEnum.TV_SERIES.equals(mediaTypeEnum)) {
            runtime = StringUtils.substringBefore(StringUtils.substringBetween(param.getHtml(), "片长:</span> ", "<br"), "分钟");
            episodes = Integer.parseInt(StringUtils.substringBetween(param.getHtml(), "集数:</span> ", "<br"));
        } else if(MediaTypeEnum.MOVIE.equals(mediaTypeEnum)) {
            runtime = document.select("span[property=v:runtime]").attr("content");
        }

        // 豆瓣评分
        Map<String, String> aggregateRating = JSONArray.parseObject(jsonObject.get("aggregateRating").toString(), Map.class);
        String douBanScore = aggregateRating.get("ratingValue");

        // 豆瓣Top250排名
        String douBanTop250No = null;
        Elements douBanTop250NoElements = document.select("span[class='top250-no']");
        if(CollectionUtils.isNotEmpty(douBanTop250NoElements)) {
            douBanTop250No = douBanTop250NoElements.get(0).text();
        }

        // 豆瓣链接
        String url = jsonObject.get("url").toString();

        // 海报图片链接
        String imageUrl = jsonObject.get("image").toString();

        DouBanInfoDto douBanInfoDto = new DouBanInfoDto();
        douBanInfoDto.setName(name);
        douBanInfoDto.setUrl(url);
        douBanInfoDto.setImageUrl(imageUrl);
        douBanInfoDto.setImageBase64(ImageTool.getImageBase64ByUrl(imageUrl));
        douBanInfoDto.setCountry(country);
        douBanInfoDto.setLanguage(language);
        douBanInfoDto.setOtherName(otherName);
        douBanInfoDto.setMediaTypeEnum(mediaTypeEnum);
        douBanInfoDto.setImdbId(imdbId);
        douBanInfoDto.setEpisodes(episodes);
        douBanInfoDto.setDirectorList(convertPerson(directorList));
        douBanInfoDto.setAuthorList(convertPerson(authorList));
        douBanInfoDto.setActorList(convertPerson(actorList));
        douBanInfoDto.setDatePublished(datePublished);
        douBanInfoDto.setGenreList(genreList);
        douBanInfoDto.setDescription(description);
        douBanInfoDto.setRuntime(runtime);
        douBanInfoDto.setDouBanTop250No(douBanTop250No);
        douBanInfoDto.setDouBanScore(Double.parseDouble(douBanScore));

        MediaInfoDto mediaInfoDto = BeanTool.copy(douBanInfoDto, MediaInfoDto::new);
        mediaInfoDto.setPublishDate(douBanInfoDto.getDatePublished());
        mediaInfoDto.setYear(douBanInfoDto.letYear());
        mediaInfoDto.setLength(douBanInfoDto.letLength());
        mediaInfoDto.setDirectorList(douBanInfoDto.letDirectorList());
        mediaInfoDto.setActorList(douBanInfoDto.letActorList());
        mediaInfoDto.setAuthorList(douBanInfoDto.letAuthorList());
        return OpResult.success(mediaInfoDto);
    }

    private List<DouBanPersonDto> convertPerson(List<Map<String, String>> dataList) {
        return DataTool.toList(dataList, data -> {
            DouBanPersonDto douBanPersonDto = new DouBanPersonDto();
            douBanPersonDto.setName(data.get("name"));
            douBanPersonDto.setUrl(data.get("url"));
            return douBanPersonDto;
        });
    }

}
