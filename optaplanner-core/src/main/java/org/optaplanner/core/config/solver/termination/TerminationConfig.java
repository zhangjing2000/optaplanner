/*
 * Copyright 2010 JBoss Inc
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

package org.optaplanner.core.config.solver.termination;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.config.heuristic.policy.HeuristicConfigPolicy;
import org.optaplanner.core.config.util.ConfigUtils;
import org.optaplanner.core.impl.solver.termination.AbstractCompositeTermination;
import org.optaplanner.core.impl.solver.termination.AndCompositeTermination;
import org.optaplanner.core.impl.solver.termination.BestScoreTermination;
import org.optaplanner.core.impl.solver.termination.OrCompositeTermination;
import org.optaplanner.core.impl.solver.termination.StepCountTermination;
import org.optaplanner.core.impl.solver.termination.Termination;
import org.optaplanner.core.impl.solver.termination.TimeMillisSpentTermination;
import org.optaplanner.core.impl.solver.termination.UnimprovedStepCountTermination;
import org.optaplanner.core.impl.solver.termination.UnimprovedTimeMillisSpentTermination;

@XStreamAlias("termination")
public class TerminationConfig implements Cloneable {

    private Class<? extends Termination> terminationClass = null;

    private TerminationCompositionStyle terminationCompositionStyle = null;

    private Long millisecondsSpentLimit = null;
    private Long secondsSpentLimit = null;
    private Long minutesSpentLimit = null;
    private Long hoursSpentLimit = null;
    private Long unimprovedMillisecondsSpentLimit = null;
    private Long unimprovedSecondsSpentLimit = null;
    private Long unimprovedMinutesSpentLimit = null;
    private Long unimprovedHoursSpentLimit = null;

    private String bestScoreLimit = null;

    private Integer stepCountLimit = null;
    private Integer unimprovedStepCountLimit = null;

    @XStreamImplicit(itemFieldName = "termination")
    private List<TerminationConfig> terminationConfigList = null;

    public Class<? extends Termination> getTerminationClass() {
        return terminationClass;
    }

    public void setTerminationClass(Class<? extends Termination> terminationClass) {
        this.terminationClass = terminationClass;
    }

    public TerminationCompositionStyle getTerminationCompositionStyle() {
        return terminationCompositionStyle;
    }

    public void setTerminationCompositionStyle(TerminationCompositionStyle terminationCompositionStyle) {
        this.terminationCompositionStyle = terminationCompositionStyle;
    }

    public Long getMillisecondsSpentLimit() {
        return millisecondsSpentLimit;
    }

    public void setMillisecondsSpentLimit(Long millisecondsSpentLimit) {
        this.millisecondsSpentLimit = millisecondsSpentLimit;
    }

    public Long getSecondsSpentLimit() {
        return secondsSpentLimit;
    }

    public void setSecondsSpentLimit(Long secondsSpentLimit) {
        this.secondsSpentLimit = secondsSpentLimit;
    }

    public Long getMinutesSpentLimit() {
        return minutesSpentLimit;
    }

    public void setMinutesSpentLimit(Long minutesSpentLimit) {
        this.minutesSpentLimit = minutesSpentLimit;
    }

    public Long getHoursSpentLimit() {
        return hoursSpentLimit;
    }

    public void setHoursSpentLimit(Long hoursSpentLimit) {
        this.hoursSpentLimit = hoursSpentLimit;
    }

    public Long getUnimprovedMillisecondsSpentLimit() {
        return unimprovedMillisecondsSpentLimit;
    }

    public void setUnimprovedMillisecondsSpentLimit(Long unimprovedMillisecondsSpentLimit) {
        this.unimprovedMillisecondsSpentLimit = unimprovedMillisecondsSpentLimit;
    }

    public Long getUnimprovedSecondsSpentLimit() {
        return unimprovedSecondsSpentLimit;
    }

    public void setUnimprovedSecondsSpentLimit(Long unimprovedSecondsSpentLimit) {
        this.unimprovedSecondsSpentLimit = unimprovedSecondsSpentLimit;
    }

    public Long getUnimprovedMinutesSpentLimit() {
        return unimprovedMinutesSpentLimit;
    }

    public void setUnimprovedMinutesSpentLimit(Long unimprovedMinutesSpentLimit) {
        this.unimprovedMinutesSpentLimit = unimprovedMinutesSpentLimit;
    }

    public Long getUnimprovedHoursSpentLimit() {
        return unimprovedHoursSpentLimit;
    }

    public void setUnimprovedHoursSpentLimit(Long unimprovedHoursSpentLimit) {
        this.unimprovedHoursSpentLimit = unimprovedHoursSpentLimit;
    }

    public String getBestScoreLimit() {
        return bestScoreLimit;
    }

    public void setBestScoreLimit(String bestScoreLimit) {
        this.bestScoreLimit = bestScoreLimit;
    }

    public Integer getStepCountLimit() {
        return stepCountLimit;
    }

    public void setStepCountLimit(Integer stepCountLimit) {
        this.stepCountLimit = stepCountLimit;
    }

    public Integer getUnimprovedStepCountLimit() {
        return unimprovedStepCountLimit;
    }

    public void setUnimprovedStepCountLimit(Integer unimprovedStepCountLimit) {
        this.unimprovedStepCountLimit = unimprovedStepCountLimit;
    }

    public List<TerminationConfig> getTerminationConfigList() {
        return terminationConfigList;
    }

    public void setTerminationConfigList(List<TerminationConfig> terminationConfigList) {
        this.terminationConfigList = terminationConfigList;
    }
    
    // ************************************************************************
    // Builder methods
    // ************************************************************************

    public Termination buildTermination(HeuristicConfigPolicy configPolicy, Termination chainedTermination) {
        Termination termination = buildTermination(configPolicy);
        if (termination == null) {
            return chainedTermination;
        }
        return new OrCompositeTermination(chainedTermination, termination);
    }

    /**
     * @param configPolicy never null
     * @return sometimes null
     */
    public Termination buildTermination(HeuristicConfigPolicy configPolicy) {
        List<Termination> terminationList = new ArrayList<Termination>();
        if (terminationClass != null) {
            Termination termination  = ConfigUtils.newInstance(this, "terminationClass", terminationClass);
            terminationList.add(termination);
        }
        Long timeMillisSpentLimit = calculateTimeMillisSpentLimit();
        if (timeMillisSpentLimit != null) {
            terminationList.add(new TimeMillisSpentTermination(timeMillisSpentLimit));
        }
        Long unimprovedTimeMillisSpentLimit = calculateUnimprovedTimeMillisSpentLimit();
        if (unimprovedTimeMillisSpentLimit != null) {
            terminationList.add(new UnimprovedTimeMillisSpentTermination(unimprovedTimeMillisSpentLimit));
        }
        if (bestScoreLimit != null) {
            Score bestScoreLimit_ = configPolicy.getScoreDefinition().parseScore(bestScoreLimit);
            terminationList.add(new BestScoreTermination(bestScoreLimit_));
        }
        if (stepCountLimit != null) {
            terminationList.add(new StepCountTermination(stepCountLimit));
        }
        if (unimprovedStepCountLimit != null) {
            terminationList.add(new UnimprovedStepCountTermination(unimprovedStepCountLimit));
        }
        if (!ConfigUtils.isEmptyCollection(terminationConfigList)) {
            for (TerminationConfig terminationConfig : terminationConfigList) {
                Termination termination = terminationConfig.buildTermination(configPolicy);
                if (termination != null) {
                    terminationList.add(termination);
                }
            }
        }
        if (terminationList.size() == 1) {
            return terminationList.get(0);
        } else if (terminationList.size() > 1) {
            AbstractCompositeTermination compositeTermination;
            if (terminationCompositionStyle == null || terminationCompositionStyle == TerminationCompositionStyle.OR) {
                compositeTermination = new OrCompositeTermination(terminationList);
            } else if (terminationCompositionStyle == TerminationCompositionStyle.AND) {
                compositeTermination = new AndCompositeTermination(terminationList);
            } else {
                throw new IllegalStateException("The terminationCompositionStyle (" + terminationCompositionStyle
                        + ") is not implemented.");
            }
            return compositeTermination;
        } else {
            return null;
        }
    }

    public Long calculateTimeMillisSpentLimit() {
        if (millisecondsSpentLimit == null && secondsSpentLimit == null
                && minutesSpentLimit == null && hoursSpentLimit == null) {
            return null;
        }
        long timeMillisSpentLimit = 0L;
        if (millisecondsSpentLimit != null) {
            timeMillisSpentLimit += millisecondsSpentLimit;
        }
        if (secondsSpentLimit != null) {
            timeMillisSpentLimit += secondsSpentLimit * 1000L;
        }
        if (minutesSpentLimit != null) {
            timeMillisSpentLimit += minutesSpentLimit * 60000L;
        }
        if (hoursSpentLimit != null) {
            timeMillisSpentLimit += hoursSpentLimit * 3600000L;
        }
        return timeMillisSpentLimit;
    }

    public void shortenTimeMillisSpentLimit(long timeMillisSpentLimit) {
        Long oldLimit = calculateTimeMillisSpentLimit();
        if (oldLimit == null || timeMillisSpentLimit < oldLimit) {
            millisecondsSpentLimit = timeMillisSpentLimit;
            secondsSpentLimit = null;
            minutesSpentLimit = null;
            hoursSpentLimit = null;
        }
    }

    public Long calculateUnimprovedTimeMillisSpentLimit() {
        if (unimprovedMillisecondsSpentLimit == null && unimprovedSecondsSpentLimit == null
                && unimprovedMinutesSpentLimit == null && unimprovedHoursSpentLimit == null) {
            return null;
        }
        long unimprovedTimeMillisSpentLimit = 0L;
        if (unimprovedMillisecondsSpentLimit != null) {
            unimprovedTimeMillisSpentLimit += unimprovedMillisecondsSpentLimit;
        }
        if (unimprovedSecondsSpentLimit != null) {
            unimprovedTimeMillisSpentLimit += unimprovedSecondsSpentLimit * 1000L;
        }
        if (unimprovedMinutesSpentLimit != null) {
            unimprovedTimeMillisSpentLimit += unimprovedMinutesSpentLimit * 60000L;
        }
        if (unimprovedHoursSpentLimit != null) {
            unimprovedTimeMillisSpentLimit += unimprovedHoursSpentLimit * 3600000L;
        }
        return unimprovedTimeMillisSpentLimit;
    }

    public void inherit(TerminationConfig inheritedConfig) {
        terminationClass = ConfigUtils.inheritOverwritableProperty(terminationClass,
                inheritedConfig.getTerminationClass());
        terminationCompositionStyle = ConfigUtils.inheritOverwritableProperty(terminationCompositionStyle,
                inheritedConfig.getTerminationCompositionStyle());
        millisecondsSpentLimit = ConfigUtils.inheritOverwritableProperty(millisecondsSpentLimit,
                inheritedConfig.getMillisecondsSpentLimit());
        secondsSpentLimit = ConfigUtils.inheritOverwritableProperty(secondsSpentLimit,
                inheritedConfig.getSecondsSpentLimit());
        minutesSpentLimit = ConfigUtils.inheritOverwritableProperty(minutesSpentLimit,
                inheritedConfig.getMinutesSpentLimit());
        hoursSpentLimit = ConfigUtils.inheritOverwritableProperty(hoursSpentLimit,
                inheritedConfig.getHoursSpentLimit());
        unimprovedMillisecondsSpentLimit = ConfigUtils.inheritOverwritableProperty(unimprovedMillisecondsSpentLimit,
                inheritedConfig.getUnimprovedMillisecondsSpentLimit());
        unimprovedSecondsSpentLimit = ConfigUtils.inheritOverwritableProperty(unimprovedSecondsSpentLimit,
                inheritedConfig.getUnimprovedSecondsSpentLimit());
        unimprovedMinutesSpentLimit = ConfigUtils.inheritOverwritableProperty(unimprovedMinutesSpentLimit,
                inheritedConfig.getUnimprovedMinutesSpentLimit());
        unimprovedHoursSpentLimit = ConfigUtils.inheritOverwritableProperty(unimprovedHoursSpentLimit,
                inheritedConfig.getUnimprovedHoursSpentLimit());
        bestScoreLimit = ConfigUtils.inheritOverwritableProperty(bestScoreLimit,
                inheritedConfig.getBestScoreLimit());
        stepCountLimit = ConfigUtils.inheritOverwritableProperty(stepCountLimit,
                inheritedConfig.getStepCountLimit());
        unimprovedStepCountLimit = ConfigUtils.inheritOverwritableProperty(unimprovedStepCountLimit,
                inheritedConfig.getUnimprovedStepCountLimit());
        terminationConfigList = ConfigUtils.inheritMergeableListProperty(
                terminationConfigList, inheritedConfig.getTerminationConfigList());
    }

    @Override
    public TerminationConfig clone() {
        TerminationConfig clone;
        try {
            clone = (TerminationConfig) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Impossible state because TerminationConfig implements Cloneable.", e);
        }
        // Deep clone terminationConfigList
        if (terminationConfigList != null) {
            List<TerminationConfig> clonedTerminationConfigList = new ArrayList<TerminationConfig>(
                    terminationConfigList.size());
            for (TerminationConfig terminationConfig : terminationConfigList) {
                TerminationConfig clonedTerminationConfig = terminationConfig.clone();
                clonedTerminationConfigList.add(clonedTerminationConfig);
            }
            clone.terminationConfigList = clonedTerminationConfigList;
        } else {
            clone.terminationConfigList = null;
        }
        return clone;
    }

    public enum TerminationCompositionStyle {
        AND,
        OR,
    }

}
