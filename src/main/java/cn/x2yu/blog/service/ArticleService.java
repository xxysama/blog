package cn.x2yu.blog.service;


import cn.x2yu.blog.dto.ArchiveArticleDto;
import cn.x2yu.blog.dto.ArticleDto;
import cn.x2yu.blog.dto.ArticleDtoWhitDate;
import cn.x2yu.blog.dto.ArticleSimpleDto;
import cn.x2yu.blog.entity.ArticleCategory;
import cn.x2yu.blog.entity.ArticleInfo;
import cn.x2yu.blog.entity.ArticlePicture;

import java.util.List;

/**
 * 文章Service
 * 说明：ArticleInfo里面封装了picture/content/category等信息
 */

public interface ArticleService {

    void addAticle(ArticleInfo articleInfo);

    void deleteArticle(Long articleId);

    void deleteArticleCategory(Long articleId);

    void updateArticleInfo(ArticleInfo articleInfo);

    void updateArticleCategory(ArticleCategory articleCategory);

    void addArticleCategory(ArticleCategory articleCategory);

    void updateArticlePic(Long articleId);

    void addArticlePic(Long articleId);

    void deleteArticlePic(Long articleId);

    String getOneById(Long id);

    Long getArtilceIdByName(String title);

    ArticlePicture getPictureByArticleId(Long id);

    List<ArticleDto>listAll();

    List<ArticleDtoWhitDate> listArticlesWithDate();

    List<ArticleDto>listArticleByCategory(Long categoryId);

    List<ArchiveArticleDto>listArchiveArticle();

    List<ArticleSimpleDto>listArticleSimple();
}
