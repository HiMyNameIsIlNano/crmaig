package com.aig.crm.breadcrumb.interceptor;


import com.aig.crm.breadcrumb.annotations.Link;
import com.aig.crm.breadcrumb.model.BreadCrumbLink;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;


@Component
public class BreadCrumbInterceptor extends HandlerInterceptorAdapter {

	private static final String BREAD_CRUMB_LINKS = "breadCrumb";

	private static final String CURRENT_BREAD_CRUMB_LINKS = "currentBreadCrumb";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Annotation[] declaredAnnotations = getDeclaredAnnotationsForHandler(handler);
		HttpSession session = request.getSession();
		emptyCurrentBreadCrumb(session);
		for (Annotation annotation : declaredAnnotations) {
			if(annotation.annotationType().equals(Link.class)){
				processAnnotation(request, session, annotation);
			}
		}
		
		return true;
	}


	private void emptyCurrentBreadCrumb(HttpSession session) {
		session.setAttribute(CURRENT_BREAD_CRUMB_LINKS, new LinkedList<BreadCrumbLink>());
	}


	private void processAnnotation(HttpServletRequest request, HttpSession session, Annotation annotation) {
		Link link = (Link) annotation;
		String family = link.family();
		
		Map<String, LinkedHashMap<String, BreadCrumbLink>> breadCrumb = getBreadCrumbLinksFromSession(session);
		
		if(breadCrumb == null){
			breadCrumb = new HashMap<>();
			session.setAttribute(BREAD_CRUMB_LINKS, breadCrumb);
		}

		LinkedHashMap<String, BreadCrumbLink> familyMap = breadCrumb.computeIfAbsent(family, k -> new LinkedHashMap<>());


		BreadCrumbLink breadCrumbLink;
		breadCrumbLink = getBreadCrumbLink(request, link, familyMap);
		LinkedList<BreadCrumbLink> currentBreadCrumb = new LinkedList<>();
		generateBreadCrumbsRecursively(breadCrumbLink,currentBreadCrumb);
		session.setAttribute(CURRENT_BREAD_CRUMB_LINKS, currentBreadCrumb);
	}


	private BreadCrumbLink getBreadCrumbLink(HttpServletRequest request, Link link,
                                             LinkedHashMap<String, BreadCrumbLink> familyMap) {
		BreadCrumbLink breadCrumbLink;
		BreadCrumbLink breadCrumbObject = familyMap.get(link.label());
		resetBreadCrumbs(familyMap);

		if (breadCrumbObject != null) {
			breadCrumbObject.setCurrentPage(true);
			breadCrumbLink = breadCrumbObject;
		} else {
			breadCrumbLink = new BreadCrumbLink(link.family(), link.label(), true, link.parent());
			String fullURL = request.getRequestURL().append((request.getQueryString()==null)
					? "" : "?" + request.getQueryString()).toString();

			breadCrumbLink.setUrl(fullURL);
			createRelationships(familyMap, breadCrumbLink);
			familyMap.put(link.label(), breadCrumbLink);
		}

		return breadCrumbLink;
	}


	@SuppressWarnings("unchecked")
	private Map<String, LinkedHashMap<String, BreadCrumbLink>> getBreadCrumbLinksFromSession(HttpSession session) {
		return  (Map<String, LinkedHashMap<String, BreadCrumbLink>>)session.getAttribute(BREAD_CRUMB_LINKS);
	}

	private Annotation[] getDeclaredAnnotationsForHandler(Object handler) {
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		Method method = handlerMethod.getMethod();
		return method.getDeclaredAnnotations();
	}
	
	private void resetBreadCrumbs(LinkedHashMap<String, BreadCrumbLink> familyMap) {
		for(BreadCrumbLink breadCrumbLink : familyMap.values()){
			breadCrumbLink.setCurrentPage(false);
		}
	}

	private void generateBreadCrumbsRecursively(BreadCrumbLink link , LinkedList<BreadCrumbLink> breadCrumbLinks){
		if(link.getPrevious() != null){
			generateBreadCrumbsRecursively(link.getPrevious(), breadCrumbLinks);
		}
		breadCrumbLinks.add(link);
	}
	
	
	private void createRelationships(LinkedHashMap<String, BreadCrumbLink> familyMap , BreadCrumbLink newLink){
		Collection<BreadCrumbLink> values = familyMap.values();
		for (BreadCrumbLink breadCrumbLink : values) {
			if(breadCrumbLink.getLabel().equalsIgnoreCase(newLink.getParentKey())){
					breadCrumbLink.getNext().add(newLink);
					newLink.setPrevious(breadCrumbLink);
					newLink.setParent(breadCrumbLink);
			}
		}
		
	}

}
