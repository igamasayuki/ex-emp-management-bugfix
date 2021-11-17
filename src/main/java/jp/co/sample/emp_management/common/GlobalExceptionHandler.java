package jp.co.sample.emp_management.common;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GlobalExceptionHandler implements HandlerExceptionResolver {

	private static final Logger LOGGER = 
			LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
		@Override
		public ModelAndView resolveException(HttpServletRequest request,
				javax.servlet.http.HttpServletResponse response, Object handler, java.lang.Exception ex) {
			LOGGER.error("システムエラーが発生しました！",ex);
			return null;
		}

}