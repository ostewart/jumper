Build Notes
===========

In order to build jumper, you must have Firefox installed with a profile for WebDriver/Selenium.

You will need to create a Firefox profile called "WebDriver".  Instructions for doing this can be found [here](http://www.qaautomation.net/?p=45).  You should hit the running server and add a permanent exception for your self-signed certificate.  This will allow the Selenium tests to run without error.  (I realize there may be a bootstrapping problem here...I haven't run into it myself, so I haven't solved it either.)  I also add the following key to the configuration, in order to keep Firefox from going into offline mode and failing my tests when I'm not connected to a network: `network.manage-offline-status`.  If you set it to `false`, Firefox won't put you in offline mode.


Running Tomcat In IntelliJ
--------------------------

In order to run jumper in Tomcat, you need to have SSL setup. A keystore is provided in the repository:

# Configure Tomcat for SSL
Add the following to conf/server.xml with the keystoreFile modified for your environment.

      <Connector port="8443" protocol="HTTP/1.1" SSLEnabled="true"
                 maxThreads="150" scheme="https" secure="true" keystorePass="password"
                 keystoreFile="/Users/oliver/devel/jumper/integration-test/src/main/resources/server.keystore"
                 clientAuth="false" sslProtocol="TLS" />

