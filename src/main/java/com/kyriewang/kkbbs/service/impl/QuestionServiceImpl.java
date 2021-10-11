package com.kyriewang.kkbbs.service.impl;

import com.kyriewang.kkbbs.dto.PageDto;
import com.kyriewang.kkbbs.dto.QuestionDto;
import com.kyriewang.kkbbs.dto.UserDto;
import com.kyriewang.kkbbs.enums.SortEnum;
import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.exception.CustomizerException;
import com.kyriewang.kkbbs.mapper.QuestionMapper;
import com.kyriewang.kkbbs.mapper.UserMapper;
import com.kyriewang.kkbbs.model.Question;
import com.kyriewang.kkbbs.service.QuestionService;
import com.kyriewang.kkbbs.service.UserService;
import com.kyriewang.kkbbs.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;


    public PageDto GetQuestionList(String search,String tag,String sort,Integer page, Integer size){
        if(page<1) page=1;

        Integer offset = size*(page-1);
        List<Question> questions = new ArrayList<>();
        Integer count;
        Long time = null;
        if((search==null||search.equals(""))&&(tag==null||tag.equals(""))&&(sort==null||sort.equals(""))){//两个都为空
            questions = questionMapper.getList(offset,size);
            count = questionMapper.count();
        }
        else{
            if(tag.equals("")){
                tag=null;//防止匹配空字符串错误
            }
            if(search.equals("")){
                search=null;
            }
            if(sort.equals("")){
                sort=null;
            }else{
                for(SortEnum sortEnum :SortEnum.values()){
                    if(sortEnum.name().toLowerCase().equals(sort)){
                        if (sortEnum == SortEnum.HOT7) {
                            time = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 7;
                        }
                        if (sortEnum == SortEnum.HOT30) {
                            time = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 7;
                        }
                        break;
                    }
                }
            }
            questions = questionMapper.getListBySearchTag(search,tag,sort,time,offset,size);
            count = questionMapper.countBySearchTag(search,tag,sort,time);
        }

        List<QuestionDto> questionDtoList = new ArrayList<>();
        for(Question question : questions){
            UserDto user = userService.getuserById(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        PageDto pageDto = new PageDto();
        pageDto.setQuestionDtos(questionDtoList);
        pageDto.setPageDtoCount(count,page,size);//计算赋值一共有多少页
        return pageDto;
    }

    //根据用户id获取所有的问题
    public PageDto getListById(Long userId, Integer page, Integer size) {
        Integer offset = size*(page-1);
        List<Question>  questions = questionMapper.getUserList(userId,offset,size);
        List<QuestionDto> questionDtoList = new ArrayList<>();
        for(Question question : questions){
            UserDto user = userService.getuserById(userId);
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        PageDto pageDto = new PageDto();
        pageDto.setQuestionDtos(questionDtoList);
        pageDto.setPageDtoCount(questionMapper.countByUserId(userId),page,size);//计算赋值一共有多少页
        return pageDto;
    }

    public QuestionDto getQuestionById(Long id) {
        Question  question = questionMapper.getQuestionByquesitonid(id);
        if(question == null){
            throw new CustomizerException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        UserDto user = userService.getuserById(question.getCreator());
        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(question,questionDto);
        questionDto.setUser(user);
        return questionDto;
    }

    public void incView(Long id) {
        questionMapper.updateViewCount(id);
    }

    public List<Question> getRelatedQuestions(QuestionDto questionDto) {
        if(questionDto.getId()==null){
            throw new CustomizerException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        ArrayList<Question> related= new ArrayList<>();
        if(questionDto.getTag()==null||questionDto.getTag().equals("")){
            return related;
        }
        String[] tags = questionDto.getTag().split(",");
        //使用stream拼接字符串
        //|表示匹配当中的任意一个
        String regexpTag = Arrays.stream(tags)
                .map(t -> t.replace("+","")).collect(Collectors.joining("|"));
        Question question=new Question();
        question.setId(questionDto.getId());
        question.setTag(regexpTag);
        List<Question> relatedQuestions = questionMapper.getRelatedQuestion(question);
        return relatedQuestions;
    }

    public void deleteQuestion(Long id){
        questionMapper.deleteQuestion(id);
    }
}
