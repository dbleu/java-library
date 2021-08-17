/*
 * Copyright 2021 by MauricePascal
 * Licensed under the GNU General Public License v3.0(the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.gnu.org/licenses/gpl-3.0.txt
 */

package de.mp.dbleu.internal.events;

import de.mp.dbleu.internal.DblEu;

public class ReadyEvent implements Event {

    private final DblEu dblEu;

    public ReadyEvent(DblEu dblEu) {
        this.dblEu = dblEu;
    }

    @Override
    public DblEu getDblEu() {
        return this.dblEu;
    }
}
