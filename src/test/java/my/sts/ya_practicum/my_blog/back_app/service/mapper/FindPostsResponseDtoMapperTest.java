package my.sts.ya_practicum.my_blog.back_app.service.mapper;

import my.sts.ya_practicum.my_blog.back_app.web.dto.FindPostsResponseDto;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FindPostsResponseDtoMapperTest {

    @Test
    void map_shouldReturnValidResponseDto_withCorrectPaginationValues() {
        FindPostsResponseDto responseDto = FindPostsResponseDtoMapper.map(
                List.of(),
                new HashMap<>(),
                42,
                2,
                5
        );

        assertNotNull(responseDto);
        assertNotNull(responseDto.getPosts());
        assertTrue(responseDto.getHasPrev());
        assertTrue(responseDto.getHasNext());
        assertEquals(9, responseDto.getLastPage());
    }
}
