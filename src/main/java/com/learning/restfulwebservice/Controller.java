package com.learning.restfulwebservice;

import com.learning.restfulwebservice.entity.WebSiteEntity;
import com.learning.restfulwebservice.entity.WebSiteStatusEntity;
import com.learning.restfulwebservice.model.WebSiteStatusResponse;
import com.learning.restfulwebservice.model.WebSiteTrackingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

import static java.util.Objects.nonNull;

@RestController
public class Controller {

    @Autowired
    private WebSiteEntityDao webSiteEntityDao;

    @Autowired
    private ScheduledConfiguration scheduledConfiguration;

    @RequestMapping(method = RequestMethod.GET, path = "/site/{name}/status")
    public ResponseEntity<WebSiteStatusResponse> getStatus(@PathVariable("name") String siteName){
        WebSiteEntity webSiteEntity = webSiteEntityDao.findByName(siteName);
        WebSiteStatusResponse webStatusResponse = null;
        if( nonNull(webSiteEntity) ){
            webStatusResponse = WebSiteStatusResponse.builder().isNamedSiteAvailable(true)
                    .avgResponseTime(webSiteEntity.getWebSiteStatusEntity().getAvgResponseTime())
                    .siteStatus(webSiteEntity.getWebSiteStatusEntity().getStatus())
                    .statusTimeInMinutes(Helper.getDurationInMinutes(webSiteEntity))
                    .build();
        } else {
            webStatusResponse = WebSiteStatusResponse.builder().isNamedSiteAvailable(false).build();
        }

        return new ResponseEntity<>(webStatusResponse, HttpStatus.OK);
    }

    @PostMapping(path="/site/{name}")
    @Transactional
    public void updateSiteTracking(@PathVariable("name") String siteName,
                                @RequestBody WebSiteTrackingRequest request){
        WebSiteEntity siteEntity = webSiteEntityDao.findByName(siteName);

        if( siteEntity == null ){
            siteEntity = new WebSiteEntity();
            siteEntity.setWebSiteStatusEntity(new WebSiteStatusEntity());
        }
        siteEntity.setIntervalTime(request.getInterval());
        siteEntity.setIntervalType(request.getIntervalType());
        siteEntity.setName(request.getName());
        siteEntity.setUrl(request.getUrl());

        webSiteEntityDao.saveAndFlush(siteEntity);
        scheduledConfiguration.job(siteEntity);
    }

    @DeleteMapping("/site/{name}")
    public void delete(@PathVariable("name") String siteName){
        scheduledConfiguration.removeJob(siteName);
    }
}
