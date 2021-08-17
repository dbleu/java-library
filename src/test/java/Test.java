/*
 * Copyright 2021 by MauricePascal
 * Licensed under the GNU General Public License v3.0(the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.gnu.org/licenses/gpl-3.0.txt
 */

import de.mp.dbleu.internal.DblEu;
import de.mp.dbleu.internal.ListenerAdapter;
import de.mp.dbleu.internal.events.ReadyEvent;

import java.io.IOException;

public class Test extends ListenerAdapter {

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        DblEu api = new DblEu.Builder()
                .setAPIKey("API KEY")
                .setId(819303604820639785L)
                .addEventListeners(new Test())
                .build();
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Ready!");
        System.out.println(event.getDblEu());
    }
}
