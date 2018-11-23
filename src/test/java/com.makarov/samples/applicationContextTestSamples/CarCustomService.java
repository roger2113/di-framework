package com.makarov.samples.applicationContextTestSamples;

import com.makarov.core.annotation.Autowired;
import com.makarov.core.annotation.Component;

@Component
public class CarCustomService implements CarCommonService {

    @Autowired
    private ManagerCommonService managerCommonService;

    @Override
    public String getResult() {
        return "CarCustomService processed;" + managerCommonService.getResult();
    }

}
