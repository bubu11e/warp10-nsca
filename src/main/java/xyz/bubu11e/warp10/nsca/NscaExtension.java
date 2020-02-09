package xyz.bubu11e.warp10.nsca;

import java.util.HashMap;
import java.util.Map;

import io.warp10.warp.sdk.WarpScriptExtension;

public class NscaExtension extends WarpScriptExtension {
  
  private static final Map<String,Object> functions;
  
  static {
    functions = new HashMap<String,Object>();
    
    functions.put("NSCA", new NSCA("NSCA"));
  }
  
  @Override
  public Map<String, Object> getFunctions() {
    return functions;
  }
}
