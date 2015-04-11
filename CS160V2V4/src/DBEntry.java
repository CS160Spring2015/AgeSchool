/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ryaneager
 */
public class DBEntry {
    String title;
    String description;
    String lesson_link;
    String lesson_image;
    String category;
    String student_grades;
    String author;
    String content_type;
    long time_scraped;
    
    public DBEntry(){
        title = "";
        description = "";
        lesson_link = "";
        lesson_image = "";
        category = "";
        student_grades = "";
        author = "";
        content_type = "Interactive";
        time_scraped = 0;
    }
    
    public DBEntry(DBEntry in){
        title = in.title;
        description = in.description;
        lesson_link = in.lesson_link;
        lesson_image = in.lesson_image;
        category = in.category;
        student_grades = in.student_grades;
        author = in.author;
        content_type = in.content_type;
        time_scraped = in.time_scraped;
    }
    
    @Override
    public String toString(){
        return title + " " + description + " " + lesson_link + " " + category + " " + student_grades + " " + author + " " + time_scraped ;
    }
}
