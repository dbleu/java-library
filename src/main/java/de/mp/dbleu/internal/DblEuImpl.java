/*
 * Copyright 2021 by MauricePascal
 * Licensed under the GNU General Public License v3.0(the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.gnu.org/licenses/gpl-3.0.txt
 */

package de.mp.dbleu.internal;

import de.mp.dbleu.internal.events.Event;
import de.mp.dbleu.internal.events.ReadyEvent;
import de.mp.dbleu.internal.events.VoteEvent;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DblEuImpl implements DblEu {

    private final String BASE_URL = "https://api.discord-botlist.eu";
    private final String VERSION = "1.0.0";
    private final String apiKey;
    private final long botId;
    private final RatelimitManager ratelimitManager;
    private final List<Object> listeners;
    private final OkHttpClient httpClient;
    public static final MediaType mediaType = MediaType.get("application/json; charset=utf-8");
    private static final HttpUrl baseUrl = new HttpUrl.Builder()
            .scheme("https")
            .host("api.discord-botlist.eu")
            .addPathSegment("v1")
            .build();

    public DblEuImpl(String apiKey, long botId, List<Object> listeners) throws ClassNotFoundException, IOException {
        this.apiKey = apiKey;
        this.botId = botId;
        this.listeners = listeners;
        this.httpClient = new OkHttpClient.Builder().build();
        this.ratelimitManager = new RatelimitManager();
        checkIdentity();
        callEvent(new ReadyEvent(null));
    }

    private void checkIdentity() throws IOException {
        try {
            Response response = execute("/ping");
            JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
            this.ratelimitManager.addRequest("ping");
            if(this.botId != Long.parseLong(String.valueOf(((JSONObject) json.get("discord")).get("id")))) throw new IllegalArgumentException("Invalid id");
        } catch(JSONException jsonException) {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    private void callEvent(Object obj) throws ClassNotFoundException {
        if (Class.forName("de.mp.dbleu.internal.events.ReadyEvent").equals(obj.getClass())) {
            listeners.forEach(listener -> ((ListenerAdapter) listener).onReady(new ReadyEvent(this)));
        } else if (Class.forName("de.mp.dbleu.internal.events.VoteEvent").equals(obj.getClass())) {
            listeners.forEach(listener -> ((ListenerAdapter) listener).onVote(new VoteEvent(this)));
        }
    }

    private Response execute(String s) throws IOException {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Authorization", "Bearer "+this.apiKey);

        Headers headers = Headers.of(headersMap);
        Request request = new Request.Builder().url(this.BASE_URL+"/v1/"+s).headers(headers).build();

        return this.httpClient.newCall(request).execute();
    }

    @Override
    public String version() {
        return this.VERSION;
    }

    @Override
    public CompletionStage<Void> postData(int servers) throws IOException {
        if(this.ratelimitManager.postData().getAvailableRequests() == 0) {
            System.err.println("[RatelimitManager]: Ratelimit reached");
            return null;
        }
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Authorization", "Bearer "+this.apiKey);

        Headers headers = Headers.of(headersMap);
        Request request = new Request.Builder().url(this.BASE_URL+"/v1/update").method("POST", RequestBody.create(mediaType, "{\"serverCount\": "+servers+"}")).headers(headers).build();

        this.ratelimitManager.addRequest("post");

        this.httpClient.newCall(request).execute();
        return new CompletableFuture<>();
    }

    @Override
    public <T> CompletionStage<Event> simulateEvent(Class<T> clazz) throws ClassNotFoundException {
        final CompletableFuture<Event> future = new CompletableFuture<>();
        Class<?> readyEventClass = Class.forName("de.mp.dbleu.internal.events.ReadyEvent");
        Class<?> voteEventClass = Class.forName("de.mp.dbleu.internal.events.VoteEvent");
        if(clazz == readyEventClass) {
            future.complete(new ReadyEvent(this));
        } else if(clazz == voteEventClass) {
            future.complete(new VoteEvent(this));
        }
        return future;
    }

    @Override
    public RatelimitManager getRatelimitManager() {
        return this.ratelimitManager;
    }
}
