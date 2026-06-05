package com.xc.basic.task;

import com.xc.api.basic.dto.DeletedUserDto;
import com.xc.basic.config.Constants;
import com.xc.basic.config.StatisticsAdaptorImpl;
import com.xc.basic.service.BasicService;
import com.xc.basic.service.DeletedUserService;
import com.xc.basic.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 系统任务
 * </p>
 *
 * @author xc
 * @since 2026-05-13
 */
@Slf4j
@Component
public class BasicTask {

    @Autowired
    private StatisticsAdaptorImpl statisticsAdaptorImpl;
    @Autowired
    private BasicService basicService;

    /**
     * 每1个小时执行一次
     */
//    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void oneHourHandle() {
        log.info("【每1小时】开始执行！");
        basicService.logoutHandle();
        // 日志同步
        statisticsAdaptorImpl.init();
        log.info("【每1小时】开始执行！");
    }
}