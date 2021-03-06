package org.osgl.mvc.result;

/*-
 * #%L
 * OSGL MVC
 * %%
 * Copyright (C) 2014 - 2017 OSGL (Open Source General Library)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.osgl.http.Http;

public final class Ok extends Result {

    public static Ok INSTANCE = new Ok();

    private Ok() {
        super(Http.Status.OK, "200 Ok");
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof Ok;
    }

    @Override
    public String toString() {
        return "HTTP/1.1 200 OK";
    }

    public static Ok get() {
        return INSTANCE;
    }
}
