package com.Final.Project.entity;

import com.Final.Project.Auditor.AuditorEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "course")
public class Course extends AuditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Lesson> lessons = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(name = "course_user",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Users>students;

    // Add a lesson to the course
    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
        lesson.setCourse(this); // Set the course reference in the Lesson
    }

    // Remove a lesson from the course
    public void removeLesson(Lesson lesson) {
        lessons.remove(lesson);
        lesson.setCourse(null); // Remove the course reference in the Lesson
    }

    // Parameterized Constructor
    public Course(String courseName) {
        this.courseName = courseName;
    }

    // toString
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}
