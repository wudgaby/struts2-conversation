This setup guide includes directions for installing Eclipse and adding the Maven and SVN plugins and for checking out and running an example application.

### Install Eclipse ###
Download Eclipse and install it. [[download page](http://www.eclipse.org/downloads/)]

### Install M2E Maven Plugin ###
  1. Open Eclipse
  1. Go to Help->Install New Software...
  1. Paste this URL into the field and click "Add":  http://download.eclipse.org/technology/m2e/releases/
  1. Follow the directions and finish installing the plugin.
  1. Restart Eclipse.

### Install SVN Plugin ###
  1. Open Eclipse
  1. Go to Help->Install New Software...
  1. Paste this URL into the field and click "Add":  http://subclipse.tigris.org/update_1.8.x
  1. Restart Eclipse.

### Checkout Example Project and Run It ###
  1. Go to Window->Open Perspective->Other...
  1. Select "SVN Repository Exploring"
  1. You should now see the "SVN Repositories" sub-window on the left.
  1. Right-click in the white area of the SVN Repositories view.
  1. Select New->Repository Location
  1. Enter the following as the URL:  http://struts2-conversation.googlecode.com/svn/support/examples
  1. Expand the new repository directory.
  1. You should see folders for each of the example projects.
  1. Right click on one of the projects and select "Checkout..."
  1. Select "Check out as a project in the workspace" and click "Finish".
  1. Click on "Java" or "JEE" in the top right-hand corner of Eclipse to switch back to a Java perspective.
  1. You should see the project that you just checkout out in the Package Explorer on the left.
  1. Right click on the project and select Configure->Convert to Maven Project
  1. When the conversion has finished, right click on the project again and select Run as->Maven Build
  1. Type "jetty:run" in the "Goals" field and then click the "Run" button.
Go to this URL to use the app:  http://localhost:8080/simple-example/registration/begin.action

Go to this URL to browse the app configuration:  http://localhost:8080/simple-example/conversation-config-browser/index.action