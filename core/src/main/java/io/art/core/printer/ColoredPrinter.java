/*
 * ART
 *
 * Copyright 2020 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.core.printer;

import io.art.core.colorizer.*;
import lombok.*;
import static com.google.common.base.Strings.*;
import static io.art.core.constants.StringConstants.*;

@NoArgsConstructor(staticName = "printer")
public class ColoredPrinter {
    private final StringBuilder builder = new StringBuilder();
    private int tabulation = 0;

    public ColoredPrinter success(String message) {
        builder.append(repeat(TABULATION, tabulation)).append(AnsiColorizer.success(message)).append(NEW_LINE);
        return this;
    }

    public ColoredPrinter error(String message) {
        builder.append(repeat(TABULATION, tabulation)).append(AnsiColorizer.error(message)).append(NEW_LINE);
        return this;
    }

    public ColoredPrinter warning(String message) {
        builder.append(repeat(TABULATION, tabulation)).append(AnsiColorizer.warning(message)).append(NEW_LINE);
        return this;
    }

    public ColoredPrinter additional(String message) {
        builder.append(repeat(TABULATION, tabulation)).append(AnsiColorizer.additional(message)).append(NEW_LINE);
        return this;
    }

    public ColoredPrinter success(String name, Object value) {
        return success(name + COLON + SPACE + value);
    }

    public ColoredPrinter error(String name, Object value) {
        return error(name + COLON + SPACE + value);
    }

    public ColoredPrinter warning(String name, Object value) {
        return warning(name + COLON + SPACE + value);
    }

    public ColoredPrinter additional(String name, Object value) {
        return additional(name + COLON + SPACE + value);
    }

    public ColoredPrinter tabulation(int count) {
        tabulation = count;
        return this;
    }

    public String print() {
        return builder.toString();
    }
}
