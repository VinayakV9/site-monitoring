package com.learning.restfulwebservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "web_site_status")
public class WebSiteStatusEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @Enumerated(EnumType.STRING)
    private SiteStatus status;
    private Date lastStatusChanged;
    private double avgResponseTime;
    private int hitCount;

}
