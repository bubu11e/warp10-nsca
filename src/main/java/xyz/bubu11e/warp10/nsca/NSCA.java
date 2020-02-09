package xyz.bubu11e.warp10.nsca;

import com.googlecode.jsendnsca.*;
import com.googlecode.jsendnsca.builders.MessagePayloadBuilder;
import com.googlecode.jsendnsca.builders.NagiosSettingsBuilder;
import com.googlecode.jsendnsca.encryption.Encryption;
import io.warp10.script.NamedWarpScriptFunction;
import io.warp10.script.WarpScriptException;
import io.warp10.script.WarpScriptStack;
import io.warp10.script.WarpScriptStackFunction;

import java.io.IOException;
import java.util.Map;

public class NSCA extends NamedWarpScriptFunction implements WarpScriptStackFunction {
  
  public NSCA(String name) {
    super(name);
  }

  private String getStringArgument(Map<String, Object> arguments, String key) throws WarpScriptException {
    if(!arguments.containsKey(key)) {
      throw new WarpScriptException("Arguments does not contains key '" + key + "'.");
    }

    try {
      return (String) arguments.get(key);
    } catch (ClassCastException e) {
      throw new WarpScriptException("Arguments does contains key '" + key + "' but value is not a String.");
    }
  }

  private Integer getIntegerArgument(Map<String, Object> arguments, String key) throws WarpScriptException {
    try {
      return Integer.valueOf(getStringArgument(arguments, key));
    } catch (NumberFormatException e) {
      throw new WarpScriptException("Arguments does contains key '" + key + "' but value is not a number.");
    }
  }

  private Encryption getEncryptionArgument(Map<String, Object> arguments, String key) throws WarpScriptException {
    try {
      return Encryption.valueOf(getStringArgument(arguments, key));
    } catch (IllegalArgumentException e) {
      throw new WarpScriptException("Arguments does contains key '" + key + "' but value is not an encryption type.");
    }
  }

  private Level getLevelArgument(Map<String, Object> arguments, String key) throws WarpScriptException {
    try {
      return Level.valueOf(getStringArgument(arguments, key));
    } catch (IllegalArgumentException e) {
      throw new WarpScriptException("Arguments does contains key '" + key + "' but value is not a valid level.");
    }
  }

  @Override
  public Object apply(WarpScriptStack stack) throws WarpScriptException {
    /* Retrieve Configuration. */
    Object input = stack.pop();

    if (!(input instanceof Map)) {
      throw new WarpScriptException("Input parameter must be a map.");
    }

    Map<String, Object> arguments = (Map<String, Object>) input;

    final String url = getStringArgument(arguments, "url");
    final Integer port = getIntegerArgument(arguments, "port");
    final Encryption encryption = getEncryptionArgument(arguments, "encryption");
    final String password = getStringArgument(arguments, "password");

    final String hostname = getStringArgument(arguments, "hostname");
    final Level level = getLevelArgument(arguments, "level");
    final String service = getStringArgument(arguments, "service");
    final String message = getStringArgument(arguments, "message");

    /* Generate nagios settings object. */
    NagiosSettings settings = new NagiosSettingsBuilder()
            .withNagiosHost(url)
            .withPort(port)
            .withEncryption(encryption)
            .withPassword(password)
            .create();

    /* Generate message. */
    MessagePayload payload = new MessagePayloadBuilder()
            .withHostname(hostname)
            .withLevel(level)
            .withServiceName(service)
            .withMessage(message)
            .create();

    /* Create sender from settings. */
    NagiosPassiveCheckSender sender = new NagiosPassiveCheckSender(settings);

    try {
      /* Send message. */
      sender.send(payload);
    } catch (NagiosException | IOException e) {
      throw new WarpScriptException("Failed to send nsca request. Reason: '" + e.getMessage() + "'.", e);
    }

    return stack;
  }
}
