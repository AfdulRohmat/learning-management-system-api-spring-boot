package com.afdul.belajar.springboot.learningmanagementsystem.question.model;

import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.CourseContent;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "question_replies")
public class QuestionReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String reply;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course courseId;

    @ManyToOne
    @JoinColumn(name = "course_content_id", nullable = false)
    private CourseContent courseContentId;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question questionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

}
