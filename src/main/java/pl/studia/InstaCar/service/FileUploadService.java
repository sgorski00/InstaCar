package pl.studia.InstaCar.service;

import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Log4j2
@Service
public class FileUploadService {

    @Value("${upload.directory}")
    private String uploadDirectory;

    private final MessageSource messageSource;

    @Autowired
    public FileUploadService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Uploads the file to the uploadDirectory path adn returns a saved path with file name.
     *
     * @param file the file to save
     * @return a unique saved file name
     * @throws FileUploadException when file is empty, not an image or any other error occurred
     */
    public String uploadFile(MultipartFile file) throws FileUploadException {
        log.debug("Plik wgrany: {}", file.getOriginalFilename());
        log.debug("Rozmiar pliku: {}", file.getSize());
        String message;
        if(file.isEmpty()) {
            message = messageSource.getMessage("error.file.empty", null, LocaleContextHolder.getLocale());
            throw new FileUploadException(message);
        }

        try {
            String content = file.getContentType();
            if(content == null || !content.startsWith("image")) {
                message = messageSource.getMessage("error.file.not.image", null, LocaleContextHolder.getLocale());
                throw new FileUploadException(message);
            }

            //noinspection DataFlowIssue
            String ext = getExtension(file.getOriginalFilename());
            String uniqueName = getUniqueName(ext);
            Path dest = Paths.get(uploadDirectory, uniqueName);

            file.transferTo(dest);

            return getUploadDir(dest.toString());
        }catch (NullPointerException | IOException e) {
            message = messageSource.getMessage("error.file.general", null, LocaleContextHolder.getLocale());
            throw new FileUploadException(message);
        }
    }

    /**
     * Returns a unique name with the given extension. The name is generated
     * using {@link UUID#randomUUID()} and appended with the given extension.
     *
     * @param extension the extension to append
     * @return a unique name with the given extension
     */
    private static String getUniqueName(String extension) {
        return UUID.randomUUID() + extension;
    }


    /**
     * Returns a path starting from the static directory (but not including it).
     * So if the full path is /static/uploads/name.ext, it will return /uploads/name.ext
     *
     * @param fullPath a full path to a file
     * @return a path starting from the static directory (but not including it)
     */
    private String getUploadDir(String fullPath) {
        return fullPath.substring(fullPath.indexOf("static") + "static".length());
    }

    /**
     * Returns the extension of a given file name with "." sign.
     *
     * @param fileName a file name
     * @return the extension of the file name
     */
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
