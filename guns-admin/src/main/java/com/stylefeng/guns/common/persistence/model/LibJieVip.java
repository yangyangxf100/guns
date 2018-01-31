package com.stylefeng.guns.common.persistence.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
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
@TableName("lib_jie_vip")
public class LibJieVip extends Model<LibJieVip> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String price;
    private String effectDays;
    private String addDays;
    private String borrowTimes;
    private String localTimes;
    private String yaJin;
    private String maxBorrowBookNum;
    private String des;
    private Date createTime;
    /**
     * 0 下线,1 上线
     */
    private String status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEffectDays() {
        return effectDays;
    }

    public void setEffectDays(String effectDays) {
        this.effectDays = effectDays;
    }

    public String getAddDays() {
        return addDays;
    }

    public void setAddDays(String addDays) {
        this.addDays = addDays;
    }

    public String getBorrowTimes() {
        return borrowTimes;
    }

    public void setBorrowTimes(String borrowTimes) {
        this.borrowTimes = borrowTimes;
    }

    public String getLocalTimes() {
        return localTimes;
    }

    public void setLocalTimes(String localTimes) {
        this.localTimes = localTimes;
    }

    public String getYaJin() {
        return yaJin;
    }

    public void setYaJin(String yaJin) {
        this.yaJin = yaJin;
    }

    public String getMaxBorrowBookNum() {
        return maxBorrowBookNum;
    }

    public void setMaxBorrowBookNum(String maxBorrowBookNum) {
        this.maxBorrowBookNum = maxBorrowBookNum;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "LibJieVip{" +
        "id=" + id +
        ", name=" + name +
        ", price=" + price +
        ", effectDays=" + effectDays +
        ", addDays=" + addDays +
        ", borrowTimes=" + borrowTimes +
        ", localTimes=" + localTimes +
        ", yaJin=" + yaJin +
        ", maxBorrowBookNum=" + maxBorrowBookNum +
        ", des=" + des +
        ", createTime=" + createTime +
        ", status=" + status +
        "}";
    }
}
