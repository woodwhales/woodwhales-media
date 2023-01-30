package cn.woodwhales.media.model.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author woodwhales on 2023-01-29 14:36
 */
@Data
public class MediaPersonDto {

    private String name;
    private String url;

    private String simpleName;

    private String douBanId;

    public String getSimpleName() {
        return StringUtils.substringBefore(this.name, " ");
    }

    public String getDouBanId() {
        return StringUtils.substringBetween(StringUtils.substringAfter(this.url, "/celebrity"), "/", "/");
    }
}
