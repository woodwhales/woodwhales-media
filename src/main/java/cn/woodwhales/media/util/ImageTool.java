package cn.woodwhales.media.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.util.Base64;

/**
 * @author woodwhales on 2023-01-29 15:34
 */
public class ImageTool {

    public static String getImageBase64ByUrl(String url) {
        HttpRequest httpRequest = HttpRequest.get(url);
        HttpResponse httpResponse = httpRequest.execute();
        byte[] bytes = httpResponse.bodyBytes();
        String imageBase64 = Base64.getEncoder().encodeToString(bytes);
        return imageBase64;
    }

}
