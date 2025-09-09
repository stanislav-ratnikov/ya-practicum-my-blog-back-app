package my.sts.ya_practicum.my_blog.back_app.util.search;

import java.util.List;

public record PostSearchCriteria(String searchSubString, List<String> tags) {
}
