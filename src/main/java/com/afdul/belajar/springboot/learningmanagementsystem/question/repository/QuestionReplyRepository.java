package com.afdul.belajar.springboot.learningmanagementsystem.question.repository;

import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.CourseContent;
import com.afdul.belajar.springboot.learningmanagementsystem.question.model.Question;
import com.afdul.belajar.springboot.learningmanagementsystem.question.model.QuestionReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionReplyRepository extends JpaRepository<QuestionReply, Long> {
    List<QuestionReply> findByCourseIdAndCourseContentIdAndQuestionId(
            Course courseId, CourseContent courseContentId, Question questionId);

}
