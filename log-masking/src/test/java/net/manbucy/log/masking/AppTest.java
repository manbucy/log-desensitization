package net.manbucy.log.masking;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppTest {
    private static final Logger log = LoggerFactory.getLogger(AppTest.class);

    @Test
    public void test_app() {
        log.info(LogMaskingUtil.masking("email: 123@test.com, China Mobile: 18212345678, China ID number: 110110199001011010"));
    }
}
