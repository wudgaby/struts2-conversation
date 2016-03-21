## Conversation Plugin vs. ScopedModelDrivenInterceptor Load Test Comparison ##

To get some gauge of the performance cost of using the Conversation Plugin, a tweaked version of the [simple-annotation-example app](http://code.google.com/p/struts2-conversation/wiki/EclipseAndMavenGuide) was deployed on the [Heroku](http://www.heroku.com/) Cedar Stack using the free, single Web Dyno.

For comparison, the same class was configured in two different packages in the struts.xml:  in one package, the Conversation Plugin's conversation interceptor was applied, in the other package the XWork scopedModelDriven interceptor was applied.

The begin action was chosen for the test URL for both packages.  The first begin action of a session is the most expensive conversation action:  various session-scoped conversation components are not created until first needed.  For both packages, the Jetty container (the container used on the Cedar Stack) has to create new HttpSessions for each request.

The [Blitz load testing app](http://blitz.io/) was used to perform load testing.

The free version of Blitz will simulate up to 250 concurrent users making repeated requests to a URL for 1 minute.  This ends up being about 7,000 requests in a minute.  If the response is not received within 1 second, the request times-out.

The results support the assumption that the conversation-processing overhead is minimal and that it compares favorably with the performance of the ScopedModelDrivenInterceptor.  Yet the conversation approach is simpler to configure, simpler to code in the action classes, and offers greater functionality than the basic session-scope approach.

These tests were run multiple times.

The graphs below display a typical set of results of from these tests:


---


## Conversation Plugin Performance ##

http://struts2-conversation.googlecode.com/svn/wiki/img/heroku-conversation-response-times-6-23-2012.PNG


---


## ScopedModelDrivenInterceptor Performance ##

http://struts2-conversation.googlecode.com/svn/wiki/img/heroku-scoped-model-response-times-6-23-2012.PNG


---


## struts.xml ##

```
<?xml version="1.0" encoding="UTF-8" ?>

<!DOCTYPE struts PUBLIC
    "-//Apache Software Foundation//DTD Struts Configuration 2.1.7//EN"
    "http://struts.apache.org/dtds/struts-2.1.7.dtd">

<struts>

	<constant name="struts.scope.followsConvention" value="true" /> 
	<constant name="struts.convention.action.suffix" value="Controller" />
	
	<constant name="conversation.package.nesting" value="false" /> 
	<constant name="conversation.monitoring.frequency" value="90000"/> 
	<constant name="conversation.idle.timeout" value="180000"/> 
	<constant name="conversation.max.instances" value="5"/>
	<constant name="conversation.monitoring.thread.pool.size" value="3"/>

	<bean type="org.byars.struts2.example.services.RegistrationService" class="org.byars.struts2.example.services.RegistrationServiceImpl" />

	<package name="registration" namespace="/registration" extends="struts-conversation-default">
	
		<interceptors>
			
			<interceptor-stack name="registrationStack">
				<interceptor-ref name="conversation" />
				<interceptor-ref name="i18n" />
				<interceptor-ref name="modelDriven" />
				<interceptor-ref name="actionMappingParams"/>
				<interceptor-ref name="params">
					<param name="excludeParams">dojo\..*</param>
				</interceptor-ref>
				<interceptor-ref name="conversionError" />
				<interceptor-ref name="validation"/>
				<interceptor-ref name="workflow">
					<param name="excludeMethods">input,back,cancel,browse</param>
				</interceptor-ref>
			</interceptor-stack>
			
		</interceptors>
		
		<default-interceptor-ref name="registrationStack" />

		<action name="*" class="org.byars.struts2.example.actions.RegistrationController" method="{1}">
			<result name="success">{1}-success.jsp</result>
		</action>

	</package>
	
	<package name="registration2" namespace="/registration2" extends="struts-conversation-default">
	
		<interceptors>
			
			<interceptor-stack name="registrationStack">
				<interceptor-ref name="scopedModelDriven">
					<param name="scope">session</param>  
                	<param name="name">model</param>  
                	<param name="className">org.byars.struts2.example.models.RegistrationContext</param>  
				</interceptor-ref>
				<interceptor-ref name="modelDriven"/>
				<interceptor-ref name="i18n" />
				<interceptor-ref name="actionMappingParams"/>
				<interceptor-ref name="params">
					<param name="excludeParams">dojo\..*</param>
				</interceptor-ref>
				<interceptor-ref name="conversionError" />
				<interceptor-ref name="validation"/>
				<interceptor-ref name="workflow">
					<param name="excludeMethods">input,back,cancel,browse</param>
				</interceptor-ref>
			</interceptor-stack>
			
		</interceptors>
		
		<default-interceptor-ref name="registrationStack" />

		<action name="*" class="org.byars.struts2.example.actions.RegistrationController" method="{1}">
			<result name="success">{1}-success.jsp</result>
		</action>

	</package>

</struts>
```