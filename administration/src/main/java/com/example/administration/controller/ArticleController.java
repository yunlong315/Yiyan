package com.example.administration.controller;

import com.example.administration.model.domain.Article;
import com.example.administration.model.micro.CheckAdminReply;
import com.example.administration.model.shared.RestBean;
import com.example.administration.model.request.article.CreateArticleRequest;
import com.example.administration.model.request.article.UpdateArticalRequest;
import com.example.administration.service.ArticleService;
import com.example.administration.service.micro.Producer;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;
    @Resource
    private Producer producer;

    //管理员创建专栏
    @PostMapping()
    public ResponseEntity<RestBean> createArticle(@RequestBody CreateArticleRequest createArticleRequest,@RequestHeader("token") String token) {
        if (token == null || token.equals("")){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        CheckAdminReply checkAdminReply = producer.checkAdmin(token);
        if (!checkAdminReply.isAdmin()){
            return RestBean.failure(HttpStatusCode.valueOf(443),"权限不足");
        }

        Article article = new Article(
                createArticleRequest.getName(),
                checkAdminReply.getId(),
                createArticleRequest.getLabel(),
                createArticleRequest.getConcepts(),
                createArticleRequest.getText(),
                createArticleRequest.getImageUrl(),
                createArticleRequest.getRelateWorkIds(),
                LocalDateTime.now()
                );
        articleService.createArticle(article);
        return RestBean.success(true);
    }
    //管理员删除专栏
    @DeleteMapping()
    public ResponseEntity<RestBean> deleteArticle(@RequestParam int id,@RequestHeader("token") String token){
        if (token == null || token.equals("")){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        //获取管理员id并鉴权
        CheckAdminReply checkAdminReply = producer.checkAdmin(token);
        if (!checkAdminReply.isAdmin()){
            return RestBean.failure(HttpStatusCode.valueOf(443),"权限不足");
        }
        articleService.deleteArticle(id);
        return RestBean.success("删除成功");
    }
    //管理员更新专栏
    @PutMapping()
    public ResponseEntity<RestBean> updateArticle(@RequestBody UpdateArticalRequest updateArticalRequest,@RequestHeader("token") String token){
        if (token == null || token.equals("")){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        //获取管理员id并鉴权
        CheckAdminReply checkAdminReply = producer.checkAdmin(token);
        if (!checkAdminReply.isAdmin()){
            return RestBean.failure(HttpStatusCode.valueOf(443),"权限不足");
        }
        Article article = new Article(updateArticalRequest.getId(),
                updateArticalRequest.getName(),
                updateArticalRequest.getLabel(),
                updateArticalRequest.getConcepts(),
                updateArticalRequest.getText(),
                updateArticalRequest.getImageUrl(),
                updateArticalRequest.getRelateWorkIds(),
                LocalDateTime.now());
        boolean res = articleService.updateArticle(article);
        return RestBean.success(res);
    }

    //用户获取专栏列表，可通过name和label筛选
    @GetMapping()
    public ResponseEntity<RestBean> getArticles(@RequestParam(required = false) String name,
                                                @RequestParam(required = false) String label){
        return RestBean.success(articleService.getArticles(name,label));
    }

    //用户查看专栏详情
    @GetMapping("/details")
    public ResponseEntity<RestBean> getArticle(@RequestParam int id){
        Article article = articleService.getArticleById(id);
        return RestBean.success(article);
    }
}
