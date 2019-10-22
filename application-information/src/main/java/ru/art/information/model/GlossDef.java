
package ru.art.information.model;

import lombok.*;
import java.util.*;

@Data
@Builder
@SuppressWarnings("unused")
public class GlossDef {

    private List<String> glossSeeAlso;
    private String para;

}
