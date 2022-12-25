package net.minecraftforge.fml.relauncher;

import java.util.*;

public interface IFMLLoadingPlugin
{
    String[] getASMTransformerClass();
    
    String getModContainerClass();
    
    String getSetupClass();
    
    void injectData(final Map<String, Object> p0);
    
    String getAccessTransformerClass();
}
