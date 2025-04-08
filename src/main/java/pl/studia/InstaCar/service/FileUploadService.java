package pl.studia.InstaCar.service;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${upload.directory}")
    private String uploadDirectory;

    public String uploadFile(MultipartFile file) throws FileUploadException {
        if(file.isEmpty()) {
            throw new FileUploadException("Plik jest pusty");
        }

        try {
            //noinspection DataFlowIssue
            String ext = getExtension(file.getOriginalFilename());
            String uniqueName = getUniqueName(ext);
            Path dest = Paths.get(uploadDirectory, uniqueName);

            file.transferTo(dest);

            return getUploadDir(dest.toString());
        }catch (NullPointerException | IOException e) {
            throw new FileUploadException("Przesłano nieprawidłowy plik");
        }
    }

    private static String getUniqueName(String extension) {
        return UUID.randomUUID() + extension;
    }

    private String getUploadDir(String fullPath) {
        return fullPath.substring(fullPath.indexOf("static") + "static".length());
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
