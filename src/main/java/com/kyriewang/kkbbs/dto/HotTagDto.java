package com.kyriewang.kkbbs.dto;

import lombok.Data;

@Data
public class HotTagDto implements Comparable{
    private String name;
    private Integer priority;

    @Override
    public int compareTo(Object o) {
        return this.priority - ((HotTagDto)o).getPriority();
    }
}
