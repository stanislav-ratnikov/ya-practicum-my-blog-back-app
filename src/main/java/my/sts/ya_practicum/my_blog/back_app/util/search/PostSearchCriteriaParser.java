package my.sts.ya_practicum.my_blog.back_app.util.search;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PostSearchCriteriaParser {

    public static PostSearchCriteria parse(String searchString) {
        if (searchString == null) {
            return null;
        }

        String[] words = searchString.split("\\s+");

        String searchSubString = Arrays.stream(words)
                .filter(word -> !word.startsWith("#"))
                .collect(Collectors.joining(" "));

        List<String> tags = Arrays.stream(words)
                .filter(word -> word.startsWith("#"))
                .toList();

        return new PostSearchCriteria(searchSubString, tags);
    }
}
