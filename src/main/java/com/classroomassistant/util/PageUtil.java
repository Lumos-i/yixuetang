package com.classroomassistant.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageUtil<T> {
    private List<T> list;
    private Integer allCount;
    private Integer allPage;
    private Integer curIndex;
    private Integer size;
    public PageUtil<T> getCount(List<T> list1,Integer begin_index,Integer size){
        if (begin_index<1||size<1){
            throw new RuntimeException("数据输入错误");
        }
        this.allCount=list1.size();
        this.curIndex=begin_index;
        this.size=size;
        Integer page=this.allCount/size;
        if (page*size!=this.allCount){
            this.allPage=page+1;
        }else {
            this.allPage=page;
        }
        List<T> tList=new ArrayList<>();
        int j=((begin_index*size>this.allCount)?this.allCount:begin_index*size);
        for (int i=begin_index-1<1?0:(begin_index-1)*size;
             i<j;
             i++){
            tList.add(list1.get(i));
        }
        this.list=tList;
        return new PageUtil<T>(this.list,this.allCount,this.allPage,this.curIndex,this.size);
    }
}
