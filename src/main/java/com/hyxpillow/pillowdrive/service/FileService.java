package com.hyxpillow.pillowdrive.service;

import com.hyxpillow.pillowdrive.common.exception.PillowException;
import com.hyxpillow.pillowdrive.common.result.ResultCodeEnum;
import com.hyxpillow.pillowdrive.common.threadlocal.AuthThreadLocal;
import com.hyxpillow.pillowdrive.entity.FileEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

@Service
public class FileService {
    private final String rootPath = "/home/hyxpillow/pillowdrive-data/";

    public List<FileEntity> showFiles(String path) throws PillowException {
        String uid = AuthThreadLocal.get();
        String targetPath = rootPath + uid + "/" + path;

        File targetDir = new File(targetPath);
        if (!targetDir.exists() || !targetDir.isDirectory()) {
            throw new PillowException(ResultCodeEnum.FILE_NOT_FOUND);
        }
        File[] files = targetDir.listFiles();
        List<FileEntity> fileList = new ArrayList<>();
        assert files != null;
        for (File file : files) {
            String name = file.getName();
            long bytes = file.length();
            long lastModified = file.lastModified();
            boolean isDirectory = file.isDirectory();
            if (isDirectory) {
                fileList.add(new FileEntity(name, null, convertTime(lastModified), isDirectory));
            } else {
                fileList.add(new FileEntity(name, convertSize(bytes), convertTime(lastModified), isDirectory));
            }
        }
        return fileList;
    }

    public void deleteFiles(List<String> pathList) throws PillowException {
        String uid = AuthThreadLocal.get();
        for (String path : pathList) {
            Path fileToBeDeleted = Paths.get(rootPath + uid  + "/" + path);
            try {
                if (Files.isDirectory(fileToBeDeleted)) {
                    deleteFolderRecursively(fileToBeDeleted);
                } else {
                    Files.deleteIfExists(fileToBeDeleted);
                }
            } catch (IOException e) {
                throw new PillowException(ResultCodeEnum.FAIL);
            }
        }
    }

    public void renameFile(String oldPath, String newPath) throws PillowException {
        String uid = AuthThreadLocal.get();
        Path oldFile = Paths.get(rootPath + uid + "/" + oldPath);
        Path newFile = Paths.get(rootPath + uid + "/" + newPath);

        if (!Files.exists(oldFile)) {
            throw new PillowException(ResultCodeEnum.FILE_NOT_FOUND);
        }
        if (Files.exists(newFile)) {
            throw new PillowException(ResultCodeEnum.FILE_FOUND);
        }
        boolean succ = oldFile.toFile().renameTo(newFile.toFile());
        if (!succ) {
            throw new PillowException(ResultCodeEnum.FAIL);
        }
    }

    public void createFolder(String path) throws PillowException {
        String uid = AuthThreadLocal.get();
        Path dirPath = Paths.get(rootPath + uid + "/" + path);
        if (Files.exists(dirPath)) {
            throw new PillowException(ResultCodeEnum.FILE_FOUND);
        }
        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            throw new PillowException(ResultCodeEnum.FAIL);
        }
    }

    public void uploadFiles(MultipartFile file, String path) throws PillowException {
        String uid = AuthThreadLocal.get();
        Path dirPath = Paths.get(rootPath + uid + "/" + path);
        try {
            Path filePath = dirPath.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath);
        } catch (IOException e) {
            throw new PillowException(ResultCodeEnum.MAX_FILE_SIZE_EXCEED);
        }
    }

    public void downloadFiles(List<String> pathList, HttpServletResponse response) throws PillowException {
        String uid = AuthThreadLocal.get();
        List<String> absolutePathList = new ArrayList<>();
        try {
            response.setContentType("application/octet-stream");
            InputStream in = null;
            if (pathList.size() > 1 || Files.isDirectory(Paths.get(absolutePathList.get(0)))) { // 多文件或文件夹下载
                String command = "zip -r -j " + rootPath + uid + "/files.zip " + String.join(" ", absolutePathList);
                System.out.println(command);
                Runtime.getRuntime().exec(command);
                Thread.sleep(500);
                in = new FileInputStream(rootPath + uid + "/files.zip");
            } else { // 单文件下载
                in = new FileInputStream(absolutePathList.get(0));
            }
            OutputStream out = response.getOutputStream();
            int count;
            byte[] by = new byte[1024];
            //通过response对象获取OutputStream流
            while((count = in.read(by)) != -1){
                out.write(by, 0, count);//将缓冲区的数据输出到浏览器
            }
            in.close();
            out.flush();
            out.close();
            Runtime.getRuntime().exec("rm " + rootPath + uid + "/files.zip");
        } catch (Exception e) {
            throw new PillowException(ResultCodeEnum.FAIL);
        }
    }

    private void deleteFolderRecursively(Path folder) throws IOException {
        if (Files.isDirectory(folder)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
                for (Path entry : stream) {
                    deleteFolderRecursively(entry);
                }
            }
        }
        Files.delete(folder);
    }
    private String convertTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(Long.parseLong(String.valueOf(timestamp))));
    }
    private String convertSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return (bytes / 1024) + " KB";
        } else {
            return (bytes / 1024 / 1024) + " MB";
        }
    }
}
