package com.example.qinyiyuedu4.html;
;
import com.example.qinyiyuedu4.pojo.Content_zj;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import lombok.SneakyThrows;

public class Html_ZhangJi {

    private ArrayList<Content_zj> Data_ZJ;

    //获取书籍章节目录，并存到临时表中
    @SneakyThrows
    public ArrayList<Content_zj> parseYueDu(String keywords) {
        try {
            Data_ZJ=new ArrayList<>();
            Document doc = Jsoup.parse(new URL(keywords), 10000);
            Elements elements1 = doc.getElementsByClass("volume").select("li");
            for (Element el : elements1) {
                String ZJname = el.getElementsByClass("book_name").select("a").text();
                String ZJhref = "https:" + el.getElementsByClass("book_name").select("a").attr("href");

                Content_zj content=new Content_zj();
                content.setName_zj(ZJname);
                content.setHref_zj(ZJhref);
                Data_ZJ.add(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Data_ZJ;
    }
}