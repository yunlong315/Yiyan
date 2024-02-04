package com.example.administration.service.serviceImp;

import com.example.administration.mapper.ArticleMapper;
import com.example.administration.model.domain.Article;
import com.example.administration.service.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleServiceImp implements ArticleService {
    @Resource
    ArticleMapper articleMapper;

    @Transactional
    @Override
    public int createArticle(Article article ) {
        int id = articleMapper.insert(article);
        if (article.getRelateWorkIds() != null){
            articleMapper.relateToWorks(article.getId(),article.getRelateWorkIds());
        }
        return id;
    }

    @Override
    public boolean deleteArticle(int id) {
        return articleMapper.deleteById(id);
    }

    @Override
    public Article getArticleById(int id) {
        Article article = articleMapper.findById(id);
        article.setRelateWorkIds(articleMapper.getRelations(id));
        return article;
    }

    @Transactional
    @Override
    public boolean updateArticle(Article article) {
        if (article.getRelateWorkIds() !=null){
            articleMapper.deleteOldRelations(article.getId());
            articleMapper.relateToWorks(article.getId(),article.getRelateWorkIds());
        }
        return articleMapper.update(article);
    }

    @Override
    public List<Article> getArticles(String name, String label) {
        return articleMapper.getArticles(name,label);
    }

}