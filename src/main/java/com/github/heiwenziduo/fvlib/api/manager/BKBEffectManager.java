package com.github.heiwenziduo.fvlib.api.manager;

public class BKBEffectManager {
    private boolean BKBStatus = false;

    public boolean hasBKB() {
        return BKBStatus;
    }

    public void setBKB(boolean bkb) {
        BKBStatus = bkb;
    }
}
