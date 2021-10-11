package com.kyriewang.kkbbs.schedule;

import com.kyriewang.kkbbs.cache.HotTagCache;
import com.kyriewang.kkbbs.mapper.QuestionMapper;
import com.kyriewang.kkbbs.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.*;

@Component
@Slf4j
public class HotTagTasks {

    @Autowired
    QuestionMapper questionMapper;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 3)
    //@Scheduled(cron = "0 0 1 * * *")
    public void reportCurrentTime(){
        int offset = 0;
        int limit = 5;
        List<Question> list = new ArrayList<>();
        Map<String,Integer> priorities = new HashMap<>();
        while(offset==0||list.size()==limit){
            list = questionMapper.getList(offset,limit);
            for(Question question:list){
                String[] tags = StringUtils.split(question.getTag(),",");
                for(String tag:tags){
                    Integer priority = priorities.get(tag);
                    if(priority!=null){
                        priorities.put(tag,priority + 5 + question.getComment_count());
                    }else{
                        priorities.put(tag,5+question.getComment_count());
                    }
                }
            }
            offset+=limit;
        }
        HotTagCache.getHotTagCache().updateTags(priorities);
    }
}
