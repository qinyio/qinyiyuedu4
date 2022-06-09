package com.example.qinyiyuedu4.html;

import android.content.Context;

import com.example.qinyiyuedu4.pojo.Content;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import lombok.SneakyThrows;

public class HtmlShuCheng {

    private ArrayList<Content> Data;

    //获取书城数据
    @SneakyThrows
    public ArrayList<Content> parseShuCheng(String keywords) {
        try {
            Data = new ArrayList<>();
            String url = "https://www.qidian.com/rank/yuepiao/" + keywords;
            Document doc = Jsoup.parse(new URL(url), 10000);
            Elements elements1 = doc.getElementsByClass("rank-view-list").select("li");
            for (Element el : elements1) {
                //获取章节名字和对应的连接
                String name = el.getElementsByClass("book-mid-info").select("a").attr("title");
                String zuozhe = el.getElementsByClass("author").select("a").eq(0).text();
                String href = "https:" + el.getElementsByClass("book-img-box").select("a").eq(0).attr("href");
                String zuixin = el.getElementsByClass("update").select("a").eq(0).text();
                String image = "https:" + el.getElementsByClass("book-img-box").select("img").attr("src");
                String jianjie = el.getElementsByClass("intro").text();

                Content content = new Content();
                content.setName(name.substring(0, name.length() - 8));
                content.setImg(image);
                content.setZuo_zhe(zuozhe);
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
