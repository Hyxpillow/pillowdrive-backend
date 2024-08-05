package com.hyxpillow.pillowdrive.controller;

import com.hyxpillow.pillowdrive.common.exception.PillowException;
import com.hyxpillow.pillowdrive.common.result.Result;
import com.hyxpillow.pillowdrive.common.threadlocal.AuthThreadLocal;
import com.hyxpillow.pillowdrive.dto.request.*;
import com.hyxpillow.pillowdrive.dto.response.FileListResponse;
import com.hyxpillow.pillowdrive.entity.FileEntity;
import com.hyxpillow.pillowdrive.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/file/list")
    public Result<FileListResponse> showFiles(@RequestBody FileListRequest fileListRequest) throws PillowException {
        String path = fileListRequest.getPath();
        log.info("SHOW_FILES uid:{} path:{}", AuthThreadLocal.get(), path);
        List<FileEntity> fileList = fileService.showFiles(path);
        FileListResponse response = new FileListResponse();
        response.setAllFiles(fileList);
        return Result.ok(response);
    }

    @PostMapping("/file/rename")
    public Result<String> renameFile(@RequestBody RenameFileRequest renameFileRequest) throws PillowException {
        String src = renameFileRequest.getSrc();
        String dst = renameFileRequest.getDst();
        log.info("RENAME_FILES uid:{} src:{} dst:{}", AuthThreadLocal.get(), src, dst);
        fileService.renameFile(src, dst);
        return Result.ok();
    }

    @PostMapping("/file/create")
    public Result<String> createFolder(@RequestBody CreateFolderRequest createFolderRequest) throws PillowException {
        String path = createFolderRequest.getPath();
        log.info("CREATE_FOLDER uid:{} path:{}", AuthThreadLocal.get(), path);
        fileService.createFolder(path);
        return Result.ok();
    }

    @PostMapping("/file/upload")
    public Result<String> uploadFiles(@RequestParam("file") MultipartFile file,
                                      @RequestParam("path") String path) throws PillowException {
        log.info("UPLOAD_FILES uid:{} path: {}", AuthThreadLocal.get(), path);
        fileService.uploadFiles(file, path);
        return Result.ok();
    }
    @PostMapping("/file/download")
    public void downloadFiles(@RequestBody DownloadFilesRequest downloadFilesRequest,
                                        HttpServletResponse response) throws PillowException {
        List<String> pathList = downloadFilesRequest.getPathList();
        log.info("DOWNLOAD_FILES uid:{}, path:{}", AuthThreadLocal.get(), pathList);
        fileService.downloadFiles(pathList, response);
    }
    @PostMapping("/file/delete")
    public Result<String> deleteFiles(@RequestBody DeleteFilesRequest deleteFilesRequest) throws PillowException {
        List<String> pathList = deleteFilesRequest.getPathList();
        log.info("DELETE_FILES uid:{} path:{}", AuthThreadLocal.get(), pathList);
        fileService.deleteFiles(pathList);
        return Result.ok();
    }
}
