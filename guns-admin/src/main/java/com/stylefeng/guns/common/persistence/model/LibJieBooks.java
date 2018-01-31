package com.stylefeng.guns.common.persistence.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 格调先生123
 * @since 2018-01-31
 */
@TableName("lib_jie_books")
public class LibJieBooks extends Model<LibJieBooks> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String bookId;
    private String bookName;
    private Integer categoryId;
    private String cagegoryName;
    private String notice;
    private Integer sortNo;
    /**
     * 0 库存不足  1 库存正常
     */
    private String status;
    private String des;
    private Integer count;
    private Integer selCount;
    private String location;
    private String categorys;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCagegoryName() {
        return cagegoryName;
    }

    public void setCagegoryName(String cagegoryName) {
        this.cagegoryName = cagegoryName;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getSelCount() {
        return selCount;
    }

    public void setSelCount(Integer selCount) {
        this.selCount = selCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategorys() {
        return categorys;
    }

    public void setCategorys(String categorys) {
        this.categorys = categorys;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "LibJieBooks{" +
        "id=" + id +
        ", bookId=" + bookId +
        ", bookName=" + bookName +
        ", categoryId=" + categoryId +
        ", cagegoryName=" + cagegoryName +
        ", notice=" + notice +
        ", sortNo=" + sortNo +
        ", status=" + status +
        ", des=" + des +
        ", count=" + count +
        ", selCount=" + selCount +
        ", location=" + location +
        ", categorys=" + categorys +
        "}";
    }
}
