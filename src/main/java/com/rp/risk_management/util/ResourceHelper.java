package com.rp.risk_management.util;

import java.io.File;

public class ResourceHelper
{
    private static ResourceHelper instance_ = new ResourceHelper();
    private ResourceHelper()
    {}

    public static ResourceHelper getInstance() {
        return instance_;
    }

    public File getResource(String resourceName)
    {
        return new File(this.getClass().getClassLoader().getResource(resourceName ).getFile());
    }
}
