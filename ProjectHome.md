[Maven Repo](https://oss.sonatype.org/index.html#nexus-search;gav~com.googlecode.struts2-conversation~~~~)  - [Example App](http://code.google.com/p/struts2-conversation/wiki/EclipseAndMavenGuide) -  [API](http://struts2-conversation.googlecode.com/svn/javadoc/struts2-conversation-scope-plugin-1.7.0-javadoc/index.html) - [Trouble Shooting](http://code.google.com/p/struts2-conversation/wiki/TroubleShooting)

# Struts2 Conversation Plugin #

Handling data across multi-page flows and multiple browser tabs is achieved
with annotations and a Struts2-extending tag library.

As well, data that might normally be stored in the session can instead be managed in a conversation and released or timed-out by the framework to conserve system resources.

This plugin simplifies JEE application development, reducing boiler-plate code and configurations while delivering powerful conversation and flow management.

This plugin has not been [tested](http://code.google.com/p/struts2-conversation/wiki/Performance?ts=1336013558&updated=Performance) for use in large-scale applications (with tens of thousands of concurrent requests), therefore its scalability has not been verified for such a scenario.  It is strongly advised to perform load-testing on any mission-critical apps with a non-trivial number of users, whether using this library or not.  That said, the testing that has been performed indicates that the conversation-processing overhead is minimal.  Any feedback regarding performance is welcomed.


---

## Features ##
  * unlimited, [nested conversations](http://code.google.com/p/struts2-conversation/wiki/NestingConversations) (well, they are limited, but you choose the limit)
  * [conversation monitoring and clean-up](http://code.google.com/p/struts2-conversation/wiki/UsageGuide#Memory_Management) to avoid memory leaks and bloated sessions
  * annotations and conventions = almost zero configuration
  * integrates with Spring IoC Container
  * integrates with the Convention plugin
  * conversation configurations are built and cached on start-up
  * utility for simple unit testing
  * an extension to the [Struts2 Config Browser](http://mvnrepository.com/artifact/org.apache.struts/struts2-config-browser-plugin/) for easily inspecting the Conversation configuration details at run-time
  * available from [Maven Central](http://repo1.maven.org/maven2/com/googlecode/struts2-conversation/struts2-conversation-scope-plugin/)
  * detailed and helpful logging
  * simple [handling for expired conversations](http://code.google.com/p/struts2-conversation/wiki/UsageGuide#Error_Mapping)
  * the [cleanest session-scope solution](http://code.google.com/p/struts2-conversation/wiki/SessionScope) available


---

## Quick Intro ##
  1. [java](#java.md)
  1. [tag lib](#tag_lib.md)
  1. [struts.xml](#struts.xml.md)
  1. [maven](#maven.md)
  1. [additional topics](http://code.google.com/p/struts2-conversation/wiki/UsageGuide)

---

### java ###
```
@ConversationController
public class RegistrationController extends ActionSupport implements ModelDriven<RegistrationModel> {

     //this field now a member of the "registration" conversation
     @ConversationField
     private RegistrationModel registrationModel; 

     //because method name starts with "begin", this action initiates the conversation
     public String beginRegistration() {
          return SUCCESS;
     }

     //this action is an intermediate member of the conversation since the
     //method name does not start with "begin" or "end"
     public String continueRegistration() {
          return SUCCESS;
     }

     //because method name starts with "end", the conversation is ended following
     //execution of this action
     public String endRegistration() {
          return SUCCESS;
     }

     public RegistrationModel getModel() {
          return registrationModel;
     }
}
```

---

### tag lib ###
The sc:form and sc:url tags should be used to pass the conversation ids on the request:
```
<%@ taglib uri="/struts-tags" prefix="s"%> 
<%@ taglib uri="/struts-conversation-tags" prefix="sc"%>    
 
     <!-- a Struts2 form tag that will persist the conversation IDs -->  
     <sc:form action="continue-registration">
	 ...
     </sc:form>

     <!-- a Struts2 url tag that will persist the conversation IDs --> 
     <sc:url action="continue-registration"/>

     <!-- a custom tag that will include the conversation IDs as hidden fields -->
     <!-- could be used, for instance, inside a Struts2 jQuery Plugin form tag -->
     <sc:conversations/>
  
```

---

### struts.xml ###
using a default stack:
```
<struts>
	
	<package name="default" extends="struts-conversation-default"/>

</struts>
```
using a custom stack:
```
<struts>
	
	<package name="default" extends="struts-conversation-default">
	
		<interceptors>

			<interceptor-stack name="customStack">
				<interceptor-ref name="conversation" />
				<interceptor-ref name="defaultStack" />
			</interceptor-stack>

		</interceptors>

		<default-interceptor-ref name="customStack" />
		
	</package>

        
</struts>
```
Note:  As the interceptors inject the conversation fields, it is important to locate
them higher in the stack than any interceptors that will depend on the fields having already been injected.

---


## maven ##
latest version dependency:

```
<dependency>
  <groupId>com.googlecode.struts2-conversation</groupId>
  <artifactId>struts2-conversation-scope-plugin</artifactId>
  <version>1.7.4</version>
</dependency>
```

optional extension for use with the struts2-config-browser-plugin:
```
<dependency>
  <groupId>com.googlecode.struts2-conversation</groupId>
  <artifactId>struts2-conversation-config-browser-extension</artifactId>
  <version>1.7.4</version>
</dependency>
```


other than the standard Struts2 dependencies, this plugin also requires an slf4j implementation such as the log4j one that can be included with this dependency:

```
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-log4j12</artifactId>
  <version>1.6.6</version>
</dependency>
```

If you are using a Struts version earlier than 2.3.3, then you must also include the Commons Lang 3 library:

```
<dependency>
   <groupId>org.apache.commons</groupId>
   <artifactId>commons-lang3</artifactId>
   <version>3.1</version>
</dependency>
```