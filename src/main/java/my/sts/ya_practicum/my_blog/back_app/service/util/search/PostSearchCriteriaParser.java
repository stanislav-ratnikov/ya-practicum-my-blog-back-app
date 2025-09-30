package my.sts.ya_practicum.my_blog.back_app.service.util.search;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PostSearchCriteriaParser {

    public static PostSearchCriteria parse(String searchString) {
        if (searchString == null) {
            return new PostSearchCriteria(null, null);
        }

        String[] words = searchString.split("\\s+");

        String searchSubString = Arrays.stream(words)
                .filter(word -> !word.startsWith("#"))
                .collect(Collectors.joining(" "));

        List<String> tags = Arrays.stream(words)
                .filter(word -> word.startsWith("#"))
                .map(word -> word.substring(1))
                .toList();

        return new PostSearchCriteria(
                searchSubString.isEmpty() ? null : searchSubString,
                tags.isEmpty() ? null : tags
        );
    }
}
