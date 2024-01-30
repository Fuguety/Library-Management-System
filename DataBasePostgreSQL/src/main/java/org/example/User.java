package org.example;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class User
{
    private String Code;
    private String Name;
    private String Id;
}
