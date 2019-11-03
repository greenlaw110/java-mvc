package org.osgl.mvc;

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

import org.osgl.$;
import org.osgl.http.H;
import org.osgl.http.HttpConfig;
import org.osgl.util.C;

import static org.osgl.mvc.MvcConfig.*;

/**
 * The {@code HttpContextManager} help to set up http context,
 * e.g. current request, response, session, flash. It also
 * provides method to clean up the context
 */
public class HttpContextManager {

    public interface Listener {
        void onSessionResolved(H.Session session);
        void onCleanUp();
    }

    private static final C.List<Listener> listeners = C.newList();

    private static String sessionKey() {
        return MvcConfig.sessionCookieName;
    }

    private static String flashKey() {
        return MvcConfig.flashCookieName;
    }

    /**
     * Initialize the Http Context. This will set up current context for
     * request, response, session (resolved from cookie) and flash (resolved
     * from cookie)
     *
     * @param request  the http request
     * @param response the http response
     */
    public static void init(H.Request request, H.Response response) {
        H.Request.current(request);
        H.Response.current(response);
        resolveSession(request);
        resolveFlash(request);
    }

    /**
     * Persist session and flash to cookie, write all cookies to http response
     */
    public static void save() {
        H.Response resp = H.Response.current();

        H.Session session = H.Session.current();
        serialize(session);

        H.Flash flash = H.Flash.current();
        serialize(flash);
    }

    /**
     * Clean up context, release thread local etc
     */
    public static void cleanUp() {
        H.cleanUp();
        listeners.accept(F.ON_SESSION_CLEAN_UP);
    }

    private static void resolveSession(H.Request request) {
        H.Cookie cookie = request.cookie(sessionCookieName);
        H.Session session = H.Session.resolve(cookie, sessionExpire);
        H.Session.current(session);
    }

    private static void resolveFlash(H.Request request) {
        H.Cookie cookie = request.cookie(flashCookieName);
        H.Flash.current(H.Flash.resolve(cookie));
    }

    private static void serialize(H.Session session) {
        saveCookie(session.serialize(sessionKey()));
    }

    private static void serialize(H.Flash flash) {
        saveCookie(flash.serialize(flashKey()));
    }

    private static void saveCookie(H.Cookie cookie) {
        cookie.path(HttpConfig.contextPath()).secure(HttpConfig.isCookieSecure());
        H.Response resp = H.Response.current();
        resp.addCookie(cookie);
    }

    private enum F {
        ;
        public static $.Visitor<Listener> onSessionResolved(final H.Session session) {
            return new $.Visitor<Listener>() {
                @Override
                public void visit(Listener listener) throws $.Break {
                    listener.onSessionResolved(session);
                }
            };
        }

        public static $.Visitor<Listener> ON_SESSION_CLEAN_UP = new $.Visitor<Listener>() {
            @Override
            public void visit(Listener listener) throws $.Break {
                listener.onCleanUp();
            }
        };
    }
}
