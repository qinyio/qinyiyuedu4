package com.example.qinyiyuedu4.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

import lombok.SneakyThrows;

public class HtmlTxt {

    private String Txt = "";

    //获取对应的文本的内容
    @SneakyThrows
    public String parseTxt(String keywords) {
        try {
            Document doc = Jsoup.parse(new URL(keywords), 10000);
            Txt += doc.getElementsByClass("j_chapterName").text() + "\n";
            Elements elements1 = doc.getElementsByClass("read-content j_readContent").select("p");
            for (Element el : elements1) {
                Txt += el.text() + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Txt;
    }
}