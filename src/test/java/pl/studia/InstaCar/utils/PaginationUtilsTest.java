package pl.studia.InstaCar.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class PaginationUtilsTest {

    @Test
    void shouldCreateCorrectPages_Start1_Size5_WhenThereIsMore() {
        int currentPage = 1;
        int visiblePages = 5;
        int totalPages = 100;

        int[] expected = new int[]{1, 2, 3, 4, 5};
        int[] actual = PaginationUtils.getPageNumbers(currentPage, visiblePages, totalPages);

        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldCreateCorrectPages_Start1_Size5_WhenThereIsEqual() {
        int currentPage = 1;
        int visiblePages = 5;
        int totalPages = 5;

        int[] expected = new int[]{1, 2, 3, 4, 5};
        int[] actual = PaginationUtils.getPageNumbers(currentPage, visiblePages, totalPages);

        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldCreateCorrectPages_Start1_Size5_WhenThereIsLess() {
        int currentPage = 1;
        int visiblePages = 5;
        int totalPages = 3;

        int[] expected = new int[]{1, 2, 3};
        int[] actual = PaginationUtils.getPageNumbers(currentPage, visiblePages, totalPages);

        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldCreateCorrectPages_Start6_Size10_WhenThereIsMore() {
        int currentPage = 11;
        int visiblePages = 10;
        int totalPages = 30;

        int[] expected = new int[]{6,7,8,9,10,11,12,13,14,15};
        int[] actual = PaginationUtils.getPageNumbers(currentPage, visiblePages, totalPages);

        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldCreateCorrectPages_From11To20_WhenThereIsEqual() {
        int currentPage = 11;
        int visiblePages = 10;
        int totalPages = 15;

        int[] expected = new int[]{6,7,8,9,10,11,12,13,14,15};
        int[] actual = PaginationUtils.getPageNumbers(currentPage, visiblePages, totalPages);

        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldCreateCorrectPages_From11To20_WhenThereIsLess() {
        int currentPage = 11;
        int visiblePages = 10;
        int totalPages = 12;

        int[] expected = new int[]{3,4,5,6,7,8,9,10,11,12};
        int[] actual = PaginationUtils.getPageNumbers(currentPage, visiblePages, totalPages);

        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldCreateCorrectPages_Start1_Size1_WhenThereIsMore() {
        int currentPage = 1;
        int visiblePages = 1;
        int totalPages = 100;

        int[] expected = new int[]{1};
        int[] actual = PaginationUtils.getPageNumbers(currentPage, visiblePages, totalPages);

        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldCreateCorrectPages_Start1_Size100_WhenThereIsLess() {
        int currentPage = 1;
        int visiblePages = 100;
        int totalPages = 1;

        int[] expected = new int[]{1};
        int[] actual = PaginationUtils.getPageNumbers(currentPage, visiblePages, totalPages);

        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldCreateCorrectPages_Start50_Size100_WhenThereIsLess() {
        int currentPage = 50;
        int visiblePages = 100;
        int totalPages = 1;

        int[] expected = new int[]{1};
        int[] actual = PaginationUtils.getPageNumbers(currentPage, visiblePages, totalPages);

        assertArrayEquals(expected, actual);
    }
}
