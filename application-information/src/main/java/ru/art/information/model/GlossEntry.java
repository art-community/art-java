
package ru.art.information.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@SuppressWarnings("unused")
public class GlossEntry {

    private String abbrev;
    private String acronym;
    private GlossDef glossDef;
    private String glossSee;
    private String glossTerm;
    private String iD;
    private String sortAs;

}
