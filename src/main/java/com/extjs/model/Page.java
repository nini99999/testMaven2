package com.extjs.model;

/**
 * Created by jenny on 2017/11/17.
 */
public class Page {
    private Integer pagesize; //页面大小
    private Integer pageno; //当前页

    public Page() {
    }

    public Page(Integer pageSize, Integer pageNo) {
        this.pagesize = pageSize; //页面大小
        this.pageno = pageNo;  //当前页

    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pageSize) {
        this.pagesize = pageSize;
    }

    public Integer getPageno() {
        return pageno;
    }

    public void setPageno(Integer pageNo) {
        this.pageno = pageNo;
    }


    @Override
    public String toString() {
        return "[{\"pageno\":" + pageno + ",\"pagesize\":" + pagesize + "}]";
    }


}