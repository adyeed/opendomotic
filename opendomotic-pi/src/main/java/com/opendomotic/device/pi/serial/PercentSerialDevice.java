package com.opendomotic.device.pi.serial;

import com.opendomotic.device.util.Percent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaques Claudino
 */
public class PercentSerialDevice extends SerialDevice {
    
    private static final Logger LOG = Logger.getLogger(PercentSerialDevice.class.getName());
    
    public static final int DEFAULT_MIN = 0;
    public static final int DEFAULT_MAX = 1024;
    public static final boolean DEFAULT_CHECK_RANGE = true;
    public static final boolean DEFAULT_AUTO = false;
    public static final boolean SHOW_LOG = false;
    
    private Integer min = null;
    private Integer max = null;
    private boolean checkRange = DEFAULT_CHECK_RANGE;
    private boolean auto = DEFAULT_AUTO;
    
    @Override
    public Integer getValue() throws Exception {
        int value = super.getValue();        
        checkMinMax(value); 
        return (int) Percent.getPercent(value, min, max, checkRange);
    }
    
    private void checkMinMax(int value) {
        if (auto) {
            if (min == null || value < min) {
                min = value;
                logMinMax();
            }
            if (max == null || value > max) {
                max = value;
                logMinMax();
            }
        } else if (min == null || max == null) {
            min = DEFAULT_MIN;
            max = DEFAULT_MAX;
        }
    }
    
    private void logMinMax() {
        if (SHOW_LOG) {
            LOG.log(Level.INFO, "***** address={0} | min={1} | max={2}", new Object[] {getAddress(), min, max});
        }
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

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }
    
}
