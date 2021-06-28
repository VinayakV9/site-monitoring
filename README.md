# Xebia XUP

Monitoring tool to track uptime of any website. 

# Getting Started

## Feature
* Get status of site - whether site is up/down. For how long site is up/down and average response time.
* Add site to track - user can specify site url and interval period for tracking that site.
* Remove site from tracking.

### Start application
execute following cmd from maven
[ <code> mvn spring-boot:run </code> ]

## Controller endpoints
* Get status: [GET] /site/{sitename}/status
* Add site to track: [POST] /site/{sitename}  body:WebSiteTrackingRequest
    e.g. body: {
                   "name":"google",
                   "url": "http://www.google.com/",
                   "interval":5,
                   "intervalType":"SECONDS"
               }
* Stop tracking site: [DELETE] /site/{sitename}


## Entities
* WebSiteEntity
* WebSiteStatusEntity

DB: in-memory H2 DB

## Working
* User will add site for start tracking that site.
* Then system will start ScheduledTask. we are using ThreadPoolTaskScheduler. Inside task we are hitting given site.
Based on response status and time we will update WebSiteStatusEntity.
* We are runing thread pool with pool size 10.
