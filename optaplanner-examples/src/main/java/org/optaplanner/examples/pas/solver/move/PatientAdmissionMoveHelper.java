/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.examples.pas.solver.move;

import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.examples.pas.domain.Bed;
import org.optaplanner.examples.pas.domain.BedDesignation;

public class PatientAdmissionMoveHelper {

    public static void moveBed(ScoreDirector scoreDirector, BedDesignation bedDesignation, Bed toBed) {
        scoreDirector.beforeVariableChanged(bedDesignation, "bed");
        bedDesignation.setBed(toBed);
        scoreDirector.afterVariableChanged(bedDesignation, "bed");
    }

    private PatientAdmissionMoveHelper() {
    }

}
