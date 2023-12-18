package com.afdul.belajar.springboot.learningmanagementsystem.course.model;

import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private double price;
    private double discount;
    private String tags;
    private String level;

    @Column(name = "video_demo")
    private String videoDemo;

    private String thumbnail;

    @ElementCollection
    @CollectionTable(name = "course_benefits", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "benefit")
    private List<String> benefits;

    @ElementCollection
    @CollectionTable(name = "course_prerequisites", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "prerequisites")
    private List<String> prerequisites;

    private double ratings;
    private int purchased;

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
