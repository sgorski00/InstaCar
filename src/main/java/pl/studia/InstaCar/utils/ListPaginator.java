package pl.studia.InstaCar.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ListPaginator<T> {

    /**
     * Returns a sublist containing the elements of the given list for the specified page and size.
     * Useful for cached methods. If not there is no need, it is better to use pagination inside the repository.
     *
     * @param list the complete list of items to paginate
     * @param page the page number to retrieve (1-based index)
     * @param pageSize the number of items per page
     * @return a list containing the items for the specified page
     * @throws IndexOutOfBoundsException if the start index is out of range
     * @throws IllegalArgumentException if page size is negative
     */
    public List<T> getPaginatedList(List<T> list, int page, int pageSize) throws IndexOutOfBoundsException, IllegalArgumentException {
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, list.size());
        try{
            return list.subList(start, end);
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }
}
