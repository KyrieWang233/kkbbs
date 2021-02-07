package com.kyriewang.kkbbs.mapper;

import com.kyriewang.kkbbs.dto.QuestionDto;
import com.kyriewang.kkbbs.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface  QuestionMapper {

    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmt_create},#{gmt_modified},#{creator},#{tag})")
    public void insertQuestion(Question question);

    @Select("select * from question limit #{offset},#{size}")
    public List<Question> getList(@Param(value="offset") Integer offset, @Param(value="size") Integer size);

    @Select("select count(1) from question")
    public Integer count();

    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUserId(@Param(value="userId") Long userId);

    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    public List<Question> getUserList(@Param(value="userId") Long userId,
                                      @Param(value="offset") Integer offset,
                                      @Param(value="size") Integer size);

    @Select("select * from question where id=#{id}")
    public Question getQuestionByquesitonid(@Param(value="id") Long id);

    @Update("update question set title = #{title}, description=#{description}, tag = #{tag}, gmt_modified = #{gmt_modified} where id = #{id}")
    void update(Question question);

    @Update("update question set view_count = view_count+1 where id = #{id}")
    void updateViewCount(@Param(value="id") Long id);

    @Update("update question set comment_count = comment_count+1 where id = #{id}")
    void incCommentCount(@Param(value="id") Long id);
    //查找相关的问题
    @Select("select * from question where id!=#{id} and tag regexp #{tag} order by gmt_create desc limit 20")
    List<Question> getRelatedQuestion(Question question);


}
