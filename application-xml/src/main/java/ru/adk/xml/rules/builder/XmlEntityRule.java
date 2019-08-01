package ru.adk.xml.rules.builder;

import lombok.Builder;
import lombok.Getter;
import static java.util.Collections.disjoint;
import java.util.Set;

@Getter
@Builder
public class XmlEntityRule {
    private final Set<String> inputs;
    private final String output;

    boolean hasIntersectionWithInputsOrOutputs(Set<String> inputs, String output) {
        return !disjoint(inputs, this.inputs) ||
                inputs.contains(output) ||
                inputs.contains(this.output) ||
                this.inputs.contains(output);
    }

}
