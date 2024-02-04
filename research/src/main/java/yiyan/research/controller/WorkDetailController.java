package yiyan.research.controller;

//import com.chatAssistant.common.Result;
//import com.theokanning.openai.completion.CompletionRequest;
//import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yiyan.research.model.RestBean;
import yiyan.research.model.domain.ChatMsg;
import yiyan.research.model.domain.Message;
import yiyan.research.model.producer.Producer;
import yiyan.research.model.request.*;
import yiyan.research.model.request.ai.ChatRequest;
import yiyan.research.service.AiService;
import yiyan.research.service.COSService;
import yiyan.research.service.WorkDetailService;
import yiyan.research.service.WorkService;

import java.net.MalformedURLException;
import java.util.*;

@RestController
@RequestMapping("/literature")
@CrossOrigin(origins = "*")
public class WorkDetailController {
    @Resource
    private WorkDetailService workDetailService;
    @Resource
    private WorkService workService;
    @Resource
    private Producer producer;
    @Resource
    AiService aiService;

    @GetMapping("/getInfo")
    public ResponseEntity<RestBean<Map<String, Object>>>getInfo(@RequestParam String work_id,@RequestHeader(name = "token", required = false) String token) throws MalformedURLException {
        boolean isFavorite=false;
        if(token!=null){
            Map<String, Object> o=producer.syncSend(new HashMap<>(){{
                put("workId",work_id);
                put("token",token);
                put("type",4);
            }});
            isFavorite=(boolean)o.get("isFavorite");
        }

        Map<String, Object> map=workDetailService.getInfo(work_id,token);

        map.put("isFavorite",isFavorite);
        return RestBean.success(map);
    }
    /*
    生成文献引用格式
    */
    @GetMapping("/getCite")
    public ResponseEntity<RestBean<Map<String, Object>>>getCite(@RequestParam String work_id) throws MalformedURLException {

        return RestBean.success(workDetailService.getCite(work_id));
    }
    //创建评论/回复评论
    //删除评论
    //获取所有评论
    //
    //上传附件权限 删除评论
    @PostMapping("/comment")
    public ResponseEntity<RestBean<String>> createComment(@RequestBody CommentRequest comment, @RequestHeader(name = "token") String token, HttpServletRequest httpServletRequest) {
        Map<String, Object> o = producer.syncSend(new HashMap<>() {{
            put("type", 1);
            put("token", token);
        }});
        int userId = (int) o.get("id");
        boolean res = workDetailService.comment(userId, comment.getContent(), comment.getParentId(), comment.getWorkId());
        if (res)
            return RestBean.success("评论成功");
        else
            return RestBean.success("评论失败");
    }

    @PostMapping("/deleteComment")
    public ResponseEntity<RestBean<String>> deleteComment(@RequestBody DeleteCommentRequest deleteCommentRequest, @RequestHeader(name = "token") String token, HttpServletRequest httpServletRequest) {
        Map<String, Object> o = producer.syncSend(new HashMap<>() {{
            put("type", 2);
            put("token", token);
        }});
        int userId = (int) o.get("id");
        boolean isAdmin=(boolean)o.get("isAdmin");

        boolean res = workDetailService.deleteComment(userId,deleteCommentRequest.commentId,isAdmin);
        if (res)
            return RestBean.success("删除评论成功");
        else
            return RestBean.success("删除评论失败");
    }
    //能否编辑权益  是否为管理员/论文作者之一

    @GetMapping("/getPermission")
    public ResponseEntity<RestBean<Boolean>> getPermission(@RequestParam String workId,@RequestHeader(name = "token", required = false) String token){
        boolean res=false;
        if(token!=null){
            Map<String ,Object>map=producer.syncSend(new HashMap<>(){{
                put("type",7);
                put("token",token);
            }});
            String authorId= (String) map.get("authorId");
            if(authorId==null||authorId.equals(""))
                res=false;
            else
                res=workDetailService.getPermission(workId,authorId);
        }
        return RestBean.success(res);
    }
    @GetMapping("/getAllComment")
    public ResponseEntity<RestBean<List<Map<String, Object>>>> getAllComment(@RequestParam String workId, HttpServletRequest httpServletRequest){
        List<Map<String, Object>> res=workDetailService.getAllComment(workId);
        return RestBean.success(res);
    }
    @Resource
    COSService cosService;


