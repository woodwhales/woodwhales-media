package cn.woodwhales.media.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.woodwhales.common.util.JsonTool;
import cn.woodwhales.media.model.dto.MediaInfoDto;
import cn.woodwhales.media.model.param.ParseParam;
import cn.woodwhales.media.util.ImageTool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

class DouBanParseMediaServiceImplTest {

    private static DouBanParseMediaServiceImpl douBanParseMediaService = new DouBanParseMediaServiceImpl();

    @Test
    public void test1() throws IOException {
        File file = new File("D:\\code\\woodwhales\\woodwhales-media\\doc\\我和我的祖国 (豆瓣).html");
        String html = FileUtil.readString(file, StandardCharsets.UTF_8);
        ParseParam parseParam = new ParseParam();
        parseParam.setHtml(html);
        MediaInfoDto mediaInfoDto = douBanParseMediaService.parseMovie(parseParam).getData();
        System.out.println("mediaInfoDto = " + JsonTool.toJSONString(mediaInfoDto));
    }

    @Test
    public void test2() throws IOException {
        File file = new File("D:\\code\\woodwhales\\woodwhales-media\\doc\\霸王别姬 (豆瓣).html");
        String html = FileUtil.readString(file, StandardCharsets.UTF_8);
        ParseParam parseParam = new ParseParam();
        parseParam.setHtml(html);
        MediaInfoDto mediaInfoDto = douBanParseMediaService.parseMovie(parseParam).getData();
        System.out.println("mediaInfoDto = " + JsonTool.toJSONString(mediaInfoDto));
    }

    @Test
    public void test6() throws IOException {
        File file = new File("D:\\code\\woodwhales\\woodwhales-media\\doc\\肖申克的救赎 (豆瓣).html");
        String html = FileUtil.readString(file, StandardCharsets.UTF_8);
        ParseParam parseParam = new ParseParam();
        parseParam.setHtml(html);
        MediaInfoDto mediaInfoDto = douBanParseMediaService.parseMovie(parseParam).getData();
        System.out.println("mediaInfoDto = " + JsonTool.toJSONString(mediaInfoDto));
    }

    @Test
    public void test5() throws IOException {
        HttpRequest httpRequest = HttpRequest.get("https://movie.douban.com/subject/3021640/");
        HttpResponse httpResponse = httpRequest.execute();
        String html = httpResponse.body();
        ParseParam parseParam = new ParseParam();
        parseParam.setHtml(html);
        MediaInfoDto mediaInfoDto = douBanParseMediaService.parseMovie(parseParam).getData();
        System.out.println("mediaInfoDto = " + JsonTool.toJSONString(mediaInfoDto));
    }

    @Test
    public void test3() {
        HttpRequest httpRequest = HttpRequest.get("https://movie.douban.com/subject/1291546/");
        HttpResponse httpResponse = httpRequest.execute();
        String html = httpResponse.body();
        ParseParam parseParam = new ParseParam();
        parseParam.setHtml(html);
        MediaInfoDto mediaInfoDto = douBanParseMediaService.parseMovie(parseParam).getData();
        System.out.println("mediaInfoDto = " + JsonTool.toJSONString(mediaInfoDto));
    }

    @Test
    public void test4() {
        String imageBase64 = ImageTool.getImageBase64ByUrl("https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2886441838.webp");
        System.out.println("imageBase64 = " + imageBase64);
    }

    @Test
    public void syncTop250() {
        File file = new File("D:\\code\\woodwhales\\woodwhales-media\\doc\\豆瓣电影 Top 250.html");
        String html = FileUtil.readString(file, StandardCharsets.UTF_8);
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByClass("grid_view");
        Element element = elements.get(0);
        Elements items = element.select("div[class=hd]");
        for (Element item : items) {
            String url = item.getElementsByTag("a").get(0).attr("href");
            String name = item.getElementsByTag("span").get(0).text();
            System.out.println("name = " + name + ", url = " + url);
        }
    }



}