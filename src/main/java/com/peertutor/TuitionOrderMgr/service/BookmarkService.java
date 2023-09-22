package com.peertutor.TuitionOrderMgr.service;

import com.peertutor.TuitionOrderMgr.model.Bookmark;
import com.peertutor.TuitionOrderMgr.model.viewmodel.request.BookmarkReq;
import com.peertutor.TuitionOrderMgr.repository.BookmarkRepository;
import com.peertutor.TuitionOrderMgr.service.dto.BookmarkDTO;
import com.peertutor.TuitionOrderMgr.service.dto.TuitionOrderDTO;
import com.peertutor.TuitionOrderMgr.service.mapper.BookmarkMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookmarkService {
    private static final Logger logger = LoggerFactory.getLogger(BookmarkService.class);
    @Autowired
    private final BookmarkMapper bookmarkMapper;
    @Autowired
    private BookmarkRepository bookmarkRepository;


    public BookmarkService(BookmarkRepository bookmarkRepository, BookmarkMapper bookmarkMapper) {
        this.bookmarkRepository = bookmarkRepository;
        this.bookmarkMapper = bookmarkMapper;
    }

    // get bookmark by student id
    @Transactional(readOnly = true)
    public Page<BookmarkDTO> getBookmark(Long studentId, Pageable pageable) {
        Page<BookmarkDTO> bookmark = bookmarkRepository.findByStudentID(studentId, pageable).map(bookmarkMapper::toDto);

        return bookmark;
    }

    // get bookmark by student id and tutor id
    public BookmarkDTO getBookmark(Long studentId, Long tutorId) {
        Optional<Bookmark> bookmark = bookmarkRepository.findByTutorIDAndStudentID(tutorId, studentId);

        return bookmark.map(bookmarkMapper::toDto).orElseGet(() -> null);
    }

    // create bookmark by tutor id
    public BookmarkDTO createBookmark(BookmarkReq req){
        Optional<Bookmark> bookmarkOptional;

        if (req.id != null) {
            bookmarkOptional = bookmarkRepository.findById(req.id);
        } else {
            bookmarkOptional = bookmarkRepository.findByTutorIDAndStudentID(req.tutorID, req.studentID);
        }

        Bookmark bookmark;

        // if is new create new bookmark
        if(!bookmarkOptional.isPresent()) {
            bookmark = new Bookmark();
        } else {
            bookmark = bookmarkOptional.get();
        }

        bookmark.setTutorID(req.tutorID);
        bookmark.setStudentID(req.studentID);

        try {
            bookmark = bookmarkRepository.save(bookmark);
        } catch (Exception e) {
            logger.error("Bookmark Creation Failed: " + e.getMessage());
            return null;
        }

        BookmarkDTO result = bookmarkMapper.toDto(bookmark);

        return result;
    }

    public void deleteBookmarkById(Long id) {
        bookmarkRepository.deleteById(id);
    }

    public void deleteBookmarkByStudentAndTutor(Long studentId, Long tutorId) {
        bookmarkRepository.deleteByTutorIDAndStudentID(tutorId, studentId);
    }
}
