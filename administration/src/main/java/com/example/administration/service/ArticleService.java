package com.example.administration.service;

import com.example.administration.model.domain.Article;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleService {
    int createArticle(Article article);
    boolean deleteArticle(int id);
    Article getArticleById(int id);
    boolean updateArticle(Article article);
    List<Article> getArticles(String name,  String label);


}
