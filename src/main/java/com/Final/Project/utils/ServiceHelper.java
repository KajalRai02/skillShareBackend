package com.Final.Project.utils;

import com.Final.Project.entity.UserPrincipal;
import com.Final.Project.exception.ProjectIllegalArgumentException;
import com.Final.Project.repository.CourseDao;
import com.Final.Project.repository.LessonDao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ServiceHelper {

    @Autowired
    private LessonDao lessonDao;

    @Autowired
    private CourseDao courseDao;

    public void checkLessonAuthority(int lessonId){
        int ownerId=lessonDao.findById(lessonId).get().getCreatedBy();
        System.out.println("Owner of the course id = "+ownerId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal= (UserPrincipal) auth.getPrincipal();
        int currentUserId= userPrincipal.getId();
        System.out.println("User that is trying to access it ="+currentUserId);
        if(currentUserId!=ownerId){
            throw new ProjectIllegalArgumentException("Only the owner of the course/lesson can make changes to the it.", HttpStatus.UNAUTHORIZED);

        }
    }
    public void checkCourseAuthority(int courseId){
        int ownerId=courseDao.findById(courseId).get().getCreatedBy();
        System.out.println("Owner of the course id = "+ownerId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal= (UserPrincipal) auth.getPrincipal();
        int currentUserId= userPrincipal.getId();
        System.out.println("User that is trying to access it ="+currentUserId);
        if(currentUserId!=ownerId){
            throw new ProjectIllegalArgumentException("Only the owner of the course can make changes to the course.",HttpStatus.UNAUTHORIZED);

        }
    }
}
