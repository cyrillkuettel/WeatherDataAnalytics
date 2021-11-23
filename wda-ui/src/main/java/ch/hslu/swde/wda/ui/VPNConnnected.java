package ch.hslu.swde.wda.ui;

public class VPNConnnected {
    static final String URL = "http://swde.el.eee.intern:8080/weatherdata-provider/";
    static final String testURL = "http://swde.el.eee.intern:8080/weatherdata-provider/rest/weatherdata/cities";

    public static void main(String[] args) {
        System.out.println(Utils.pingURL(testURL, 100000));
    }
}
