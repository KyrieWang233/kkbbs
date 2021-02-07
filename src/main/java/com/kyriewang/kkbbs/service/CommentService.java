package com.kyriewang.kkbbs.service;

import com.kyriewang.kkbbs.dto.CommentDto;
import com.kyriewang.kkbbs.enums.CommentTypeEnum;
import com.kyriewang.kkbbs.enums.NotificationStatusEnum;
import com.kyriewang.kkbbs.enums.NotificationTypeEnum;
import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.exception.CustomizerException;
import com.kyriewang.kkbbs.mapper.*;
import com.kyriewang.kkbbs.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    CommentExMapper commentExMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User user){
        if(comment.getParent_id()==null||comment.getParent_id()==0){
            throw new CustomizerException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        Question question;
        Notification notification = new Notification();
        Long receiver;
        if(comment.getType()==1){
            //回复问题
            question = questionMapper.getQuestionByquesitonid(comment.getParent_id());
            if(question==null){
                throw new CustomizerException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //设置消息种类设置接受者
            notification.setType(NotificationTypeEnum.REPLY_QUESTION.getType());
            receiver = question.getCreator();

        }else if(comment.getType()==2){
            //回复评论
            //根据父id获取一级评论
            Comment comment1 = commentMapper.selectByPrimaryKey(comment.getParent_id());
            if(comment1==null){
                throw new CustomizerException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            //根据一级评论的父id获取当前的问题
            question = questionMapper.getQuestionByquesitonid(comment1.getParent_id());
            comment1.setComment_count(1);
            commentExMapper.incCommentCount(comment1);
            //设置消息种类
            notification.setType(NotificationTypeEnum.REPLY_COMMENT.getType());
            receiver = comment1.getComment_creator();
        }else{
            throw new CustomizerException(CustomizeErrorCode.COMMENT_TYPE_ERROR);
        }
        if(receiver!=user.getId())//如果消息不是给自己发的，那么才生成提醒
        {
            notification.setNotifier(user.getId());
            notification.setReceiver(receiver);
            notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
            notification.setGmt_create(System.currentTimeMillis());
            notification.setOut_title(question.getTitle());
            notification.setOuterid(question.getId());
            notification.setOut_username(user.getName());
            notificationMapper.insert(notification);
        }
        questionMapper.incCommentCount(question.getId());
        commentMapper.insert(comment);
    }

    //传入的是问题id，根据问题id查找到评论
    public List<CommentDto> getCommentsByTargetId(Long id,CommentTypeEnum commentTypeEnum) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParent_idEqualTo(id)
                .andTypeEqualTo(commentTypeEnum.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if(comments.size()==0){
            return new ArrayList<>();
        }
        //获取所有的用户id，不重复的数组
        Set<Long> commentators = comments.stream().map(comment -> comment.getComment_creator()).collect(Collectors.toSet());
        List<Long> userids = new ArrayList<>();
        userids.addAll(commentators);
        List<User> users = new ArrayList<>();
        //根据用户id查出所有的用户对象
        for (Long userid:userids) {
            users.add(userMapper.getUser(userid));
        }
        Map<Long,User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(),user -> user));

        //把comment和user封装到commentDto里面
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment:comments) {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(comment,commentDto);
            commentDto.setUser(userMap.get(comment.getComment_creator()));
            commentDtos.add(commentDto);
        }

        return commentDtos;
    }
}
