
package ru.art.information.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@SuppressWarnings("unused")
public class GlossDiv {

    private GlossList glossList;
    private String title;

}
