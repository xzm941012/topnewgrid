package com.example.topnewgrid.obj;

import java.io.Serializable;

/**
 * Created by 真爱de仙 on 2015/3/9.
 */
public class Resume implements Serializable {
    private String merit;
    private int id;
    private String honour;
    private String sex;
    private String skill;
    private String contactInformation;
    private String workExperience;
    private int userId;
    private String age;
    private String name;
    private String education;
    private String locate;
    private String evaluation;

    public Resume(String merit, int id, String honour, String sex, String skill, String contactInformation, String workExperience, int userId, String age, String name, String education, String locate, String evaluation) {
        this.merit = merit;
        this.id = id;
        this.honour = honour;
        this.sex = sex;
        this.skill = skill;
        this.contactInformation = contactInformation;
        this.workExperience = workExperience;
        this.userId = userId;
        this.age = age;
        this.name = name;
        this.education = education;
        this.locate = locate;
        this.evaluation = evaluation;
    }

    public String getMerit() {
        return merit;
    }

    public void setMerit(String merit) {
        this.merit = merit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHonour() {
        return honour;
    }

    public void setHonour(String honour) {
        this.honour = honour;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public String toString() {
        return "Resume{" +
                "merit='" + merit + '\'' +
                ", id=" + id +
                ", honour='" + honour + '\'' +
                ", sex='" + sex + '\'' +
                ", skill='" + skill + '\'' +
                ", contactInformation='" + contactInformation + '\'' +
                ", workExperience='" + workExperience + '\'' +
                ", userId=" + userId +
                ", age='" + age + '\'' +
                ", name='" + name + '\'' +
                ", education='" + education + '\'' +
                ", locate='" + locate + '\'' +
                ", evaluation='" + evaluation + '\'' +
                '}';
    }
}
