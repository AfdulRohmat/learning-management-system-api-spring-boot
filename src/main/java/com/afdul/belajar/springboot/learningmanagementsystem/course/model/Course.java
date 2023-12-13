package com.afdul.belajar.springboot.learningmanagementsystem.course.model;

import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private double estimatedPrice;

    private String tags;
    private String level;
    private String demoUrl;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private List<CourseBenefit> benefits;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private List<CoursePrerequisite> prerequisites;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private List<CourseData> courseData;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;
}
