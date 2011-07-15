package com.sandwich.util.xml;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

class DeferredInvocation {
	private final Object receiver;
	private final List<String> path;
	private final Method method;
	private final List<String> elements;
	public DeferredInvocation(Object receiver, Class<?>[] args, String methodName, List<String> path, String...elements) {
		this.receiver = receiver;
		try {
			this.method = receiver.getClass().getMethod(methodName, args);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		this.elements = Arrays.asList(elements);
		this.path = path;
	}
	
	public static DeferredInvocation getInstance(Object receiver, String...params){
		if(params != null && params.length > 1){
			if(params[0].startsWith("/")){
				params[0] = params[0].substring(1);
			}
			if(params[0].endsWith("/")){
				params[0] = params[0].substring(0, params[0].length() - 1);
			}
			List<String> path = Arrays.asList(params[0].contains("/") ? params[0].split("/") : params);
			if(params.length == 2){
				Class<?>[] args = new Class[1];
				Arrays.fill(args, String.class);
				return new DeferredInvocation(receiver, args, params[1], path.subList(0, path.size() - 1), 
						params[0].substring(params[0].lastIndexOf("/") + 1));
			}else{
				Class<?>[] args = new Class[params.length - 2];
				Arrays.fill(args, String.class);
				return new DeferredInvocation(receiver, args, params[1], path,
						Arrays.asList(params).subList(2, params.length).toArray(new String[params.length - 2]));
			}
		}
		throw new IllegalArgumentException(receiver+":"+Arrays.toString(params));
	}
	
	Object getReceiver() {
		return receiver;
	}

	List<String> getPath() {
		return path;
	}

	Method getMethod() {
		return method;
	}

	List<String> getElements() {
		return elements;
	}

	@Override
	public String toString() {
		return "DeferredInvocation [path=" + path + ", method=" + (method == null ? null : method.getName())  
				+ ", elements=" + elements + "]";
	}
}
