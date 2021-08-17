/*
 * Copyright 2021 by MauricePascal
 * Licensed under the GNU General Public License v3.0(the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.gnu.org/licenses/gpl-3.0.txt
 */

package de.mp.dbleu.internal;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RatelimitManager {

    private final List<String> requests;

    public RatelimitManager() {
        this.requests = new LinkedList<>();
    }

    protected void addRequest(String endpoint) {
        requests.add(endpoint);
        new Thread(() -> {
            try {
                if(endpoint.equalsIgnoreCase("post")) Thread.sleep(300000); else Thread.sleep(600000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AtomicInteger index = new AtomicInteger();
            requests.forEach(entry -> {
                if(entry.equalsIgnoreCase(endpoint)) {
                    requests.remove(index.get());
                    index.getAndIncrement();
                }
            });
        }).start();
    }

    public PostDataManager postData() {
        return new PostDataManager(this);
    }

    public VotesManager votes() {
        return new VotesManager(this);
    }

    static class PostDataManager {

        private final String ENDPOINT = "post";
        private final RatelimitManager manager;

        public PostDataManager(RatelimitManager manager) {
            this.manager = manager;
        }

        public int getAvailableRequests() {
            int max = 1;
            int entries = (int) this.manager.requests.stream().filter(entry -> entry.equalsIgnoreCase(ENDPOINT)).count();
            return max-entries;
        }

    }

    static class VotesManager {

        private final String ENDPOINT = "votes";
        private final RatelimitManager manager;

        public VotesManager(RatelimitManager manager) {
            this.manager = manager;
        }

        public int getAvailableRequests() {
            int max = 10;
            int entries = (int) this.manager.requests.stream().filter(entry -> entry.equalsIgnoreCase(ENDPOINT)).count();
            return max-entries;
        }

    }

    static class PingManager {

        private final String ENDPOINT = "ping";
        private final RatelimitManager manager;

        public PingManager(RatelimitManager manager) {
            this.manager = manager;
        }

        public int getAvailableRequests() {
            int max = 10;
            int entries = (int) this.manager.requests.stream().filter(entry -> entry.equalsIgnoreCase(ENDPOINT)).count();
            return max-entries;
        }

    }

}
