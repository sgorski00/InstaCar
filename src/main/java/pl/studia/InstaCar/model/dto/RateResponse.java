package pl.studia.InstaCar.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateResponse {

    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;
}
