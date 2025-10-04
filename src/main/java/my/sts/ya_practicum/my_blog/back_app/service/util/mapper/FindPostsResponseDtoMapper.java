package my.sts.ya_practicum.my_blog.back_app.service.util.mapper;

import my.sts.ya_practicum.my_blog.back_app.web.dto.FindPostsResponseDto;
import my.sts.ya_practicum.my_blog.back_app.web.dto.PostDto;
import my.sts.ya_practicum.my_blog.back_app.persistence.model.Post;

import java.util.List;
import java.util.Map;

public class FindPostsResponseDtoMapper {

    public static FindPostsResponseDto map(
            List<Post> posts,
            Map<Long, Long> postCommentsCounts,
            int postsTotalCount,
            int pageNumber,
            int pageSize
    ) {
        List<PostDto> postDtos = PostDtoMapper.map(posts, postCommentsCounts);
        int lastPage = (int) Math.ceil((double) postsTotalCount / pageSize);

        FindPostsResponseDto responseDto = new FindPostsResponseDto();

        responseDto.setPosts(postDtos);
        responseDto.setHasPrev(pageNumber > 1);
        responseDto.setHasNext(pageNumber < lastPage);
        responseDto.setLastPage(lastPage);

        return responseDto;
    }
}
