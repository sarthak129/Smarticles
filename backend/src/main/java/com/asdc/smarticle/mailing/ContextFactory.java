package com.asdc.smarticle.mailing;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import com.asdc.smarticle.articletag.Tag;

@Component
public class ContextFactory {

	
public Context getContextInstance() {
		
		return new Context();
	}
	
	
}
