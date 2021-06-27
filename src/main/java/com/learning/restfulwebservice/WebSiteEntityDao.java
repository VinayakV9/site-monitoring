package com.learning.restfulwebservice;

import com.learning.restfulwebservice.entity.WebSiteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebSiteEntityDao extends JpaRepository<WebSiteEntity, Integer> {

    WebSiteEntity findByName(String name);
}
