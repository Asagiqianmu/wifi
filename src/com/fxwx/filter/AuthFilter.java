package com.fxwx.filter;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;

public interface AuthFilter extends ActionFilter {
	
	View match(ActionContext context);
	
}
