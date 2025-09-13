package my.sts.ya_practicum.my_blog.back_app.controller;

import my.sts.ya_practicum.my_blog.back_app.service.ImageService;
import my.sts.ya_practicum.my_blog.back_app.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ImageController imageController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    public void shouldReturnStatusOk_whenUploadImage() throws Exception {
        MockMultipartFile mockImage = new MockMultipartFile(
                "image",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "fake-image-content".getBytes()
        );

        when(postService.exists(any())).thenReturn(true);

        mockMvc.perform(
                multipart("/api/posts/{id}/image", 42)
                        .file(mockImage)
                        .with(request -> {
                            request.setMethod("PUT");

                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().isOk());

        verify(imageService, times(1)).uploadImage(any(), any());
    }

    @Test
    void shouldReturnNotFound_whenPostDoesNotExist() throws Exception {
        Long postId = 42L;

        MockMultipartFile mockImage = new MockMultipartFile(
                "image",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "fake-image-content".getBytes()
        );

        when(postService.exists(postId)).thenReturn(false);

        mockMvc.perform(
                multipart("/api/posts/{id}/image", 42)
                        .file(mockImage)
                        .with(request -> {
                            request.setMethod("PUT");

                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().isNotFound());

        verify(imageService, never()).uploadImage(anyLong(), any());
    }

    @Test
    void shouldReturnImageBytes_whenImageExists() throws Exception {
        Long postId = 42L;
        byte[] imageBytes = "fake-image-bytes".getBytes();

        when(postService.exists(postId)).thenReturn(true);
        when(imageService.getImage(postId)).thenReturn(imageBytes);

        mockMvc.perform(get("/api/posts/{id}/image", postId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(imageBytes));
    }

    @Test
    void shouldReturnNotFoundWhenPostDoesNotExist() throws Exception {
        Long postId = 42L;

        when(postService.exists(postId)).thenReturn(false);

        mockMvc.perform(get("/api/posts/{id}/image", postId))
                .andExpect(status().isNotFound());

        verify(imageService, never()).getImage(anyLong());
    }

    @Test
    void shouldReturnNotFoundWhenImageIsEmpty() throws Exception {
        Long postId = 42L;

        when(postService.exists(postId)).thenReturn(true);
        when(imageService.getImage(postId)).thenReturn(null);

        mockMvc.perform(get("/api/posts/{id}/image", postId))
                .andExpect(status().isNotFound());
    }
}
