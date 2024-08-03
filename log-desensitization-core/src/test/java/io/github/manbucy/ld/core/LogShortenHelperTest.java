package io.github.manbucy.ld.core;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class LogShortenHelperTest {

    @Test
    public void test_shorten_log__notOverDefaultThreshold() {
        LogShortenHelper shortenHelper = new LogShortenHelper.Builder().build();

        String log = String.format(RandomUtil.randomString(1024 * 9 - 2) + "[%s]", RandomUtil.randomString(1024));
        String shortenLog = shortenHelper.shorten(log);
        Assertions.assertEquals(log.length(), shortenLog.length());
    }

    @Test
    public void test_shorten_log__overDefaultThreshold() {
        LogShortenHelper shortenHelper = new LogShortenHelper.Builder().build();
        String log = String.format(RandomUtil.randomString(1024 * 10) + "[%s]", RandomUtil.randomString(1024));
        String shortenLog = shortenHelper.shorten(log);
        Assertions.assertTrue(log.length() > shortenLog.length());
    }

    @Test
    public void test_shorten_log__overDefaultThreshold_but_noNeedToShorten() {
        LogShortenHelper shortenHelper = new LogShortenHelper.Builder().build();
        String log = String.format(RandomUtil.randomString(1024 * 10) + "[%s]", RandomUtil.randomString(256));
        String shortenLog = shortenHelper.shorten(log);
        Assertions.assertEquals(log.length(), shortenLog.length());
    }

    @Test
    public void test_shorten_log__customizeThreshold_and_maxAllowValueLength_and_maxRetainedValueLength_and_replacedStrOfOverflow() {
        int maxAllowValueLength = 100;
        int maxRetainedValueLength = 10;
        String replacedStrOfOverflow = "###";
        LogShortenHelper shortenHelper = new LogShortenHelper.Builder(maxAllowValueLength, maxRetainedValueLength, replacedStrOfOverflow)
                .build();

        int thresholdLength = 100;
        String log = String.format(RandomUtil.randomString(100 - 2) + "[%s]", RandomUtil.randomString(101));
        String shortenLog = shortenHelper.shorten(log, thresholdLength);

        Assertions.assertTrue(log.length() > shortenLog.length());

    }

}
