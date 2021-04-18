/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.core.extensions;

import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.checker.NullityChecker.let;
import static io.art.core.constants.ArrayConstants.*;
import static java.lang.System.*;
import static java.util.Arrays.*;
import static java.util.Objects.*;

@UtilityClass
public class ArrayExtensions {
    public static <T> T[] concatArrays(T[] a1, T[] a2) {
        if (isNull(a1) || a1.length == 0) return a2;
        if (isNull(a2) || a2.length == 0) return a1;
        T[] res = copyOf(a1, a1.length + a2.length);
        arraycopy(a2, 0, res, a1.length, a2.length);
        return res;
    }

    public int[] unbox(Integer[] array) {
        if (isEmpty(array)) {
            return EMPTY_INTS;
        }
        int[] newArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }
    
    public char[] unbox(Character[] array) {
        if (isEmpty(array)) {
            return EMPTY_CHARS;
        }
        char[] newArray = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public long[] unbox(Long[] array) {
        if (isEmpty(array)) {
            return EMPTY_LONGS;
        }
        long[] newArray = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public float[] unbox(Float[] array) {
        if (isEmpty(array)) {
            return EMPTY_FLOATS;
        }
        float[] newArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public double[] unbox(Double[] array) {
        if (isEmpty(array)) {
            return EMPTY_DOUBLES;
        }
        double[] newArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public short[] unbox(Short[] array) {
        if (isEmpty(array)) {
            return EMPTY_SHORTS;
        }
        short[] newArray = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public byte[] unbox(Byte[] array) {
        if (isEmpty(array)) {
            return EMPTY_BYTES;
        }
        byte[] newArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public boolean[] unbox(Boolean[] array) {
        if (isEmpty(array)) {
            return EMPTY_BOOLEANS;
        }
        boolean[] newArray = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }


    public Character[] box(char[] array) {
        if (isEmpty(array)) {
            return EMPTY_CHAR_ARRAY;
        }
        Character[] newArray = new Character[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public Integer[] box(int[] array) {
        if (isEmpty(array)) {
            return EMPTY_INTEGER_ARRAY;
        }
        Integer[] newArray = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public Long[] box(long[] array) {
        if (isEmpty(array)) {
            return EMPTY_LONG_ARRAY;
        }
        Long[] newArray = new Long[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public Float[] box(float[] array) {
        if (isEmpty(array)) {
            return EMPTY_FLOAT_ARRAY;
        }
        Float[] newArray = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public Double[] box(double[] array) {
        if (isEmpty(array)) {
            return EMPTY_DOUBLE_ARRAY;
        }
        Double[] newArray = new Double[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public Short[] box(short[] array) {
        if (isEmpty(array)) {
            return EMPTY_SHORT_ARRAY;
        }
        Short[] newArray = new Short[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public Byte[] box(byte[] array) {
        if (isEmpty(array)) {
            return EMPTY_BYTE_ARRAY;
        }
        Byte[] newArray = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public Boolean[] box(boolean[] array) {
        if (isEmpty(array)) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        Boolean[] newArray = new Boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }
}
