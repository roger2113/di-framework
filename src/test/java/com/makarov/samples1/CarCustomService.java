package com.makarov.samples1;

import com.makarov.annotation.Autowired;
import com.makarov.annotation.Component;

@Component
public class CarCustomService implements CarCommonService {

    @Autowired
    private ManagerCommonService managerCommonService;

    @Override
    public String getResult() {
        return "CarCustomService processed;" + managerCommonService.getResult();
    }

}
