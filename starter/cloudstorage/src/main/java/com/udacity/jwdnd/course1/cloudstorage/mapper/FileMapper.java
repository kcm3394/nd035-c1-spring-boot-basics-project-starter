package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    //create (upload)
    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{name}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int uploadFile(File file);

    //read
    @Select("SELECT * FROM FILES where userid = #{userId}")
    List<File> getFilesByUserId(Integer userId);

    //update

    //delete
    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    void deleteFileById(Integer fileId);
}
