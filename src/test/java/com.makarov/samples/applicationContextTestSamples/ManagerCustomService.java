package com.makarov.samples.applicationContextTestSamples;

import com.makarov.core.annotation.Component;

@Component
public class ManagerCustomService implements ManagerCommonService {

    @Override
    public String getResult() {
        return "ManagerCustomService processed;";
    }
}