    @PostMapping("/updateCommentPermission")
    public ResponseEntity<RestBean<String>>updateCommentPermission(@RequestBody UpdatePermissionRequest request){
        //判断author
        boolean res=workDetailService.updateCommentPermission(request.getWorkId(),request.getAuthorId());
        if(res)
            return RestBean.success("更新评论区权限成功");
        else
            return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, "关闭失败");
    }
    @GetMapping("/getCommentPermission")
    public ResponseEntity<RestBean<Map<String, Object>>>getCommentPermission(@RequestParam String workId){
        //判断author
        boolean res=workDetailService.getCommentPermission(workId);
        Map<String, Object>map=new HashMap<>(){{put("permission",res);put("res",res?"可编辑":"不可编辑");}};
        return RestBean.success(map);
    }
    /*
    上传附件

    */
    @PostMapping("/uploadAttachment")
    public ResponseEntity<RestBean<String>>uploadAttachment(@RequestBody UploadAttachmentRequest req){
        MultipartFile attachment= req.getAttachment();
        String fileName = UUID.randomUUID()+".png";
        String url=cosService.uploadFile(attachment,"work/"+fileName);
        if (url!=null) {
            // 返回成功的响应
            workDetailService.updateAttachment(url,req.getWorkId());
            return RestBean.success("上传成功");
        } else {
            // 返回失败的响应
            return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, "失败");
        }
    }

    @GetMapping("/test")
    public ResponseEntity<RestBean<Map<String,Object>>>test(@RequestParam String workId, @RequestHeader(name = "token") String token){
//        Map<String, Object> o=producer.syncSend(new HashMap<>(){{
//            put("workId",workId);
//            put("token",token);
//            put("type",4);
//        }});

//        Map<String, Object> m=producer.syncSend(new HashMap<>(){{
//            put("srcId",11);
//            put("dstId",11);
//            put("messageType",2);
////            put("content","真好看");
//            put("type",3);
//        }});
//        Map<String, Object> m2=producer.syncSend(new HashMap<>(){{
////            put("token",token);
//            put("type",2);
//        }});
//
//        Map<String, Object> m1=producer.syncSend(new HashMap<>(){{
//            put("token",token);
//            put("type",1);
//        }});

        Map<String, Object> m5=producer.syncSend(new HashMap<>(){{
            put("token",token);
            put("email","aamof@qq.com");
            put("type",5);
        }});
        return RestBean.success(m5);
    }
    /*
    关闭/打开评论区
    AI提问
    获取AI提问历史记录
     */

    //AI提问
    @PostMapping("/question")
    public ResponseEntity<RestBean<String>> aiChat(@RequestBody ChatRequest chatRequest){
        //todo:获取userId
        int userId = 1;
        String res = aiService.chat(chatRequest.getWorkId(),userId, chatRequest.getQuestion());
        return RestBean.success(res);
    }
    //获取Ai总结
    @GetMapping("/aiSummary")
    public ResponseEntity<RestBean<String>> aiSummary(@RequestParam String workId){
        String summary= aiService.getSummary(workId);
        return RestBean.success(summary);
    }
    //获取AI提问历史记录
    @GetMapping("/aiHistory")
    public ResponseEntity<RestBean<Map>> aiHistory(@RequestParam String workId){
        //todo:获取userId
        int userId = 1;
        Map map = aiService.getQuestionHistory(workId,userId);
        return RestBean.success( map);
    }

    @PostMapping("/addView")
    public ResponseEntity addView(@RequestBody WorkIdReq request){
        workService.addWorkViewCount(request.getWorkId());
        return ResponseEntity.ok(null);
    }
}

