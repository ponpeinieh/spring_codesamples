package com.example.jsouptest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class App {
    public static void main(String[] args) {
        long startT = System.currentTimeMillis();
        long endT = 0;
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            webClient.getOptions().setUseInsecureSSL(true);
            final HtmlPage page = webClient.getPage("https://www.taiwanlottery.com.tw/lotto/bingobingo/drawing.aspx");
            //System.out.println("Page title:" + page.getTitleText());
            final HtmlForm form = page.getHtmlElementById("form1");
            final HtmlSelect select = page.getHtmlElementById("DropDownList2");
            select.setSelectedIndex(1);
            final HtmlSubmitInput button = page.getHtmlElementById("Button1");
            //
            endT = System.currentTimeMillis();
            System.out.println("Time spent for stage 1:" + (endT - startT) * 1.0 / 1000);
            for (int n = 0; n < 3; n++) {
                final HtmlPage page2 = button.click();
                // System.out.print(page2.asXml());
                final List<HtmlElement> trs = page2.getByXPath("//tr[td[text()=\"期別\"]]/following-sibling::tr");
                endT = System.currentTimeMillis();
                System.out.println("Time spent for stage 2:" + (endT - startT) * 1.0 / 1000);
                List<LotteryNumbers> lnList = new ArrayList<>();
                for (var tr : trs.subList(0, 2)) {
                    // System.out.print(tr.asXml());
                    // final Iterable<DomElement> tds = tr.getChildElements();
                    // tds.forEach(e->System.out.println(e.getTextContent()));
                    final List<DomText> td_text_list = tr.getByXPath(".//td/child::text()");
                    System.out.println(td_text_list);
                    if (td_text_list.size() == 5) {
                        int id = Integer.parseInt(td_text_list.get(0).getWholeText().strip());
                        String[] numstrs = td_text_list.get(1).getWholeText().split("\\s+");
                        List<Integer> nums = Arrays.stream(numstrs).map(Integer::valueOf).collect(Collectors.toList());
                        LotteryNumbers ln = new LotteryNumbers(id, nums);
                        System.out.println(ln);
                        lnList.add(ln);
                    }

                }
                endT = System.currentTimeMillis();
                System.out.println("Time spent:" + (endT - startT) * 1.0 / 1000);
            }
        } catch (FailingHttpStatusCodeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static class LotteryNumbers {
        private int id;
        private List<Integer> numbers;

        public LotteryNumbers(int id, List<Integer> numbers) {
            super();
            this.id = id;
            this.numbers = numbers;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<Integer> getNumbers() {
            return numbers;
        }

        public void setNumbers(List<Integer> numbers) {
            this.numbers = numbers;
        }

        @Override
        public String toString() {
            return "LotteryNumbers [id=" + id + ", numbers=" + numbers + "]";
        }

    }
}
