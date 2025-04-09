package pl.studia.InstaCar.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rate {

    private String no;
    private String effectiveDate;
    private String mid;
}
