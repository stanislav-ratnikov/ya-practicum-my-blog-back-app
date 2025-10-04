package my.sts.ya_practicum.my_blog.back_app.service.search;

import java.util.List;

public record PostSearchCriteria(String search, List<String> tags) {
}
