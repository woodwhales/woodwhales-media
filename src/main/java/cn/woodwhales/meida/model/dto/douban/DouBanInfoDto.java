package cn.woodwhales.meida.model.dto.douban;

import cn.hutool.core.date.DateUtil;
import cn.woodwhales.common.business.DataTool;
import cn.woodwhales.meida.model.dto.MediaPersonDto;
import cn.woodwhales.meida.model.enums.MediaTypeEnum;
import cn.woodwhales.meida.util.BeanTool;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author woodwhales on 2023-01-29 11:37
 */
@Data
public class DouBanInfoDto {

    /**
     * 媒体类型
     */
    private MediaTypeEnum mediaTypeEnum;

    private String name;
    private String url;
    private String imageUrl;
    private String imageBase64;
    /**
     * 制片国家/地区
     */
    private String country;
    /**
     * 语言
     */
    private String language;
    /**
     * 又名
     */
    private String otherName;
    /**
     * imdb的id
     */
    private String imdbId;
    /**
     * 豆瓣评分
     */
    private Double douBanScore;
    /**
     * 豆瓣Top250排名
     */
    private String douBanTop250No;
    /**
     * 集数
     */
    private Integer episodes;
    private List<DouBanPersonDto> directorList;
    private List<DouBanPersonDto> authorList;
    private List<DouBanPersonDto> actorList;
    /**
     * 上映日期
     */
    private String datePublished;
    private List<String> genreList;
    private String description;
    private String runtime;

    public Integer letYear() {
        return Integer.parseInt(DateFormatUtils.format(DateUtil.parse(this.datePublished), "yyyy"));
    }

    public Integer letLength() {
        if(StringUtils.isBlank(this.runtime)) {
            return null;
        }
        return Integer.parseInt(this.runtime);
    }

    public List<MediaPersonDto> letDirectorList() {
        return DataTool.toList(this.directorList, data -> BeanTool.copy(data, MediaPersonDto::new));
    }

    public List<MediaPersonDto> letAuthorList() {
        return DataTool.toList(this.authorList, data -> BeanTool.copy(data, MediaPersonDto::new));
    }

    public List<MediaPersonDto> letActorList() {
        return DataTool.toList(this.actorList, data -> BeanTool.copy(data, MediaPersonDto::new));
    }
}
