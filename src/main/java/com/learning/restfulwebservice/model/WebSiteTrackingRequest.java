package com.learning.restfulwebservice.model;

import com.learning.restfulwebservice.entity.IntervalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebSiteTrackingRequest {

    private String name;
    private String url;
    private int interval;
    private IntervalType intervalType;
}
