package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    //create
    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{title}, #{description}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int createNote(Note note);

    //read
    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getNotesByUserId(Integer userId);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    Note getNoteByNoteId(Integer noteId);

    //update
    @Update("UPDATE note SET notetitle=#{title}, notedescription=#{description} WHERE noteid = #{noteId}")
    int updateNote(String title, String description, Integer noteId);

    //delete
    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    int deleteNoteById(Integer noteId);
}
