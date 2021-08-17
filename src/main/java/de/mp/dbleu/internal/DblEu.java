package de.mp.dbleu.internal;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public interface DblEu {

    String version();

    class Builder {

        private String apiKey;
        private long botId;
        private List<Object> listeners = new LinkedList<>();

        public Builder setAPIKey(String key) {
            this.apiKey = key;
            return this;
        }

        public Builder setId(long id) {
            this.botId = id;
            return this;
        }

        public Builder setId(String id) {
            try {
                this.botId = Long.getLong(id);
            } catch(Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Builder addEventListeners(Object... listeners) {
            Collections.addAll(this.listeners, listeners);
            return this;
        }

        public DblEu build() throws ClassNotFoundException, IOException {
            return new DblEuImpl(apiKey, botId, listeners);
        }

    }

}
