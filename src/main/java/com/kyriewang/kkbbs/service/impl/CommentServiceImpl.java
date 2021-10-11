package com.kyriewang.kkbbs.service.impl;

import com.kyriewang.kkbbs.dto.CommentDto;
import com.kyriewang.kkbbs.dto.CommentPostDto;
import com.kyriewang.kkbbs.dto.UserDto;
import com.kyriewang.kkbbs.enums.CommentTypeEnum;
import com.kyriewang.kkbbs.enums.NotificationStatusEnum;
import com.kyriewang.kkbbs.enums.NotificationTypeEnum;
import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.exception.CustomizerException;
import com.kyriewang.kkbbs.mapper.*;
import com.kyriewang.kkbbs.model.*;
import com.kyriewang.kkbbs.service.CommentService;
import com.kyriewang.kkbbs.shiro.AccountProfile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Service
public class CommentServiceImpl implements CommentService {

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
    public void insert(CommentPostDto commentPostDto, AccountProfile user){
        Comment comment = new Comment();
        comment.setParent_id(commentPostDto.getParent_id());
        comment.setContent(commentPostDto.getContent());
        comment.setType(commentPostDto.getType());
        comment.setComment_creator(user.getId());
        comment.setComment_count(0);
        comment.setLike_count(0l);
        comment.setGmt_create(System.currentTimeMillis());
        comment.setGmt_modified(System.currentTimeMillis());
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
            //一级评论，接收对象为问题的创建者
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
            //如果是二级评论接收者为评论的回复对象id
            receiver = commentPostDto.getReceiver_id();
        }else{
            throw new CustomizerException(CustomizeErrorCode.COMMENT_TYPE_ERROR);
        }
        if(!receiver.equals(user.getId()))//如果消息不是给自己发的，那么才生成提醒
        {
            notification.setNotifier(user.getId());
            notification.setReceiver(receiver);
            notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
            notification.setGmt_create(System.currentTimeMillis());
            notification.setOut_title(question.getTitle());
            notification.setOuterid(question.getId());
            notification.setOut_username(user.getName());//这个表示生成的提示链接显示的username
            notificationMapper.insert(notification);
        }
        //给评论也添加回复对象的id
        comment.setReceiver_id(receiver);
        //把找个设计在数据库里，主要是觉得这样每次看评论的时候不用再查多查一次用户表了（毕竟只需要名字）
        comment.setReceiver_name(userMapper.getUser(receiver).getName());
        questionMapper.incCommentCount(question.getId());
        commentMapper.insert(comment);
    }

    //传入的是问题id，根据问题id查找到评论
    public List<CommentDto> getCommentsByTargetId(Long id,CommentTypeEnum commentTypeEnum) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParent_idEqualTo(id)
                .andTypeEqualTo(commentTypeEnum.getType());
        //如果是一级评论就是降序
        if(commentTypeEnum==CommentTypeEnum.QUESSTION){
            commentExample.setOrderByClause("gmt_create desc");
        }
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if(comments.size()==0){
            return new ArrayList<>();
        }
        //获取所有的用户id，不重复的数组
        Set<Long> commentators = comments.stream().map(comment -> comment.getComment_creator()).collect(Collectors.toSet());
        List<Long> userids = new ArrayList<>();
        userids.addAll(commentators);
        List<UserDto> users = new ArrayList<>();
        //根据用户id查出所有的用户对象
        for (Long userid:userids) {
            User user  = userMapper.getUser(userid);
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user,userDto);
            users.add(userDto);
        }
        Map<Long,UserDto> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(),user -> user));

        //把comment和user封装到commentDto里面
        List<CommentDto> commentDtos = new ArrayList<>();
        List<CommentDto> replys = new ArrayList<>();
        for (Comment comment:comments) {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(comment,commentDto);
            commentDto.setUser(userMap.get(comment.getComment_creator()));
            commentDto.setReplys(this.getCommentsByTargetId(commentDto.getId(),CommentTypeEnum.COMMENT));
            commentDtos.add(commentDto);
        }

        return commentDtos;
    }
}
