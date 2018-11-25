package fi.iki.elonen.responses;

import android.content.Context;
import fi.iki.elonen.HttpResponse;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.menu.Menu;
import fi.iki.elonen.velocity.VelocityAsset;
import java.io.IOException;

public abstract class BaseVelocityResponse implements HttpResponse {

    private final Context mContext;

    protected BaseVelocityResponse(Context context) {
        mContext = context;
    }

    @Override
    public final NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession session, Menu menu) throws IOException {
        VelocityAsset v = new VelocityAsset();
        v.initAsset(mContext, getVelocityAsset());

        prepareVelocityVariables(v, session, menu);

        return new NanoHTTPD.Response(v.html());
    }

    protected void prepareVelocityVariables(VelocityAsset v, NanoHTTPD.IHTTPSession session, Menu menu) {

    }

    protected abstract String getVelocityAsset();

    protected Context getContext() {
        return mContext;
    }
}