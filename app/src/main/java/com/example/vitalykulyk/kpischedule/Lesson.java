package com.example.vitalykulyk.kpischedule;

/**
 * Created by Vitaly Kulyk on 23.02.2016.
 */
public class Lesson {

    public String lesson_number   ;
    public String lesson_room     ;
    public String lesson_name     ;
    public String lesson_type     ;
    public String teacher_name    ;
    public String lesson_week     ;
    public String day_number      ;


    public String getLesson_number() {
        return lesson_number;
    }

    public void setLesson_number(String lesson_number) {
        this.lesson_number = lesson_number;
    }

    public String getLesson_room() {
        return lesson_room;
    }

    public void setLesson_room(String lesson_room) {
        this.lesson_room = lesson_room;
    }

    public String getLesson_name() {
        return lesson_name;
    }

    public void setLesson_name(String lesson_name) {
        this.lesson_name = lesson_name;
    }

    public String getLesson_type() {
        return lesson_type;
    }

    public void setLesson_type(String lesson_type) {
        this.lesson_type = lesson_type;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getLesson_week() {
        return lesson_week;
    }

    public void setLesson_week(String lesson_week) {
        this.lesson_week = lesson_week;
    }

    public String getDay_number() {
        return day_number;
    }

    public void setDay_number(String day_number) {
        this.day_number = day_number;
    }
}
