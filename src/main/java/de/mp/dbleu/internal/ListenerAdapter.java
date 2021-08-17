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

public abstract class ListenerAdapter {

    public void onReady(ReadyEvent event) {}
    public void onVote(VoteEvent event) {}

}
