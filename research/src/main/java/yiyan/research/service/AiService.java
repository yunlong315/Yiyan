package yiyan.research.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import yiyan.research.config.WenXinConfig;
import yiyan.research.mapper.AisummaryMapper;
import yiyan.research.mapper.QuestionHistoryMapper;
import yiyan.research.model.domain.AiSummary;
import yiyan.research.model.domain.QuestionHistory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AiService {
    @Resource
    private WenXinConfig wenXinConfig;
    @Resource
    AisummaryMapper aisummaryMapper;
    @Resource
    QuestionHistoryMapper questionHistoryMapper;

    public String getSummary(String workId) {
        AiSummary aiSummary = aisummaryMapper.getSummary(workId);
        if (aiSummary == null) {
            String title = "用友:“三年计划”只争朝夕";
            String ab = "很少有企业像用友这样以三年为一个周期更新自己的战略计划。走过18个岁月的用友用“新三年规划”完成了属于自己的成人礼,王文京似乎在以这种方式提醒自己和企业同仁:IT行业的生命周期短暂,他们总是面临着紧迫的变革考验";
            String question = String.format("阅读论文题目和摘要写一段总结：题目:%s 摘要:%s\n", title, ab);
            String summary = askAiForSummary(question);
            aiSummary = new AiSummary(workId,summary);
            aisummaryMapper.addSummary(new AiSummary(workId,summary));
        }
        return aiSummary.getSummary();
    }


    public Map<String,List> getQuestionHistory(String workId, int userId) {
        QuestionHistory questionHistory= questionHistoryMapper.getQuestionHistory(workId, userId);
        return Map.of("messages",getMessages(questionHistory.getHistory()));
    }
    @Async
    void updateQuestionHistory(QuestionHistory questionHistory) {
        questionHistoryMapper.updateQuestionHistory(questionHistory);
    }
    @Async
    void insertQuestionHistory(QuestionHistory questionHistory){
        questionHistoryMapper.insertQuestionHistory(questionHistory);
    }

    //单问题
    private String askAiForSummary(String question) {
        if (question == null || question.equals("")) {
            return "请输入问题";
        }
        List<Map<String, String>> messages = new ArrayList<>();
        String res = null;
        //先获取令牌然后才能访问api
        if (wenXinConfig.flushAccessToken() != null) {
            messages.add(Map.of("role", "user",
                    "content", question));
            res = sendRequestToAI(messages);
        }
        return res;
    }



    private List<Map<String, String>> getMessages(String history){
        JSONObject jsonHistory = JSONObject.parseObject(history);
        JSONArray jsonArray = jsonHistory.getJSONArray("messages");
        List<Map<String, String>> messages = new ArrayList<>();
        if (!jsonArray.isEmpty()){
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                messages.add(
                        Map.of(
                                "role", item.getString("role"),
                                "content", item.getString("content")
                        )
                );
            }
        }
        return messages;
    }
    public String chat(String workId, int userId, String question) {
        QuestionHistory questionHistory = questionHistoryMapper.getQuestionHistory(workId, userId);
        //获取旧message
        List<Map<String, String>> messages = new ArrayList<>();
        if (questionHistory != null){
            String history = questionHistory.getHistory();
            messages.addAll(getMessages(history));
        }

        //加入新问题
        Map<String, String> user = Map.of(
                "role", "user",
                "content", question
        );
        messages.add(user);

        if (wenXinConfig.flushAccessToken() != null) {
            //获取回答
            String res = sendRequestToAI(messages);
            messages.add(
                    Map.of(
                            "role","assistant",
                            "content",res
                    )
            );

            //保存
            Map<String, Object> messagesMap = new HashMap<>();
            messagesMap.put("messages", messages);
            String record = JSON.toJSONString(messagesMap);

            if (questionHistory == null){
                QuestionHistory newQuestionHistory = new QuestionHistory(workId,userId,record);
                insertQuestionHistory(newQuestionHistory);
            }else{
                questionHistory.setHistory(record);
                updateQuestionHistory(questionHistory);
            }
            return res;
        }
        return null;
    }

    private String sendRequestToAI(List<Map<String, String>> messages) {
        String requestJson = constructRequestJson(2, 0.95, 0.8, 1.0, false, messages);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestJson);
        Request request = new Request.Builder()
                .url(wenXinConfig.ERNIE_Bot_4_0_URL + "?access_token=" + wenXinConfig.flushAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        log.debug(request.toString());
        OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
        try {
            String responseJson = HTTP_CLIENT.newCall(request).execute().body().string();
            JSONObject responseObject = JSON.parseObject(responseJson);
            return responseObject.getString("result");
        } catch (IOException e) {
            log.error("网络有问题 "+ e.getMessage());
            return "网络有问题，请稍后重试";
        }
    }


    /**
     * 构造请求的请求参数
     *
     * @param userId
     * @param temperature
     * @param topP
     * @param penaltyScore
     * @param messages
     * @return
     */
    private String constructRequestJson(Integer userId,
                                        Double temperature,
                                        Double topP,
                                        Double penaltyScore,
                                        boolean stream,
                                        List<Map<String, String>> messages) {
        Map<String, Object> request = new HashMap<>();
        request.put("user_id", userId.toString());
        request.put("temperature", temperature);
        request.put("top_p", topP);
        request.put("penalty_score", penaltyScore);
        request.put("stream", stream);
        request.put("messages", messages);
        System.out.println(JSON.toJSONString(request));
        return JSON.toJSONString(request);
    }
}
