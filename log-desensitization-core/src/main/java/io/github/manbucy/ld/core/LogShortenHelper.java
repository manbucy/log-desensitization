package io.github.manbucy.ld.core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LogShortenHelper {

    private static final String DEFAULT_SHORTEN_LOG_REGEX_TEMPLATE = "(\\%s)([^\\%s\\n\\:]{%d})([^\\%s\\n\\:]{%d,})(\\%s)";


    private static final int DEFAULT_MAX_ALLOW_VALUE_LENGTH = 256;

    private static final int DEFAULT_MAX_RETAINED_VALUE_LENGTH = 10;

    private static final char[][] DEFAULT_VALUE_WRAP_CHAR_ARR = new char[][]{
            {'"', '"'}, {'[', ']'}, {'{', '}'}, {'<', '>'}, {'\'', '\''}, {'(', ')'}
    };

    private static final int DEFAULT_SHORTEN_LOG_LENGTH_THRESHOLD = 1024 * 10;

    private static final String DEFAULT_REPLACED_STR_OF_OVERFLOW = "...";

    private final Pattern regex;

    private final String replacement;

    private LogShortenHelper(Pattern regex, String replacement) {
        this.regex = regex;
        this.replacement = replacement;
    }

    public String shorten(String fullString, int thresholdLength) {
        if (fullString.length() <= thresholdLength) {
            return fullString;
        }

        return regex.matcher(fullString).replaceAll(replacement);
    }

    public String shorten(String fullString) {
        return shorten(fullString, DEFAULT_SHORTEN_LOG_LENGTH_THRESHOLD);
    }


    static class Builder {

        private final int maxAllowValueLength;

        private final int maxRetainedValueLength;

        private final String replacedStrOfOverflow;

        private final List<List<Character>> valueWrapCharsList = new ArrayList<>();

        public Builder() {
            this.maxAllowValueLength = DEFAULT_MAX_ALLOW_VALUE_LENGTH;
            this.maxRetainedValueLength = DEFAULT_MAX_RETAINED_VALUE_LENGTH;
            this.replacedStrOfOverflow = DEFAULT_REPLACED_STR_OF_OVERFLOW;
        }

        public Builder(int maxAllowValueLength, int maxRetainedValueLength) {
            this.maxAllowValueLength = maxAllowValueLength;
            this.maxRetainedValueLength = maxRetainedValueLength;
            this.replacedStrOfOverflow = DEFAULT_REPLACED_STR_OF_OVERFLOW;
        }


        public Builder(int maxAllowValueLength, int maxRetainedValueLength, String replacedStrOfOverflow) {
            this.maxAllowValueLength = maxAllowValueLength;
            this.maxRetainedValueLength = maxRetainedValueLength;
            this.replacedStrOfOverflow = replacedStrOfOverflow;
        }

        public Builder addWrapChars(Character start, Character end) {
            List<Character> wrapChars = new ArrayList<>();
            wrapChars.add(start);
            wrapChars.add(end);
            valueWrapCharsList.add(wrapChars);
            return this;
        }

        private char[][] getWrapsChars() {
            if (valueWrapCharsList.isEmpty()) {
                return DEFAULT_VALUE_WRAP_CHAR_ARR;
            }
            char[][] wrapCharsArr = new char[valueWrapCharsList.size()][2];
            for (int i = 0; i < valueWrapCharsList.size(); i++) {
                wrapCharsArr[i] = new char[]{valueWrapCharsList.get(i).get(0), valueWrapCharsList.get(i).get(1)};
            }

            return wrapCharsArr;
        }

        public LogShortenHelper build() {
            if (maxRetainedValueLength <= 0 || maxAllowValueLength <= 0 ||
                    maxAllowValueLength < maxRetainedValueLength) {
                throw new IllegalArgumentException(String.format("The maxAllowValueLength=%s and maxRetainedValueLength=%s could be invalid.",
                        maxAllowValueLength, maxRetainedValueLength));
            }

            char[][] wrapCharsArr = this.getWrapsChars();

            StringBuilder regex = new StringBuilder();
            StringBuilder group1 = new StringBuilder();
            StringBuilder group2 = new StringBuilder();
            StringBuilder group4 = new StringBuilder();
            for (int i = 0; i < wrapCharsArr.length; i++) {
                char startChar = wrapCharsArr[i][0];
                char endChar = wrapCharsArr[i][1];

                regex.append(String.format(DEFAULT_SHORTEN_LOG_REGEX_TEMPLATE, startChar, endChar, maxRetainedValueLength,
                        endChar, (maxAllowValueLength - maxRetainedValueLength + 1), endChar));

                if (i < wrapCharsArr.length - 1) {
                    regex.append("|");
                }
                group1.append("$").append(1 + (i * 4));
                group2.append("$").append(2 + (i * 4));
                group4.append("$").append(4 + (i * 4));
            }

            Pattern pattern = Pattern.compile(regex.toString());
            String replacement = group1.toString() + group2.toString() + replacedStrOfOverflow + group4.toString();


            return new LogShortenHelper(pattern, replacement);
        }

    }

}
