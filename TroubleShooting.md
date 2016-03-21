This page covers solutions to simple setup/detail issues that can be big headaches.


---

## Dependencies ##

The plugin uses slf4j for logging.  Without having the slf4j jars in your WAR file, the plugin's classes cannot be loaded.

To download the API, go [here](http://repo2.maven.org/maven2/org/slf4j/slf4j-api/1.6.6/).

The API will also need an implementation.  I use log4j.

Download it [here](http://repo2.maven.org/maven2/org/slf4j/slf4j-log4j12/1.6.6/).

And for Maven:

```
<dependency>
   <groupId>org.slf4j</groupId>
   <artifactId>slf4j-log4j12</artifactId>
   <version>1.6.6</version>
</dependency>
```


---

## Action Conversation Flow and Configuration ##

Sometimes with a larger application, it may become difficult to keep track of and quickly trouble shoot the conversation-flow aspects of all the actions.

The Conversation Config Browser is a simple to use drop-in extension to the [Struts2 Config Browser Plugin](http://mvnrepository.com/artifact/org.apache.struts/struts2-config-browser-plugin/2.3.4) that includes a tab for viewing the Conversation flow details of each action in the application at run-time.

The extension is available from the Downloads link above or though Maven with the following dependency:

```
<dependency>
   <groupId>com.googlecode.struts2-conversation</groupId>
   <artifactId>struts2-conversation-config-browser-extension</artifactId>
   <version>${struts.conversation.version}</version>
</dependency>
```

To use the config browser, include it in your WAR file, startup your app, and go to {application\_root}/conversation-config-browser/index.action, and from there you can select actions and click on the "Conversations" tab to view each action's conversation details.


---

## Conversation-Scoped Bean and Field Names ##

Be sure early on to develop a convention for naming your conversation-scoped beans and fields.

With the @ConversationField annotation, the default name of the field is going to be the fields actual name, e.g. "String myString" would be identified in the conversation by the name "myString".  Pretty simple.

But, there is room to screw it up.  For instance, some people might use non-unique names for models in ModelDriven actions, calling them all "model".  If this is done with each of them being ConversationFields, then, depending on the flow, the fields all called "model" could clobber each other if they occur within a single conversation.

To avoid this clobbering, you have two options: (1) you can name the models all something other than "model", or (2) you can specify a different name using the @ConversationField annotation:

```
@ConversationField(Models.SHOPPING_CART) //Models.SHOPPING_CART being a String constant
private ShoppingCartContext model;
```

So, as long as you are aware of the implications of the field names and stick to a sensible naming convention, its pretty easy.