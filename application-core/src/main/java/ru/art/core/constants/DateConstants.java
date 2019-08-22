/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.core.constants;

import java.text.*;

public interface DateConstants {
    String YYYY_MM_DD_HH_MM_SS_24H_DASH = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat YYYY_MM_DD_HH_MM_SS_24H_DASH_FORMAT = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_24H_DASH);

    String YYYY_MM_DD_T_HH_MM_SS_24H_DASH = "yyyy-MM-dd'T'HH:mm:ss";
    SimpleDateFormat YYYY_MM_DD_T_HH_MM_SS_24H_DASH_FORMAT = new SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SS_24H_DASH);

    String YYYY_MM_DD_T_HH_MM_SS_24H_Z_DASH = "yyyy-MM-dd'T'HH:mm:ssZ";
    SimpleDateFormat YYYY_MM_DD_T_HH_MM_SS_24H_Z_DASH_FORMAT = new SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SS_24H_Z_DASH);

    String YYYY_MM_DD_T_HH_MM_SS_24H_SSS_DASH = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    SimpleDateFormat YYYY_MM_DD_T_HH_MM_SS_24H_SSS_DASH_FORMAT = new SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SS_24H_SSS_DASH);

    String YYYY_MM_DD_T_HH_MM_SS_24H_SSS_Z_DASH = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    SimpleDateFormat YYYY_MM_DD_T_HH_MM_SS_24H_SSS_Z_DASH_FORMAT = new SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SS_24H_SSS_Z_DASH);

    String YYYY_MM_DD_T_HH_MM_SS_SSSXXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    SimpleDateFormat YYYY_MM_DD_T_HH_MM_SS_SSSXXX_FORMAT = new SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SS_SSSXXX);

    String YYYY_MM_DD_DASH = "yyyy-MM-dd";
    SimpleDateFormat YYYY_MM_DD_DASH_FORMAT = new SimpleDateFormat(YYYY_MM_DD_DASH);

    String YYYY_MM_DD_HH_MM_SS_24H_Z_DOT = "yyyy.MM.dd HH:mm:ss Z";
    SimpleDateFormat YYYY_MM_DD_HH_MM_SS_24H_Z_DOT_FORMAT = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_24H_Z_DOT);

    String DD_MM_YYYY_HH_MM_24H_DOT = "dd.MM.yyyy HH:mm";
    SimpleDateFormat DD_MM_YYYY_HH_MM_24H_DOT_FORMAT = new SimpleDateFormat(DD_MM_YYYY_HH_MM_24H_DOT);

    String DD_MM_YYYY_HH_MM_SS_DOT = "dd.MM.yyyy hh:mm:ss";
    SimpleDateFormat DD_MM_YYYY_HH_MM_SS_DOT_FORMAT = new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS_DOT);

    String DD_MM_YYYY_HH_MM_SS_24H_DOT = "dd.MM.yyyy HH:mm:ss";
    SimpleDateFormat DD_MM_YYYY_HH_MM_SS_24H_DOT_FORMAT = new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS_DOT);

    String DD_MM_YYYY_HH_MM_SS_24H_SLASH = "dd/MM/yyyy HH:mm:ss";
    SimpleDateFormat DD_MM_YYYY_HH_MM_SS_24H_SLASH_FORMAT = new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS_24H_SLASH);

    String DD_MM_YYYY_HH_MM_SS_24H_DASH = "dd-MM-yyyy HH:mm:ss";
    SimpleDateFormat DD_MM_YYYY_HH_MM_SS_24H_DASH_FORMAT = new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS_24H_DOT);

    String YYYYMM = "yyyyMM";
    SimpleDateFormat YYYYMM_FORMAT = new SimpleDateFormat(YYYYMM);

    String YYYY = "yyyy";
    SimpleDateFormat YYYY_FORMAT = new SimpleDateFormat(YYYY);

    String DD_MM_DOT = "dd.MM";
    SimpleDateFormat DD_MM_DOT_FORMAT = new SimpleDateFormat(DD_MM_DOT);

    String DD_MM_YYYY_DOT = "dd.MM.yyyy";
    SimpleDateFormat DD_MM_YYYY_DOT_FORMAT = new SimpleDateFormat(DD_MM_YYYY_DOT);

    String DD_MM_YYYY_EEEE_DOT = "dd.MM.yyyy EEEE";
    SimpleDateFormat DD_MM_YYYY_EEEE_DOT_FORMAT = new SimpleDateFormat(DD_MM_YYYY_EEEE_DOT);

    String DD_MM_YYYY_DASH = "dd-MM-yyyy";
    SimpleDateFormat DD_MM_YYYY_DASH_FROMAT = new SimpleDateFormat(DD_MM_YYYY_DASH);

    String DD_MM_DASH = "dd-MM";
    SimpleDateFormat DD_MM_DASH_FORMAT = new SimpleDateFormat(DD_MM_DASH);

    String HH_MM_SS_24H = "HH:mm:ss";
    SimpleDateFormat HH_MM_SS_24H_FORMAT = new SimpleDateFormat(HH_MM_SS_24H);

    String HH_MM_24H = "HH:mm";
    SimpleDateFormat HH_MM_24H_FORMAT = new SimpleDateFormat(HH_MM_24H);

    String HH = "HH";
    SimpleDateFormat HH_FORMAT = new SimpleDateFormat(HH);

    String MMMMM_YYYY = "MMMMM yyyy";
    SimpleDateFormat MMMMM_YYYY_FORMAT = new SimpleDateFormat(MMMMM_YYYY);

    String EEE_MMM_D_HH_MM_SS_Z_YYYY = "EEE MMM d HH:mm:ss z yyyy";
    SimpleDateFormat EEE_MMM_D_HH_MM_SS_Z_YYYY_FORMAT = new SimpleDateFormat(EEE_MMM_D_HH_MM_SS_Z_YYYY);

    int MONTHS_IN_YEAR = 12;
    int MONTHS_IN_QUARTER = 3;
    int QUARTERS_COUNT = 4;
    String TIME_ZONE_GMT = "GMT";
}
