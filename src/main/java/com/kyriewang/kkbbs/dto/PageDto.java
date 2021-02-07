package com.kyriewang.kkbbs.dto;

import lombok.Data;

import java.util.List;

//封装问题和页码信息
@Data
public class PageDto {
    private List<QuestionDto> questionDtos;
    private List<NotificationDto> notificationDtos;
    private Integer currentPage;
    private Integer totalPages;

    public void setPageDtoCount(Integer questionCount,Integer currentPage,Integer size){
        if(questionCount%size==0){
            this.totalPages = questionCount/size;
        }
        else {
            this.totalPages = questionCount / size + 1;
        }
        this.currentPage = currentPage;
    }
}
