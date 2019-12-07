package com.superbank.utils;

import java.lang.reflect.Type;
import java.net.http.HttpRequest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Primitives;

public class ConversionUtils {

	public static HttpRequest.BodyPublisher buildFormData(Object object) {
		String jsonObject = new Gson().toJson(object);
		
        return HttpRequest.BodyPublishers.ofString(jsonObject);
    }
	
	public static String toJson(Object object) {
		return new Gson().toJson(object);
	}
	
	public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
	    Object object = new Gson().fromJson(json, (Type) classOfT);
	    return Primitives.wrap(classOfT).cast(object);
	  }
}
