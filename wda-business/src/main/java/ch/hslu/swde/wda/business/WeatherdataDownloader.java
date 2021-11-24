package ch.hslu.swde.wda.business;

import ch.hslu.swde.wda.business.CheckConnection.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public final class WeatherdataDownloader {
    private static final Logger Log = LogManager.getLogger(WeatherdataDownloader.class);
    private static final String BASE_URI = "http://swde.el.eee.intern:8080/weatherdata-provider/rest/weatherdata/";
    static final String CITY_URL = "http://swde.el.eee.intern:8080/weatherdata-provider/rest/weatherdata/cities";


    public String getCitiesAsXML() {
        if (!Utils.pingURL(CITY_URL, 10000)) {
            Log.error("Could not ping Server, are you connected to VPN?");
            return "";
        }
        HttpClient client = HttpClient.newHttpClient();
        String mimeType = "application/xml";

        URI uriObj = null;
        try {
            uriObj = new URI(BASE_URI + "cities/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.uri(uriObj);
        builder.header("Accept", mimeType);
        builder.GET();
        HttpRequest req = builder.build();
        HttpResponse<String> res = null;
        try {
            res = client.send(req, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (res.statusCode() == 200) {
            String messageAsJsonString = res.body();
            return messageAsJsonString;
            // do something with the retrieved message as String ...
        } else {
            // something went wrong ..
            Log.error("Http status code not 200! It's " + res.statusCode());
        }
        return "";
    }
}
