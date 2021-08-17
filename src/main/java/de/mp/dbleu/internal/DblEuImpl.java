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

import java.io.*;
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
        callEvent(new ReadyEvent(null));
    }

    private void callEvent(Object obj) throws ClassNotFoundException {
        if(obj.getClass() == Class.forName("de.mp.dbleu.internal.events.ReadyEvent")) {
            listeners.forEach(listener -> ((ListenerAdapter) listener).onReady(new ReadyEvent(this)));
        }
    }

    @Override
    public String version() {
        return this.VERSION;
    }
}
