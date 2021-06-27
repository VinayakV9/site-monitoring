package com.learning.restfulwebservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "web_sites")
public class WebSiteEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String name;
    private String url;
    private int intervalTime;
    @Enumerated(value = EnumType.STRING)
    private IntervalType intervalType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name ="web_site_id")
    private WebSiteStatusEntity webSiteStatusEntity;
}
