package yiyan.research.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonHandler {
    /***
     * 对于形如["abc","def","ghi"]的json数据，使用此方法将其转换为String列表
     * @param json
     * @return
     */
    public static List<String> parse(String json){
        List<String> resultList = new ArrayList<>();

        // 使用正则表达式匹配字符串中的所有引号括起的部分
        Pattern pattern = Pattern.compile("\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(json);

        // 循环匹配结果，并将匹配到的内容添加到结果列表中
        while (matcher.find()) {
            resultList.add(matcher.group(1));
        }

        return resultList;
    }
}
