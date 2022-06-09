package com.example.qinyiyuedu4.html;

import com.example.qinyiyuedu4.pojo.Content;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class Html_ShuJIXinXI {
    private ArrayList<Content> Data;

    //获取网站的内容
    @SneakyThrows
    public List<Content> parseSouSuo(String keywords) {
        try {
            Data = new ArrayList<>();
            String url = "https://www.qidian.com/soushu/" + keywords + ".html";
            Document doc = Jsoup.parse(new URL(url), 10000);
            Elements elements1 = doc.getElementsByClass("book-img-text").select("li");
            for (Element el : elements1) {
                //书名名
                String name = el.getElementsByClass("book-info-title").eq(0).select("a").attr("title");
                //作者名
                String zuo_zhe = el.getElementsByClass("author").select("a").eq(0).text();
                //简介
                String jianjie = el.getElementsByClass("intro").eq(0).select("p").text();
                //最新章节
                String zuixin = el.getElementsByClass("update").eq(0).select("a").text();
                //书籍详情地址
                String href = "https:" + el.getElementsByClass("book-img-box").eq(0).select("a").attr("href") + "#Catalog";
                //书籍封面图片
                String image = "https:" + el.getElementsByTag("img").eq(0).attr("src");

                Content content = new Content();
                content.setImg(image);
                content.setName(name.substring(0, name.length() - 4));
                content.setZuo_zhe(zuo_zhe);
                content.setZui_xin(zuixin);
                content.setJianjie(jianjie);
                content.setHref(href);
                Data.add(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Data;
    }
}