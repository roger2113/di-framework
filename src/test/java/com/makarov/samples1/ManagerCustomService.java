package com.makarov.samples1;

import com.makarov.annotation.Component;

@Component
public class ManagerCustomService implements ManagerCommonService {

    @Override
    public String getResult() {
        return "ManagerCustomService processed;";
    }
}
