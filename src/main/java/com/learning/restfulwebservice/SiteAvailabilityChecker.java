package com.learning.restfulwebservice;

import com.learning.restfulwebservice.entity.SiteStatus;
import com.learning.restfulwebservice.model.SiteAvailabilityStatus;
import org.springframework.util.StopWatch;

import java.net.HttpURLConnection;
import java.net.URL;

public class SiteAvailabilityChecker {

    public static SiteAvailabilityStatus checkSiteAvailability(String targetUrl) {
        HttpURLConnection httpUrlConn;
        try {
            httpUrlConn = (HttpURLConnection) new URL(targetUrl)
                    .openConnection();

            // A HEAD request is just like a GET request, except that it asks
            // the server to return the response headers only, and not the
            // actual resource (i.e. no message body).
            // This is useful to check characteristics of a resource without
            // actually downloading it,thus saving bandwidth. Use HEAD when
            // you don't actually need a file's contents.
            httpUrlConn.setRequestMethod("HEAD");

            // Set timeouts in milliseconds
            httpUrlConn.setConnectTimeout(30000);
            httpUrlConn.setReadTimeout(30000);
            StopWatch stopWatch = new StopWatch();

            stopWatch.start();
            // Print HTTP status code/message for your information.
            System.out.println("Response Code: "
                    + httpUrlConn.getResponseCode());
            stopWatch.stop();

            return SiteAvailabilityStatus.builder().responseTime(stopWatch.getTotalTimeSeconds())
                    .siteUrl(targetUrl)
                    .siteStatus(httpUrlConn.getResponseCode()==200? SiteStatus.UP:SiteStatus.DOWN)
                    .build();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
