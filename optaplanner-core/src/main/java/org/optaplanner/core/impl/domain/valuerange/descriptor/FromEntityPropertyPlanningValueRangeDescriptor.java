/*
 * Copyright 2011 JBoss Inc
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

package org.optaplanner.core.impl.domain.valuerange.descriptor;

import java.lang.reflect.Method;

import org.optaplanner.core.api.domain.valuerange.ValueRange;
import org.optaplanner.core.impl.domain.variable.descriptor.PlanningVariableDescriptor;
import org.optaplanner.core.impl.solution.Solution;

public class FromEntityPropertyPlanningValueRangeDescriptor extends AbstractFromPropertyPlanningValueRangeDescriptor {

    public FromEntityPropertyPlanningValueRangeDescriptor(
            PlanningVariableDescriptor variableDescriptor, boolean addNullInValueRange,
            Method readMethod) {
        super(variableDescriptor, addNullInValueRange, readMethod);
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    @Override
    public boolean isEntityIndependent() {
        return false;
    }

    @Override
    public boolean isValuesCacheable() {
        return false;
    }

    @Override
    public ValueRange<?> extractValueRange(Solution solution, Object entity) {
        return readValueRange(entity);
    }

}