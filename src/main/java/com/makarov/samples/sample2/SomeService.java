package com.makarov.samples.sample2;

import com.makarov.core.annotation.Autowired;
import com.makarov.core.annotation.Component;

@Component
public class SomeService {
    @Autowired
    private SomeDependency someDependency;
    @Autowired
    private OtherDependency otherDependency;
}
