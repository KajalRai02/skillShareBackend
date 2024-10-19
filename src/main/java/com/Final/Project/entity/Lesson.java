package com.Final.Project.entity;


import com.Final.Project.Auditor.AuditorEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="lesson")
public class Lesson extends AuditorEntity {

    //declare fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="lesson_id")
    private int id;

    @Column(name="lesson_name")
    private String lessonName;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="course_id")
    private Course course;


    //parameterized Constructor

    public Lesson(int id, String lessonName) {
        this.id = id;
        this.lessonName = lessonName;
    }
    //declare to string


    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", lessonName='" + lessonName + '\'' +
                '}';
    }
}
