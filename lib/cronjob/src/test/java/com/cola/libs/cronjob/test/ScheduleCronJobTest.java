package com.cola.libs.cronjob.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jiachen.shi on 6/19/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class ScheduleCronJobTest {

    private static Logger logger = LoggerFactory.getLogger(ScheduleCronJobTest.class);

    @Test
    public void init(){
        logger.info("start");
    }

}
