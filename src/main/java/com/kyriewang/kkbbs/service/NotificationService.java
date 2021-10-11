package com.kyriewang.kkbbs.service;

import com.kyriewang.kkbbs.dto.PageDto;

public interface NotificationService {

    PageDto getListById(Long id, Integer page, Integer size);

    Boolean read(Long id, Long userId);

    Long getUnreadCount(Long userId);
}
