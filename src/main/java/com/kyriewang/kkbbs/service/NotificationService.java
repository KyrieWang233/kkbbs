package com.kyriewang.kkbbs.service;

import com.kyriewang.kkbbs.dto.NotificationDto;
import com.kyriewang.kkbbs.dto.PageDto;
import com.kyriewang.kkbbs.enums.NotificationStatusEnum;
import com.kyriewang.kkbbs.enums.NotificationTypeEnum;
import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.exception.CustomizerException;
import com.kyriewang.kkbbs.mapper.NotificationMapper;
import com.kyriewang.kkbbs.model.Notification;
import com.kyriewang.kkbbs.model.NotificationExample;
import com.kyriewang.kkbbs.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired
    NotificationMapper notificationMapper;

    public PageDto getListById(Long id, Integer page, Integer size) {
        Integer offset = size*(page-1);
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(id);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));
        List<NotificationDto> notificationDtos = new ArrayList<>();
        for(Notification notification:notifications){
            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setTypename(NotificationTypeEnum.nameOfType(notification.getType()));
            BeanUtils.copyProperties(notification,notificationDto);
            notificationDtos.add(notificationDto);
        }
        PageDto pageDto = new PageDto();
        pageDto.setNotificationDtos(notificationDtos);
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id);
        pageDto.setPageDtoCount((int)notificationMapper.countByExample(notificationExample),page,size);//计算赋值一共有多少页
        return pageDto;
    }

    public NotificationDto read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification == null){
            throw new CustomizerException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!Objects.equals(notification.getReceiver(),user.getId())){
            throw new CustomizerException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);//如果当前用户与评论对象不一致
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus());//修改消息读取状态
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDto notificationDto = new NotificationDto();
        BeanUtils.copyProperties(notification,notificationDto);

        return notificationDto;
    }

    public Long getUnreadCount(User user) {
        NotificationExample notificationExample = new NotificationExample();
        //查询当前用户所有未读的信息
        notificationExample.createCriteria()
                .andReceiverEqualTo(user.getId())
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }
}
