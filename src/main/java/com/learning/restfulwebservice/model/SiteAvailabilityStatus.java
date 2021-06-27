package com.learning.restfulwebservice.model;

import com.learning.restfulwebservice.entity.SiteStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SiteAvailabilityStatus {
    private String siteUrl;
    private SiteStatus siteStatus;
    private double responseTime;
}
