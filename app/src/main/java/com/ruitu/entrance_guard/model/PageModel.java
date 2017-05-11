package com.ruitu.entrance_guard.model;

import java.io.Serializable;
import java.util.List;

/**
 * 分页bean
 */
public class PageModel<T> implements Serializable {

    public int totalPage;
    public List<T> dataList;

    @Override
    public String toString() {
        return "PageModel{" +
                "totalPage=" + totalPage +
                ", dataList=" + dataList +
                '}';
    }
}
