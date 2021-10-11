package com.kyriewang.kkbbs.mapper;

import com.kyriewang.kkbbs.dto.QuestionDto;
import com.kyriewang.kkbbs.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface  QuestionMapper {

    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmt_create},#{gmt_modified},#{creator},#{tag})")
    void insertQuestion(Question question);

    @Delete("delete from question where id=#{id}")
    void deleteQuestion(Long id);

    @Select("select * from question order by gmt_create desc limit #{offset},#{size}")
    List<Question> getList(@Param(value="offset") Integer offset, @Param(value="size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUserId(@Param(value="userId") Long userId);

    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    List<Question> getUserList(@Param(value="userId") Long userId,
                                      @Param(value="offset") Integer offset,
                                      @Param(value="size") Integer size);

    @Select("select * from question where id=#{id}")
    Question getQuestionByquesitonid(@Param(value="id") Long id);

    @Update("update question set title = #{title}, description=#{description}, tag = #{tag}, gmt_modified = #{gmt_modified} where id = #{id}")
    void update(Question question);

    @Update("update question set view_count = view_count+1 where id = #{id}")
    void updateViewCount(@Param(value="id") Long id);

    @Update("update question set comment_count = comment_count+1 where id = #{id}")
    void incCommentCount(@Param(value="id") Long id);
    //查找相关的问题
    @Select("select * from question where id!=#{id} and tag regexp #{tag} order by gmt_create desc limit 20")
    List<Question> getRelatedQuestion(Question question);

    //根据搜索和标签查找问题

    @Select({
            "<script>" ,
            "select * from question",
            "where 1=1",
            "<when test='search != null '>",
            "and title regexp #{search}",
            "</when>",
            "<when test='tag != null '>",
            "and tag regexp #{tag}",
            "</when>",
            "<when test='sort != null and sort == \"no\"'>",
            "and comment_count = 0",
            "</when>",
            "<when test=' time != null '>",
            "and gmt_create > #{time}",
            "</when>",
            "<if test=' sort != null and (sort == \"hot\" || sort == \"hot7\" || sort == \"hot30\") '>",
            "order by comment_count desc",
            "</if>",
            "<if test=' sort == null or sort==\"new\" or sort==\"no\" '>",
            "order by gmt_create desc",
            "</if>",
            "limit #{offset},#{size}",
            "</script>"
    })
    List<Question> getListBySearchTag(@Param("search") String search,
                                      @Param("tag") String tag,
                                      @Param("sort") String sort,
                                      @Param("time") Long time,
                                      @Param("offset") Integer offset,
                                      @Param("size") Integer size);

    @Select("select * from question where title regexp #{search} order by gmt_create desc limit #{offset},#{size}")
    List<Question> getListBySearch(String search, Integer offset, Integer size);

    @Select({
            "<script>" ,
            "select count(1) from question",
            "where 1=1",
            "<if test='search != null and search != \" \" '>",
            "and title regexp #{search}",
            "</if>",
            "<if test='tag != null and tag != \" \" '>",
            "and tag regexp #{tag}",
            "</if>",
            "<when test='sort != null and sort == \"no\"'>",
            "and comment_count = 0",
            "</when>",
            "<when test=' time != null '>",
            "and gmt_create > #{time}",
            "</when>",
            "</script>"
    })
    Integer countBySearchTag(String search,String tag,String sort,Long time);

    @Select("select count(1) from question where title regexp #{search}")
    Integer countBySearch(String search);
}
