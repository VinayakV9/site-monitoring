package com.learning.restfulwebservice;

import com.learning.restfulwebservice.entity.WebSiteEntity;
import com.learning.restfulwebservice.entity.WebSiteStatusEntity;
import com.learning.restfulwebservice.model.SiteAvailabilityStatus;
import org.springframework.stereotype.Component;

import java.util.Calendar;

import static java.util.Objects.isNull;

@Component
public class SiteProcessor {

    public void process(WebSiteEntity webSiteEntity){
        SiteAvailabilityStatus siteAvailabilityStatus = SiteAvailabilityChecker.checkSiteAvailability(webSiteEntity.getUrl());

        if( isNull(siteAvailabilityStatus) ){
            return;
        }

        WebSiteStatusEntity webSiteStatusEntity = webSiteEntity.getWebSiteStatusEntity();
        if(isNull(webSiteStatusEntity)){
            webSiteStatusEntity = new WebSiteStatusEntity();
        }

        double totalResponseTime = webSiteStatusEntity.getAvgResponseTime() * webSiteStatusEntity.getHitCount();
        totalResponseTime += siteAvailabilityStatus.getResponseTime();
        int totalHits = webSiteStatusEntity.getHitCount() + 1;

        double avgResponseTime = totalResponseTime/totalHits;

        webSiteStatusEntity.setAvgResponseTime(avgResponseTime);
        webSiteStatusEntity.setHitCount(totalHits);
        if( webSiteStatusEntity.getStatus() != siteAvailabilityStatus.getSiteStatus()) {
            webSiteStatusEntity.setLastStatusChanged(Calendar.getInstance().getTime());
            webSiteStatusEntity.setStatus(siteAvailabilityStatus.getSiteStatus());
        }
    }
}
