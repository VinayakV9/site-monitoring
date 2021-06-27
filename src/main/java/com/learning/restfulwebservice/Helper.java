package com.learning.restfulwebservice;

import com.learning.restfulwebservice.entity.WebSiteEntity;

import java.time.Duration;
import java.util.Calendar;

public class Helper {
    private Helper(){}

    public static long getDurationInMinutes(WebSiteEntity webSiteEntity){
        return Duration.between(webSiteEntity.getWebSiteStatusEntity().getLastStatusChanged().toInstant()
                , Calendar.getInstance().getTime().toInstant()).toMinutes();
    }
}
