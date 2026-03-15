package org.xiaoyu.hello.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xiaoyu.micro.annotation.Autowired;
import org.xiaoyu.micro.annotation.Component;

@Component
public class DbInitializer {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    @PostConstruct
    public void init() {
        logger.info("Initializing DB...");
        userService.initDb();
    }
}
