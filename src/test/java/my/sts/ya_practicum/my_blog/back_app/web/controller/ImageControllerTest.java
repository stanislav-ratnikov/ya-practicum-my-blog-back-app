package my.sts.ya_practicum.my_blog.back_app.web.controller;

import my.sts.ya_practicum.my_blog.back_app.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
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

        mockMvc.perform(
                multipart("/api/posts/{id}/image", 42)
                        .file(mockImage)
                        .with(request -> {
                            request.setMethod("PUT");

                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().isNoContent());

        verify(imageService, times(1)).uploadImage(any(), any());
    }

    @Test
    void shouldReturnImageBytes_whenGetImage() throws Exception {
        Long postId = 42L;
        byte[] imageBytes = "fake-image-bytes".getBytes();

        when(imageService.getImage(postId)).thenReturn(imageBytes);

        mockMvc.perform(get("/api/posts/{id}/image", postId))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(imageBytes));
    }
}
