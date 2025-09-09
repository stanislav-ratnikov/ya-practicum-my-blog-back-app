package my.sts.ya_practicum.my_blog.back_app.service;

import jakarta.servlet.ServletContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    public static final String UPLOAD_DIR = "uploads/";

    private final ServletContext servletContext;

    public ImageService(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public byte[] getImage(Long postId) {
        try {
            Path filePath = Paths.get(servletContext.getRealPath(UPLOAD_DIR)).resolve(postId.toString()).normalize();

            if (!filePath.toFile().exists()) {
                return null;
            }

            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            e.printStackTrace();

            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void uploadImage(Long postId, MultipartFile file) {
        try {
            Path uploadDir = Paths.get(servletContext.getRealPath(UPLOAD_DIR));

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(postId.toString());

            file.transferTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();

            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
