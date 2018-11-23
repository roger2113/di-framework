package com.makarov.samples.sample1;

import com.makarov.core.annotation.Autowired;
import com.makarov.core.annotation.Component;

@Component
public class SomeService {
    @Autowired
    private SomeDependency someDependency;
}
