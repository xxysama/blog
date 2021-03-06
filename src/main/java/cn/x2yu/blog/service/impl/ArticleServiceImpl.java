package cn.x2yu.blog.service.impl;

import cn.x2yu.blog.dao.ArticleCategoryMapper;
import cn.x2yu.blog.dao.ArticleInfoMapper;
import cn.x2yu.blog.dao.ArticlePictureMapper;
import cn.x2yu.blog.dao.CategoryInfoMapper;
import cn.x2yu.blog.dto.ArchiveArticleDto;
import cn.x2yu.blog.dto.ArticleDto;
import cn.x2yu.blog.dto.ArticleDtoWhitDate;
import cn.x2yu.blog.dto.ArticleSimpleDto;
import cn.x2yu.blog.entity.*;
import cn.x2yu.blog.service.ArticleService;
import cn.x2yu.blog.util.FormatFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文章Service实现类
 * 说明：ArticleInfo里面封装了picture/content/category等信息
 *
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleInfoMapper articleInfoMapper;
    @Autowired
    ArticlePictureMapper articlePictureMapper;
    @Autowired
    ArticleCategoryMapper articleCategoryMapper;
    @Autowired
    CategoryInfoMapper categoryInfoMapper;
    @Autowired
    FormatFile formatFile;

    public ArticleServiceImpl(){

    }


    @Override
    public void addAticle(ArticleInfo articleInfo) {
        articleInfoMapper.insertSelective(articleInfo);
    }

    @Override
    public Long getArtilceIdByName(String title) {
        ArticleInfo articleInfo = articleInfoMapper.selectByArticleName(title);
        return articleInfo.getId();
    }

    @Override
    public void deleteArticle(Long articleId) {
        articleInfoMapper.deleteByPrimaryKey(articleId);
    }

    @Override
    public void deleteArticleCategory(Long articleId) {

        articleCategoryMapper.deleteByArticleId(articleId);
    }

    @Override
    public void updateArticleInfo(ArticleInfo articleInfo) {

        articleInfoMapper.updateByPrimaryKeySelective(articleInfo);
    }

    @Override
    public void updateArticleCategory(ArticleCategory articleCategory) {

        articleCategoryMapper.updateByArticleIdSelective(articleCategory);

    }

    @Override
    public void addArticleCategory(ArticleCategory articleCategory) {
        articleCategoryMapper.insertSelective(articleCategory);
    }

    @Override
    public void updateArticlePic(Long articleId) {

        //数据库中题图修改
        ArticlePicture articlePicture = new ArticlePicture();
        String pitctureUrl = ("images/masonary-post/"+articleId+".jpg");
        articlePicture.setArticle_id(articleId);
        articlePicture.setPicture_url(pitctureUrl);

        articlePictureMapper.updateByArticleIdSelective(articlePicture);
    }

    @Override
    public void addArticlePic(Long articleId) {
        //数据库中题图储存
        ArticlePicture articlePicture = new ArticlePicture();
        String pitctureUrl = ("images/masonary-post/"+articleId+".jpg");
        articlePicture.setArticle_id(articleId);
        articlePicture.setPicture_url(pitctureUrl);

        articlePictureMapper.insertSelective(articlePicture);

    }

    @Override
    public void deleteArticlePic(Long articleId) {
        articlePictureMapper.deleteByArticleId(articleId);
    }

    @Override
    public String getOneById(Long id) {
        //获取文章的标题
        ArticleInfo articleInfo = articleInfoMapper.selectByPrimaryKey(id);
        System.out.println(articleInfo.getTitle());
        return articleInfo.getTitle();
    }

    @Override
    public ArticlePicture getPictureByArticleId(Long id) {
        return null;
    }


    /**
     * 获取首页展示文章卡片内容集合
     * 从tbl_article_picture/tbl_article_info/tbl_article_category表中获取内容
     * */
    @Override
    public List<ArticleDto> listAll() {

        List<ArticleDto> listArticleDto = new ArrayList<>();

        //查询tbl_article_info
        ArticleInfoExample articleInfoExample = new ArticleInfoExample();
        articleInfoExample.setOrderByClause("id desc");
        // 无添加查询即返回所有
        List<ArticleInfo> listArticleInfo = articleInfoMapper.selectByExample(articleInfoExample);

        for(int i =0;i<listArticleInfo.size();i++){
            ArticleDto articleDto = new ArticleDto();
            Long articleId = listArticleInfo.get(i).getId();
            String title = listArticleInfo.get(i).getTitle();
            title = formatFile.formatArticleTitle(title);
            String summary = listArticleInfo.get(i).getSummary();


            articleDto.setId(articleId);
            articleDto.setTitle(title);
            articleDto.setSummary(summary);

            //先根据文章id获取文章和分类的关系表数据，在根据category_id查询分类名称
            ArticleCategory articleCategory = articleCategoryMapper.selectByArticleId(articleId);
            Long aticleCategoryId = articleCategory.getCategory_id();

            String category = categoryInfoMapper.selectByPrimaryKey(aticleCategoryId).getName();
            articleDto.setCategory(category);

            //获取图片url
            String pictureUrl = articlePictureMapper.selectByArticleId(articleId).getPicture_url();
            articleDto.setPictureUrl(pictureUrl);

            listArticleDto.add(articleDto);
        }

        return listArticleDto;
    }

    /**
     *
     *获取带有时间戳的文章对象
     * */
    @Override
    public List<ArticleDtoWhitDate> listArticlesWithDate() {
        List<ArticleDtoWhitDate> articleDtoWhitDates = new ArrayList<>();

        //查询tbl_article_info
        ArticleInfoExample articleInfoExample = new ArticleInfoExample();
        articleInfoExample.setOrderByClause("id desc");
        // 无添加查询即返回所有
        List<ArticleInfo> listArticleInfo = articleInfoMapper.selectByExample(articleInfoExample);

        for(int i =0;i<listArticleInfo.size();i++){
            ArticleDtoWhitDate articleDtoWhitDate = new ArticleDtoWhitDate();
            Long articleId = listArticleInfo.get(i).getId();
            String title = listArticleInfo.get(i).getTitle();
            title = formatFile.formatArticleTitle(title);
            String summary = listArticleInfo.get(i).getSummary();
            Date create_by = listArticleInfo.get(i).getCreate_by();

            articleDtoWhitDate.setId(articleId);
            articleDtoWhitDate.setTitle(title);
            articleDtoWhitDate.setSummary(summary);
            articleDtoWhitDate.setCreate_by(create_by);

            //先根据文章id获取文章和分类的关系表数据，在根据category_id查询分类名称
            ArticleCategory articleCategory = articleCategoryMapper.selectByArticleId(articleId);
            Long aticleCategoryId = articleCategory.getCategory_id();

            String category = categoryInfoMapper.selectByPrimaryKey(aticleCategoryId).getName();
            articleDtoWhitDate.setCategory(category);

            //获取图片url
            String pictureUrl = articlePictureMapper.selectByArticleId(articleId).getPicture_url();
            articleDtoWhitDate.setPictureUrl(pictureUrl);

            articleDtoWhitDates.add(articleDtoWhitDate);
        }

        return articleDtoWhitDates;
    }

    /**
     * 根据分类名称（category）查询该分类下的文章
     * */
    @Override
    public List<ArticleDto> listArticleByCategory(Long categoryId) {

        List<ArticleDto>articleDtosByCategory = new ArrayList<>();

        //根据category_id 查询和article关系数据集合
        ArticleCategoryExample articleCategoryExample = new ArticleCategoryExample();
        articleCategoryExample.setOrderByClause("id desc");
        articleCategoryExample.createCriteria().andCategory_idEqualTo(categoryId);

        List<ArticleCategory> articleCategories = articleCategoryMapper.selectByExample(articleCategoryExample);

        if(articleCategories.size()==0){
            return null;
        }else {
        //获取分类id
        Long category_id = articleCategories.get(0).getCategory_id();

        //封装返回的数据
        for(int i=0;i<articleCategories.size();i++){
            ArticleDto articleDto = new ArticleDto();
            Long article_id = articleCategories.get(i).getArticle_id();
            String title = articleInfoMapper.selectByPrimaryKey(article_id).getTitle();
            title = formatFile.formatArticleTitle(title);
            String summary = articleInfoMapper.selectByPrimaryKey(article_id).getSummary();

            articleDto.setId(article_id);
            articleDto.setTitle(title);
            articleDto.setSummary(summary);

            //获取分类名称
            String category = categoryInfoMapper.selectByPrimaryKey(category_id).getName();
            articleDto.setCategory(category);

            //获取图片url
            String pictureUrl = articlePictureMapper.selectByArticleId(article_id).getPicture_url();
            articleDto.setPictureUrl(pictureUrl);

            articleDtosByCategory.add(articleDto);
        }

            }

        return articleDtosByCategory;
    }

    /**
     * 查询用于展示最新文章的简单文章数据体
     * */
    @Override
    public List<ArticleSimpleDto> listArticleSimple() {

        List<ArticleSimpleDto>articleSimpleDtos = new ArrayList<>();

        List<ArticleInfo> articleInfos = new ArrayList<>();
        ArticleInfoExample articleInfoExample = new ArticleInfoExample();
        articleInfoExample.setOrderByClause("create_by desc");
        articleInfos = articleInfoMapper.selectByExample(articleInfoExample);

        if(articleInfos.size()==0){
            return null;
        }else {
            //限制查询三条数据，避免越界访问
            for(int i = 0;i<articleInfos.size() && i<3;i++){
                Long articleId = articleInfos.get(i).getId();
                String title = articleInfos.get(i).getTitle();
                title = formatFile.formatArticleTitle(title);
                Date create_by = articleInfos.get(i).getCreate_by();
                String newDate = formatFile.formatDate(create_by);

                String pictureUrl = articlePictureMapper.selectByArticleId(articleId).getPicture_url();

                ArticleSimpleDto articleSimpleDto = new ArticleSimpleDto();
                articleSimpleDto.setId(articleId);
                articleSimpleDto.setTitle(title);
                articleSimpleDto.setCreate_by(newDate);
                articleSimpleDto.setPictureUrl(pictureUrl);

                articleSimpleDtos.add(articleSimpleDto);
            }
        }
        return articleSimpleDtos;
    }

    /**
     * 查询归档页文章数据
     * */
    @Override
    public List<ArchiveArticleDto> listArchiveArticle() {

        List<ArchiveArticleDto> archiveArticleDtos = new ArrayList<>();

        ArticleInfoExample articleInfoExample = new ArticleInfoExample();
        articleInfoExample.setOrderByClause("create_by desc");

        List<ArticleInfo>articleInfos = articleInfoMapper.selectByExample(articleInfoExample);

        if(articleInfos.size()==0){
            return null;
        }else {
            for(ArticleInfo a:articleInfos){
                Long articleId = a.getId();
                String title = a.getTitle();
                title = formatFile.formatArticleTitle(title);
                Date create_by = a.getCreate_by();

                //先获取分类id 再查询分类
                Long categoryId = articleCategoryMapper.selectByArticleId(articleId).getCategory_id();
                String category = categoryInfoMapper.selectByPrimaryKey(categoryId).getName();

                ArchiveArticleDto archiveArticleDto = new ArchiveArticleDto();
                archiveArticleDto.setId(articleId);
                archiveArticleDto.setTitle(title);
                archiveArticleDto.setCategory(category);
                archiveArticleDto.setCreate_by(create_by);

                archiveArticleDtos.add(archiveArticleDto);
            }
        }

        return archiveArticleDtos;
    }
}
