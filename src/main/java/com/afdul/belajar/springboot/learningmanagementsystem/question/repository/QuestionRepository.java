package com.afdul.belajar.springboot.learningmanagementsystem.question.repository;

import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.CourseContent;
import com.afdul.belajar.springboot.learningmanagementsystem.question.model.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q " +
            "FROM Question q " +
            "LEFT JOIN FETCH q.questionReplies " +
            "WHERE q.courseId = :courseId AND q.courseContentId = :courseContentId")
    List<Question> getAllQuestions(@Param("courseId") Course courseId,
                                   @Param("courseContentId") CourseContent courseContentId,
                                   Pageable pageable);

    @Query("SELECT COUNT(qr) FROM QuestionReply qr WHERE qr.courseId = :courseId AND qr.courseContentId = :courseContentId AND qr.questionId = :questionId")
    long countRepliesByQuestionId(@Param("courseId") Course courseId,
                                  @Param("courseContentId") CourseContent courseContentId,
                                  @Param("questionId") Question questionId);

    Optional<Question> findByIdAndCourseIdAndCourseContentId(
            Long id,
            Course courseId,
            CourseContent courseContentId
    );

}
