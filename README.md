# warp10-nsca

# Description
Perform NSCA Calls from warp10 platform

# Build
Use the following commands to build the project.

    git clone ...
    cd warp10-nsca
    gradle /gradlew -Duberjar shadowJar

Result of build is in build/libs/xys.bubu11e.warp10.nsca-\<version\>-all.jar

# Deploy

Put the jar in the extension directory of your Warp10 distribution.

Add the following configuration line to your Warp10 configuration file :

    warpscript.extensions = xyz.bubu11e.warp10.nsca.NscaExtension

# Use

You can use the NSCA function as follow :

    {
      'url' '127.0.0.1'
      'port' '5667'
      'encryption' 'XOR'
      'password' '<password>'
      'hostname' 'warp10'
      'level' 'CRITICAL'
      'service' '<service>'
      'message' 'Critical message from Warp10'
    } NSCA
