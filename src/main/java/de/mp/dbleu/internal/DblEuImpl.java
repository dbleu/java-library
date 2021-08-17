/*
 * Copyright 2021 by MauricePascal
 * Licensed under the GNU General Public License v3.0(the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.gnu.org/licenses/gpl-3.0.txt
 */

package de.mp.dbleu.internal;

import de.mp.dbleu.internal.events.ReadyEvent;
import de.mp.dbleu.internal.events.VoteEvent;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DblEuImpl implements DblEu {

    private final String BASE_URL = "https://api.discord-botlist.eu";
    private final String VERSION = "1.0.0";
    private final String apiKey;
    private final long botId;
    private final List<Object> listeners;

    public DblEuImpl(String apiKey, long botId, List<Object> listeners) throws ClassNotFoundException, IOException {
        this.apiKey = apiKey;
        this.botId = botId;
        this.listeners = listeners;
        checkIdentity();
        callEvent(new ReadyEvent(null));
    }

    @Deprecated
    private void checkIdentity() throws IOException {
        URL url = new URL(this.BASE_URL+"/v1/ping");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("Authorization", "Bearer "+this.apiKey);

        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        con.setInstanceFollowRedirects(false);

        int status = con.getResponseCode();
    }

    private void callEvent(Object obj) throws ClassNotFoundException {
        if (Class.forName("de.mp.dbleu.internal.events.ReadyEvent").equals(obj.getClass())) {
            listeners.forEach(listener -> ((ListenerAdapter) listener).onReady(new ReadyEvent(this)));
        } else if (Class.forName("de.mp.dbleu.internal.events.VoteEvent").equals(obj.getClass())) {
            listeners.forEach(listener -> ((ListenerAdapter) listener).onVote(new VoteEvent(this)));
        }
    }

    @Override
    public String version() {
        return this.VERSION;
    }
}
