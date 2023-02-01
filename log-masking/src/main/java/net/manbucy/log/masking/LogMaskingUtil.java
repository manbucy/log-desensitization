package net.manbucy.log.masking;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogMaskingUtil {
    private static final List<Pattern> maskingPatterList = new ArrayList<>();
    static {
        // email
        maskingPatterList.add(Pattern.compile("\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}"));

        // China mobile
        maskingPatterList.add(Pattern.compile("0?(13|14|15|17|18|19)[0-9]{9}"));

        // China ID number
        maskingPatterList.add(Pattern.compile("\\d{17}[\\d|x]|\\d{15}"));
    }

    public static String masking(String message) {
        for (Pattern pattern : maskingPatterList) {
            Matcher matcher = pattern.matcher(message);
            message = matcher.replaceAll("***");
        }
        return message;
    }
}
