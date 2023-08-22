package com.peertutor.TuitionOrderMgr.service;

import com.peertutor.TuitionOrderMgr.model.Student;
import com.peertutor.TuitionOrderMgr.model.viewmodel.request.StudentProfileReq;
import com.peertutor.TuitionOrderMgr.repository.StudentRepository;
import com.peertutor.TuitionOrderMgr.service.dto.StudentDTO;
import com.peertutor.TuitionOrderMgr.service.mapper.StudentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    @Autowired
    private final StudentMapper studentMapper;
    @Autowired
    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();

        return students.stream().map(studentMapper::toDto).collect(Collectors.toList());

    }

    public StudentDTO getStudentProfileByAccountName(String accountName) {
        Student student = studentRepository.findByAccountName(accountName);

        if (student == null) {
            return null;
        }
        StudentDTO result = studentMapper.toDto(student);

        return result;
    }

    public StudentDTO getStudentProfileById(Long id) {
        Optional<Student> student = studentRepository.findById(id);

        if (!student.isPresent()) {
            return null;
        }
        StudentDTO result = studentMapper.toDto(student.get());

        return result;
    }

    public StudentDTO createStudentProfile(StudentProfileReq req) {
        Student student = studentRepository.findByAccountName(req.name);

        if (student == null) {
            student = new Student();
            student.setAccountName(req.name);
        }

        if (req.displayName != null && !req.displayName.trim().isEmpty()) {
            student.setDisplayName(req.displayName);
        } else {
            student.setDisplayName(req.name);
        }

        student.setIntroduction(req.introduction);
        student.setSubjects(req.subjects);

        try {
            student = studentRepository.save(student);
        } catch (Exception e) {
            logger.error("Student Profile Creation Failed: " + e.getMessage());
            return null;
        }

        StudentDTO result = studentMapper.toDto(student);

        return result;
    }
}
