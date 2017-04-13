package com.jin.domain;

import java.io.Serializable;

/**
 * 保存成绩信息的类，把每个科目看成一个对象
 */
public class ScoreSearchInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private boolean IsXueWei;

    public boolean isXueWei() {
        return IsXueWei;
    }

    public void setIsXueWei(boolean isXueWei) {
        IsXueWei = isXueWei;
    }

    /**
     * 学期
     */
    private String xueqi;

    /**
     * 补考
     */
    private String Papermakeupinfo;
    /**
     * 重修
     */
    private String Rebuildsocre;
    /**
     * 课程名称
     */
    private String Name;
    /**
     * 实验成绩
     */
    private float Experiment;
    /**
     * 期末成绩
     */
    private float Terminal;
    /**
     * 绩点
     */
    private float GPA;
    /**
     * 平时成绩
     */
    private float Ususal;

    /**
     * 学分
     */
    private float Credit;
    /**
     * 期中成绩
     */
    private float Midterm;
    /**
     * 成绩
     */
    private float Result;


    public String getXueqi() {
        return xueqi;
    }

    public void setXueqi(String xueqi) {
        this.xueqi = xueqi;
    }

    public String getPapermakeupinfo() {
        return Papermakeupinfo;
    }

    public void setPapermakeupinfo(String papermakeupinfo) {
        Papermakeupinfo = papermakeupinfo;
    }

    public String getRebuildsocre() {
        return Rebuildsocre;
    }

    public void setRebuildsocre(String rebuildsocre) {
        Rebuildsocre = rebuildsocre;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public float getExperiment() {
        return Experiment;
    }

    public void setExperiment(float experiment) {
        Experiment = experiment;
    }

    public float getTerminal() {
        return Terminal;
    }

    public void setTerminal(float terminal) {
        Terminal = terminal;
    }

    public float getGPA() {
        return GPA;
    }

    public void setGPA(float gPA) {

        GPA = gPA;
    }

    public float getUsusal() {
        return Ususal;
    }

    public void setUsusal(float ususal) {
        Ususal = ususal;
    }



    public float getCredit() {
        return Credit;
    }

    public void setCredit(float credit) {
        Credit = credit;
    }

    public float getMidterm() {
        return Midterm;
    }

    public void setMidterm(float midterm) {
        Midterm = midterm;
    }

    public float getResult() {
        return Result;
    }

    public void setResult(float result) {
        Result = result;
    }

}
