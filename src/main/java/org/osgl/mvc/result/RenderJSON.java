package org.osgl.mvc.result;

import org.osgl.http.H;
import org.osgl.mvc.MvcConfig;
import org.osgl.util.S;

public class RenderJSON extends RenderContent {

    private static RenderJSON _INSTANCE = new RenderJSON() {
        @Override
        public String content() {
            return payload().message;
        }
    };

    private RenderJSON() {
        super(H.Format.HTML);
    }

    public RenderJSON(String jsonStr) {
        super(jsonStr, H.Format.json);
    }
    public RenderJSON(String jsonFormat, Object ... args) {
        super(S.fmt(jsonFormat, args), H.Format.json);
    }
    public RenderJSON(Object v) {
        this(MvcConfig.jsonSerializer().apply(v));
    }

    public static RenderJSON of(String jsonStr) {
        payload.get().message(jsonStr);
        return _INSTANCE;
    }

    public static RenderJSON of(String jsonFormt, Object... args) {
        payload.get().message(jsonFormt, args);
        return _INSTANCE;
    }

    public static RenderJSON of(Object v) {
        String s = v instanceof String ? (String) v : MvcConfig.jsonSerializer().apply(v);
        payload.get().message(s);
        return _INSTANCE;
    }
}
