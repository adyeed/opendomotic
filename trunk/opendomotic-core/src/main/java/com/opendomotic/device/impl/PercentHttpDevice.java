package com.opendomotic.device.impl;

import com.opendomotic.device.util.Percent;

/**
 *
 * @author Jaques Claudino
 */
public class PercentHttpDevice extends HttpDevice {
    
    public static final int DEFAULT_MIN = 0;
    public static final int DEFAULT_MAX = 1024;
    public static final boolean DEFAULT_CHECK_RANGE = true;
    
    private int min = DEFAULT_MIN;
    private int max = DEFAULT_MAX;
    private boolean checkRange = DEFAULT_CHECK_RANGE;

    @Override
    public Integer getValue() throws Exception {
        return Percent.getPercent(super.getValue(), min, max, checkRange);
    }
    
    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean isCheckRange() {
        return checkRange;
    }

    public void setCheckRange(boolean checkRange) {
        this.checkRange = checkRange;
    }
    
}
