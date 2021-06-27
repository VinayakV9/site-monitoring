package com.learning.restfulwebservice.model;

import com.learning.restfulwebservice.entity.SiteStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WebSiteStatusResponse {
    private boolean isNamedSiteAvailable;
    private SiteStatus siteStatus;
    private long statusTimeInMinutes;
    private double avgResponseTime;
}
