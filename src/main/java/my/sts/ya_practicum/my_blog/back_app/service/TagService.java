package my.sts.ya_practicum.my_blog.back_app.service;

import my.sts.ya_practicum.my_blog.back_app.dao.TagRepository;
import my.sts.ya_practicum.my_blog.back_app.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<String> getTagsByPostId(Long postId) {
        List<Tag> tags = tagRepository.findByPostId(postId);

        return tags.stream()
                .map(Tag::getName)
                .toList();
    }

    public Map<Long, List<String>> getTagsByPostIds(List<Long> postIds) {
        List<Tag> tags = tagRepository.findByPostIds(postIds);

        return tags.stream()
                .collect(Collectors.groupingBy(
                        Tag::getPostId,
                        Collectors.mapping(Tag::getName, Collectors.toList())
                ));
    }
}
