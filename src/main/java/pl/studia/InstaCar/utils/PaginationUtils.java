package pl.studia.InstaCar.utils;

public class PaginationUtils {

    /**
     * Oblicza numery stron do wyświetlenia w paginacji
     *
     * @param currentPage  aktualna strona (widoczna dla użytkownika, liczona od 1)
     * @param visiblePages liczba widocznych stron w paginacji (np. 5)
     * @param totalPages   całkowita liczba stron
     * @return tablica int[] zawierająca numery stron do wyświetlenia
     */
    public static int[] getPageNumbers(int currentPage, int visiblePages, int totalPages) {
        currentPage = Math.max(1, currentPage);
        totalPages = Math.max(1, totalPages);
        visiblePages = Math.max(1, visiblePages);

        int radius = visiblePages / 2;

        int startPage = Math.max(1, currentPage - radius);
        int endPage = Math.min(totalPages, startPage + visiblePages - 1);

        if (endPage - startPage + 1 < visiblePages) {
            startPage = Math.max(1, endPage - visiblePages + 1);
        }

        int[] pages = new int[Math.min(visiblePages, totalPages)];
        for (int i = 0; i < pages.length; i++) {
            pages[i] = startPage + i;
        }

        return pages;
    }
}