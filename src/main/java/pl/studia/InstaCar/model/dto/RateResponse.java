package pl.studia.InstaCar.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateResponse {

    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;

    public double getNewestPrice(double price) {
        NumberFormat nf = new DecimalFormat("#0.00");
        nf.setRoundingMode(RoundingMode.HALF_EVEN);
        double result = Double.parseDouble(rates.getLast().getMid()) * price;
        return Double.parseDouble(nf.format(result));
    }
}
