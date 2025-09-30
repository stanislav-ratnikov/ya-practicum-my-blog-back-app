package my.sts.ya_practicum.my_blog.back_app.web.controller;

import my.sts.ya_practicum.my_blog.back_app.service.ImageService;
import my.sts.ya_practicum.my_blog.back_app.service.PostService;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts")
public class ImageController {

    private final PostService postService;
    private final ImageService imageService;

    public ImageController(PostService postService, ImageService imageService) {
        this.postService = postService;
        this.imageService = imageService;
    }

    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getPostImage(@PathVariable("id") Long postId) {
        if (!postService.exists(postId)) {
            return ResponseEntity.notFound().build();
        }

        byte[] bytes = imageService.getImage(postId);

        if (bytes == null || bytes.length == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .cacheControl(CacheControl.noStore())
                .body(bytes);
    }

    @PutMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<Void> uploadImage(@PathVariable("id") Long postId, @RequestParam("image") MultipartFile file) {
        if (!postService.exists(postId)) {
            return ResponseEntity.notFound().build();
        }

        imageService.uploadImage(postId, file);

        return ResponseEntity.noContent().build();
    }
}
