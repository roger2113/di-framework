package com.makarov.samples.applicationContextTestSamples;

import com.makarov.core.annotation.Component;

@Component
public class ManagerAdditionalService implements ManagerCommonService{
    @Override
    public String getResult() {
        return "ManagerAdditionalService processed";
    }
}
