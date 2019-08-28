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

package ru.art.example.api.interceptor.http;

import org.apache.http.client.methods.*;
import ru.art.core.constants.*;
import ru.art.http.client.interceptor.*;
import static ru.art.core.constants.CharConstants.*;
import static ru.art.core.constants.InterceptionStrategy.*;
import java.util.*;

public class ExampleHttpClientInterception implements HttpClientRequestInterception {

    /**
     * Client http interceptor can add some data into request
     * for example can add header for basic authorization
     *
     * @param httpRequest - contains some data of http request
     * @return NEXT_INTERCEPTOR policy for other interceptors
     */

    @Override
    public InterceptionStrategy intercept(HttpUriRequest httpRequest) {
        String login = "login";
        String password = "password";
        String encodedLoginPass = Base64.getEncoder().encodeToString((login + COLON + password).getBytes());

        httpRequest.addHeader("Authorization", "Basic " + encodedLoginPass);
        return NEXT_INTERCEPTOR;
    }
}