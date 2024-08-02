package io.github.manbucy.ld.core;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TextSplitTest {

    /**
     * hello world my email is 12345@abc.com, and my mobile is +86-18255447811
     */
    @Test
    public void test_split_pure_text() {
        String text = "hello world! my email is 12345@abc.com, and my mobile is +86-18255447811. and i hope you can contact me.";
        byte[] separators = new byte[128];
        separators[' '] = 2;
        separators[','] = 1;
        separators['.'] = 1;
        separators['!'] = 1;
        byte[] bytes = text.getBytes();
        List<String> strList = new ArrayList<>();

        int start = 0;
        byte cur;
        for (int i = 0; i < bytes.length; i++) {
            cur = bytes[i];
            if (separators[cur] > 1) {
                if (start < i - 1) {
                    strList.add(text.substring(start, i));
                }
                start = i;
            }
        }

        log.info("strList: {}", strList);
    }
}
