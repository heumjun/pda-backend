package com.denso.pdabackend.utils;

import java.util.Collection;
import java.util.Iterator;

public final class Validate {

	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void notEmpty(String object, String message) {
		if (StringUtils.isEmptyOrWhitespace(object)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void notEmpty(Collection<?> object, String message) {
		if (object == null || object.size() == 0) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void notEmpty(Object[] object, String message) {
		if (object == null || object.length == 0) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void containsNoNulls(Iterable<?> collection, String message) {
		Iterator var2 = collection.iterator();

		while(var2.hasNext()) {
			Object object = var2.next();
			notNull(object, message);
		}

	}

	public static void containsNoEmpties(Iterable<String> collection, String message) {
		Iterator var2 = collection.iterator();

		while(var2.hasNext()) {
			String object = (String)var2.next();
			notEmpty(object, message);
		}

	}

	public static void containsNoNulls(Object[] array, String message) {
		Object[] var2 = array;
		int var3 = array.length;

		for(int var4 = 0; var4 < var3; ++var4) {
			Object object = var2[var4];
			notNull(object, message);
		}

	}

	public static void isTrue(boolean condition, String message) {
		if (!condition) {
			throw new IllegalArgumentException(message);
		}
	}

	private Validate() {
	}

}
