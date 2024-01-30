package org.example;


import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class Book
{
    public String ISBN;
    public String TITLE;
    public String STATE;
    public String LOCALIZER;

}
