package com.xinder.article.service;

import com.xinder.article.service.impl.ArticleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by sang on 2017/12/25.
 */
@Component
public class DataStatisticsComponent {
    @Autowired
    ArticleServiceImpl articleServiceImpl;

    //每天执行一次，统计PV
    @Scheduled(cron = "1 0 0 * * ?")
    public void pvStatisticsPerDay() {
        articleServiceImpl.pvStatisticsPerDay();
    }
}
