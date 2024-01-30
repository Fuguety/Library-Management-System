package org.example;


import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder(setterPrefix = "with")
public class Borrow {
    private String locationCode;
    private String isbn;
    private String memberCode;
    private Date loanDate;
    private Date returnDate;
}
