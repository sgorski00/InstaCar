package pl.studia.InstaCar.service;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileUploadServiceTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private FileUploadService fileUploadService;

    @Mock
    private MultipartFile file;

    @BeforeEach
    void setUp() throws Exception {
        Field field = FileUploadService.class.getDeclaredField("uploadDirectory");
        field.setAccessible(true);
        field.set(fileUploadService, "src/test/resources/static/temp");
    }

    @Test
    void shouldUploadFileInCorrectPathWhenJpg() throws Exception {
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getOriginalFilename()).thenReturn("test.jpg");

        String result = fileUploadService.uploadFile(file);

        verify(file, times(1)).transferTo(any(Path.class));
        assertTrue(result.startsWith("\\temp\\"));
        assertTrue(result.endsWith(".jpg"));
    }

    @Test
    void shouldUploadFileInCorrectPathWhenPng() throws Exception {
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");
        when(file.getOriginalFilename()).thenReturn("test.png");

        String result = fileUploadService.uploadFile(file);

        verify(file, times(1)).transferTo(any(Path.class));
        assertTrue(result.startsWith("\\temp\\"));
        assertTrue(result.endsWith(".png"));
    }
    @Test
    void shouldThrowWhileUploadingWhenNotImage() throws Exception {
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("text/plain");
        when(file.getOriginalFilename()).thenReturn("test.txt");

        assertThrows(FileUploadException.class, () -> fileUploadService.uploadFile(file));
        verify(messageSource, times(1)).getMessage(eq("error.file.not.image"), nullable(Object[].class), any(Locale.class));
        verify(file, times(0)).transferTo(any(Path.class));
    }

    @Test
    void shouldThrowWhileUploadingWhenEmptyFile() throws Exception {
        when(file.isEmpty()).thenReturn(true);

        assertThrows(FileUploadException.class, () -> fileUploadService.uploadFile(file));
        verify(messageSource, times(1)).getMessage(eq("error.file.empty"), nullable(Object[].class), any(Locale.class));
        verify(file, times(0)).transferTo(any(Path.class));
    }

    @Test
    void shouldThrowWhileUploadingWhenFileWithoutExtension() throws Exception {
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("text/plain");
        when(file.getOriginalFilename()).thenReturn("test");

        assertThrows(FileUploadException.class, () -> fileUploadService.uploadFile(file));
        verify(messageSource, times(1)).getMessage(eq("error.file.general"), nullable(Object[].class), any(Locale.class));
        verify(file, times(0)).transferTo(any(Path.class));
    }
}
