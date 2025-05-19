package pl.studia.InstaCar.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListPaginatorTest {

    private ListPaginator<Integer> paginator;
    private List<Integer> sampleList;

    @BeforeEach
    void setUp() {
        paginator = new ListPaginator<>();
        sampleList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    @Test
    void shouldCreateCorrectSubList() {
        List<Integer> result = paginator.getPaginatedList(sampleList, 2, 3);
        assertEquals(Arrays.asList(4, 5, 6), result);
    }

    @Test
    void shouldReturnEmptyListWhenPageOutOfBounds() {
        List<Integer> result = paginator.getPaginatedList(sampleList, 5, 10);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnLastElementsIfRemainingLessThanPageSize() {
        List<Integer> result = paginator.getPaginatedList(sampleList, 4, 3);
        assertEquals(List.of(10), result);
    }

    @Test
    void shouldReturnEmptyListWhenListIsEmpty() {
        List<Integer> result = paginator.getPaginatedList(Collections.emptyList(), 1, 5);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenPageSizeIsZero() {
        List<Integer> result = paginator.getPaginatedList(sampleList, 1, 0);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenPageIsZero() {
        List<Integer> result = paginator.getPaginatedList(sampleList, 0, 3);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenPageIsNegative() {
        List<Integer> result = paginator.getPaginatedList(sampleList, -1, 3);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenPageSizeIsNegative() {
        List<Integer> result = paginator.getPaginatedList(sampleList, 1, -2);
        assertTrue(result.isEmpty());
    }
}
