package com.kyriewang.kkbbs.service;

import com.kyriewang.kkbbs.dto.PageDto;
import com.kyriewang.kkbbs.dto.QuestionDto;
import com.kyriewang.kkbbs.model.Question;

import java.util.List;

public interface QuestionService {

    PageDto GetQuestionList(String search,String tag,String sort, Integer page, Integer size);

    PageDto getListById(Long userId, Integer page, Integer size);

    QuestionDto getQuestionById(Long id);

    void incView(Long id);

    List<Question> getRelatedQuestions(QuestionDto questionDto);

    void deleteQuestion(Long id);

}
