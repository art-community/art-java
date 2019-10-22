
package ru.art.information.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@SuppressWarnings("unused")
public class Glossary {

    private GlossDiv glossDiv;
    private String title;

}
