package com.you.domain.portalview;

import com.you.domain.course.CourseBase;
import com.you.domain.course.CourseMarket;
import com.you.domain.course.CoursePic;
import com.you.domain.course.ext.TeachplanNode;
import com.you.domain.report.ReportCourse;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by MichaelLiu on 2019/2/27.
 */
@Data
@ToString
@Document(collection = "view_course")
public class ViewCourse implements Serializable{

    @Id
    private String id;
    private CourseBase courseBase;
    private CourseMarket courseMarket;
    private CoursePic coursePic;
    private TeachplanNode teachplan;
    //课程统计信息
    private ReportCourse reportCourse;

}
