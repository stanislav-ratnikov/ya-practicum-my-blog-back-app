package my.sts.ya_practicum.my_blog.back_app.util.search;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PostSearchCriteriaParserTest {

    @Test
    public void givenNull_whenParse_thenReturnNotNulCriteria() {
        PostSearchCriteria searchCriteria = PostSearchCriteriaParser.parse(null);

        assertNotNull(searchCriteria);
        assertNull(searchCriteria.search());
        assertNull(searchCriteria.tags());
    }

    @Test
    public void givenEmptyString_whenParse_thenReturnNotNulCriteria() {
        PostSearchCriteria searchCriteria = PostSearchCriteriaParser.parse("");

        assertNotNull(searchCriteria);
        assertNull(searchCriteria.search());
        assertNull(searchCriteria.tags());
    }

    @Test
    public void givenEmptyStringWithWhiteSpace_whenParse_thenReturnNotNulCriteria() {
        PostSearchCriteria searchCriteria = PostSearchCriteriaParser.parse("   ");

        assertNotNull(searchCriteria);
        assertNull(searchCriteria.search());
        assertNull(searchCriteria.tags());
    }

    @ParameterizedTest
    @MethodSource("validTestCases")
    public void givenValidString_whenParse_thenReturnValidResult(
            String inputString,
            String expectedSearch,
            List<String> expectedTags
    ) {
        PostSearchCriteria searchCriteria = PostSearchCriteriaParser.parse(inputString);

        assertNotNull(searchCriteria);
        assertEquals(expectedSearch, searchCriteria.search());
        assertIterableEquals(expectedTags, searchCriteria.tags());
    }

    private static Stream<Arguments> validTestCases() {
        return Stream.of(
                Arguments.of("text1", "text1", null),
                Arguments.of("#tag1", null, List.of("tag1")),
                Arguments.of("text1text2", "text1text2", null),
                Arguments.of("#tag1tag2", null, List.of("tag1tag2")),
                Arguments.of("text1 text2", "text1 text2", null),
                Arguments.of("#tag1 #tag2", null, List.of("tag1", "tag2")),
                Arguments.of("text1 #tag1 text2", "text1 text2", List.of("tag1")),
                Arguments.of("#tag1 text1 #tag2", "text1", List.of("tag1", "tag2")),
                Arguments.of("text1 #tag1 text2 #tag2", "text1 text2", List.of("tag1", "tag2"))
        );
    }
}
