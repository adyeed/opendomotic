package com.opendomotic.device.impl;

import com.opendomotic.device.util.Percent;

/**
 *
 * @author Jaques Claudino
 */
public class PercentHttpDevice extends DoubleHttpDevice {
    
    public static final double DEFAULT_MIN = 0;
    public static final double DEFAULT_MAX = 1023;
    public static final boolean DEFAULT_CHECK_RANGE = true;
    
    private Double min = DEFAULT_MIN;
    private Double max = DEFAULT_MAX;

    @Override
    public Double getValue() throws Exception {
        Double value = super.getValue();
        return Percent.getPercent(value, min, max, DEFAULT_CHECK_RANGE);
    }
    
    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }
    
}
