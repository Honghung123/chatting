package com.honghung.chatapp.service.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.honghung.chatapp.component.exception.BusinessException;
import com.honghung.chatapp.component.exception.types.FileException;
import com.honghung.chatapp.utils.ImageFileUtils;

import lombok.SneakyThrows;

@Service
public class LocalImageFileUploadService {
    private static final Path IMAGE_FOLDER = Paths.get("src/main/resources/static/images");

    public LocalImageFileUploadService() throws IOException {
        try {
            Files.createDirectories(IMAGE_FOLDER);
        } catch (IOException e) {
            throw new IOException("Could not create folder", e);
        }
    }

    @SneakyThrows
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw BusinessException.from(
                    FileException.EMPTY_FILE, "Failed to upload because file is empty");
        }
        if (!ImageFileUtils.isImageFile(file)) {
            throw BusinessException.from(
                    FileException.NOT_IMAGE_FILE,
                    "Failed to upload because file is not image file");
        }
        float fileSize = (float) file.getSize() / (1 * 1024 * 1024);
        if (fileSize > 5.0f) {
            throw BusinessException.from(
                    FileException.FILE_TOO_LARGE,
                    "Failed to upload because file size is too large, > 5MB");
        }
        String fileExtensionName = ImageFileUtils.getFileExtension(file);
        String generatedFileName = UUID.randomUUID().toString().replaceAll("-", "");
        String generatedFile = generatedFileName + "." + fileExtensionName;
        Path destinationPath = IMAGE_FOLDER.resolve(Paths.get(generatedFile)).normalize().toAbsolutePath();
        if (!destinationPath.getParent().equals(IMAGE_FOLDER.toAbsolutePath())) {
            throw new IOException("Cannot store file outside current directory");
        }
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        }
        return generatedFile;
    }

    // @Override
    // @SneakyThrows
    // public Stream<Path> loadAllFiles() {
    // return Files.walk(IMAGE_FOLDER, 1)
    // .filter(path -> !path.equals(IMAGE_FOLDER))
    // .map(IMAGE_FOLDER::relativize);
    // }

    // @Override
    // @SneakyThrows
    // public byte[] readFileContent(String fileName) {
    // try {
    // Path targetPath = IMAGE_FOLDER.resolve(fileName);
    // Resource resource = new UrlResource(targetPath.toUri());
    // if (resource.exists() || resource.isReadable()) {
    // return ImageFileUtils.readFileToByteArray(resource);
    // }
    // } catch (Exception e) {
    // throw new IOException("Could not read file: " + fileName, e);
    // }
    // throw new RuntimeException("Failed to read file: " + fileName);
    // }

    // @Override
    // public void deleteAllFiles() {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'deleteAllFiles'");
    // }

}
