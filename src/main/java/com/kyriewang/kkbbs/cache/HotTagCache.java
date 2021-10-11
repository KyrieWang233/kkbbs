package com.kyriewang.kkbbs.cache;

import com.kyriewang.kkbbs.dto.HotTagDto;

import java.util.*;

public class HotTagCache {

    private final static HotTagCache hotTagCache = new HotTagCache();
    private List<String> hots = new ArrayList<>();
    private HotTagCache(){
    }
    public static synchronized HotTagCache getHotTagCache(){
        return hotTagCache;
    }
    public void updateTags(Map<String,Integer> m){
        int max = 5;
        PriorityQueue<HotTagDto> priorityQueue = new PriorityQueue<>(max);
        for(String key : m.keySet()){
            HotTagDto hotTagDto = new HotTagDto();
            hotTagDto.setName(key);
            hotTagDto.setPriority(m.get(key));
            if(priorityQueue.size()<max){
                priorityQueue.add(hotTagDto);
            }else
            {
                HotTagDto minHot = priorityQueue.peek();
                if(hotTagDto.compareTo(minHot)>0){
                    priorityQueue.poll();//队首出队
                    priorityQueue.add(hotTagDto);
                }
            }
        }
        List<String> tmp = new ArrayList<>();
        HotTagDto poll = priorityQueue.poll();
        while(poll!=null){
            tmp.add(0,poll.getName());
            poll = priorityQueue.poll();
        }
        this.hots = tmp;
    }
    public List<String> getHots(){
        return this.hots;
    }
}
