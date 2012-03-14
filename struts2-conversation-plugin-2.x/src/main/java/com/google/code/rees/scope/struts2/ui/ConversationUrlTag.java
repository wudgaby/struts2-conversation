package com.google.code.rees.scope.struts2.ui;

import java.util.Map;

import org.apache.struts2.views.jsp.URLTag;

import com.google.code.rees.scope.struts2.StrutsConversationConstants;

/**
 * A normal Struts2 {@link URLTag}, but additionally includes the
 * conversation IDs as parameters on the URL
 * 
 * @author rees.byars
 */
public class ConversationUrlTag extends URLTag {

    private static final long serialVersionUID = -2799594627916112974L;

    @Override
    protected void populateParams() {
        super.populateParams();
        @SuppressWarnings("unchecked")
        Map<String, String> convoIdMap = (Map<String, String>) this.component
                .getStack().findValue(
                        StrutsConversationConstants.CONVERSATION_ID_MAP_STACK_KEY);
        this.component.addAllParameters(convoIdMap);
    }

}