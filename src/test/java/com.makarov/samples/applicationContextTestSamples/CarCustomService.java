package com.makarov.samples.applicationContextTestSamples;

import com.makarov.core.annotation.Autowired;
import com.makarov.core.annotation.Component;

@Component
public class CarCustomService implements CarCommonService {

    @Autowired
    private ManagerCustomService managerCustomService;

    @Autowired
    private ManagerAdditionalService managerAdditionalService;

    public ManagerCommonService getManagerCustomService() {
        return managerCustomService;
    }

    public ManagerAdditionalService getManagerAdditionalService() {
        return managerAdditionalService;
    }

    @Override
    public String getResult() {
        return "CarCustomService processed;" + managerCustomService.getResult();
    }

}
