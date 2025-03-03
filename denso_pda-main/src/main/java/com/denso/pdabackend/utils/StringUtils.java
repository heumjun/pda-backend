package com.denso.pdabackend.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

public final class StringUtils {

	private static final String ALPHA_NUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final Random RANDOM = new Random();

	public static String toString(Object target) {
		return target == null ? null : target.toString();
	}

	public static String abbreviate(Object target, int maxSize) {
		Validate.isTrue(maxSize >= 3, "Maximum size must be greater or equal to 3");
		if (target == null) {
			return null;
		} else {
			String str = target.toString();
			if (str.length() <= maxSize) {
				return str;
			} else {
				StringBuilder strBuilder = new StringBuilder(maxSize + 2);
				strBuilder.append(str, 0, maxSize - 3);
				strBuilder.append("...");
				return strBuilder.toString();
			}
		}
	}

	public static Boolean equals(Object first, Object second) {
		if (first == null && second == null) {
			return Boolean.TRUE;
		} else {
			return first != null && second != null ? first.toString().equals(second.toString()) : Boolean.FALSE;
		}
	}

	public static Boolean equalsIgnoreCase(Object first, Object second) {
		if (first == null && second == null) {
			return Boolean.TRUE;
		} else {
			return first != null && second != null ? first.toString().equalsIgnoreCase(second.toString()) : Boolean.FALSE;
		}
	}

	public static Boolean contains(Object target, String fragment) {
		Validate.notNull(target, "Cannot apply contains on null");
		Validate.notNull(fragment, "Fragment cannot be null");
		return target.toString().contains(fragment);
	}

	public static Boolean containsIgnoreCase(Object target, String fragment, Locale locale) {
		Validate.notNull(target, "Cannot apply containsIgnoreCase on null");
		Validate.notNull(fragment, "Fragment cannot be null");
		Validate.notNull(locale, "Locale cannot be null");
		return target.toString().toUpperCase(locale).contains(fragment.toUpperCase(locale));
	}

	public static Boolean startsWith(Object target, String prefix) {
		Validate.notNull(target, "Cannot apply startsWith on null");
		Validate.notNull(prefix, "Prefix cannot be null");
		return target.toString().startsWith(prefix);
	}

	public static Boolean endsWith(Object target, String suffix) {
		Validate.notNull(target, "Cannot apply endsWith on null");
		Validate.notNull(suffix, "Suffix cannot be null");
		return target.toString().endsWith(suffix);
	}

	public static String substring(Object target, int beginIndex, int endIndex) {
		if (target == null) {
			return null;
		} else {
			Validate.isTrue(beginIndex >= 0, "Begin index must be >= 0");
			return new String(target.toString().substring(beginIndex, endIndex));
		}
	}

	public static String substring(Object target, int beginIndex) {
		if (target == null) {
			return null;
		} else {
			String str = target.toString();
			int len = str.length();
			Validate.isTrue(beginIndex >= 0 && beginIndex < len, "beginIndex must be >= 0 and < " + len);
			return str.substring(beginIndex);
		}
	}

	public static String substringAfter(Object target, String substr) {
		Validate.notNull(substr, "Parameter substring cannot be null");
		if (target == null) {
			return null;
		} else {
			String str = target.toString();
			int index = str.indexOf(substr);
			return index < 0 ? null : str.substring(index + substr.length());
		}
	}

	public static String substringBefore(Object target, String substr) {
		Validate.notNull(substr, "Parameter substring cannot be null");
		if (target == null) {
			return null;
		} else {
			String str = target.toString();
			int index = str.indexOf(substr);
			return index < 0 ? null : new String(str.substring(0, index));
		}
	}

	public static String prepend(Object target, String prefix) {
		Validate.notNull(prefix, "Prefix cannot be null");
		return target == null ? null : prefix + target;
	}

	public static String append(Object target, String suffix) {
		Validate.notNull(suffix, "Suffix cannot be null");
		return target == null ? null : target + suffix;
	}

	public static String repeat(Object target, int times) {
		if (target == null) {
			return null;
		} else {
			String str = target.toString();
			StringBuilder strBuilder = new StringBuilder(str.length() * times + 10);

			for(int i = 0; i < times; ++i) {
				strBuilder.append(str);
			}

			return strBuilder.toString();
		}
	}

	public static String concat(Object... values) {
		return concatReplaceNulls("", values);
	}

	public static String concatReplaceNulls(String nullValue, Object... values) {
		if (values == null) {
			return "";
		} else {
			StringBuilder sb = new StringBuilder();
			Object[] var3 = values;
			int var4 = values.length;

			for(int var5 = 0; var5 < var4; ++var5) {
				Object value = var3[var5];
				if (value == null) {
					sb.append(nullValue);
				} else {
					sb.append(value.toString());
				}
			}

			return sb.toString();
		}
	}

	public static Integer indexOf(Object target, String fragment) {
		Validate.notNull(target, "Cannot apply indexOf on null");
		Validate.notNull(fragment, "Fragment cannot be null");
		return target.toString().indexOf(fragment);
	}

	public static boolean isEmpty(String target) {
		return target == null || target.length() == 0;
	}

	public static boolean isEmptyOrWhitespace(String target) {
		if (target == null) {
			return true;
		} else {
			int targetLen = target.length();
			if (targetLen == 0) {
				return true;
			} else {
				char c0 = target.charAt(0);
				if ((c0 < 'a' || c0 > 'z') && (c0 < 'A' || c0 > 'Z')) {
					for(int i = 0; i < targetLen; ++i) {
						char c = target.charAt(i);
						if (c != ' ' && !Character.isWhitespace(c)) {
							return false;
						}
					}

					return true;
				} else {
					return false;
				}
			}
		}
	}

	public static String join(Object[] target, String separator) {
		Validate.notNull(separator, "Separator cannot be null");
		if (target == null) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder();
			if (target.length > 0) {
				sb.append(target[0]);

				for(int i = 1; i < target.length; ++i) {
					sb.append(separator);
					sb.append(target[i]);
				}
			}

			return sb.toString();
		}
	}

	public static String join(Iterable<?> target, String separator) {
		Validate.notNull(separator, "Separator cannot be null");
		if (target == null) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder();
			Iterator<?> it = target.iterator();
			if (it.hasNext()) {
				sb.append(it.next());

				while(it.hasNext()) {
					sb.append(separator);
					sb.append(it.next());
				}
			}

			return sb.toString();
		}
	}

	public static String join(Iterable<?> target, char separator) {
		if (target == null) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder();
			Iterator<?> it = target.iterator();
			if (it.hasNext()) {
				sb.append(it.next());

				while(it.hasNext()) {
					sb.append(separator);
					sb.append(it.next());
				}
			}

			return sb.toString();
		}
	}

	public static String[] split(Object target, String separator) {
		Validate.notNull(separator, "Separator cannot be null");
		if (target == null) {
			return null;
		} else {
			StringTokenizer strTok = new StringTokenizer(target.toString(), separator);
			int size = strTok.countTokens();
			String[] array = new String[size];

			for(int i = 0; i < size; ++i) {
				array[i] = strTok.nextToken();
			}

			return array;
		}
	}

	public static Integer length(Object target) {
		Validate.notNull(target, "Cannot apply length on null");
		return target.toString().length();
	}

	public static String replace(Object target, String before, String after) {
		Validate.notNull(before, "Parameter \"before\" cannot be null");
		Validate.notNull(after, "Parameter \"after\" cannot be null");
		if (target == null) {
			return null;
		} else {
			String targetStr = target.toString();
			int targetStrLen = targetStr.length();
			int beforeLen = before.length();
			if (targetStrLen != 0 && beforeLen != 0) {
				int index = targetStr.indexOf(before);
				if (index < 0) {
					return targetStr;
				} else {
					StringBuilder stringBuilder = new StringBuilder(targetStrLen + 10);

					int lastPos;
					for(lastPos = 0; index >= 0; index = targetStr.indexOf(before, lastPos)) {
						stringBuilder.append(targetStr, lastPos, index);
						stringBuilder.append(after);
						lastPos = index + beforeLen;
					}

					stringBuilder.append(targetStr, lastPos, targetStrLen);
					return stringBuilder.toString();
				}
			} else {
				return targetStr;
			}
		}
	}

	public static String toUpperCase(Object target, Locale locale) {
		Validate.notNull(locale, "Locale cannot be null");
		return target == null ? null : target.toString().toUpperCase(locale);
	}

	public static String toLowerCase(Object target, Locale locale) {
		Validate.notNull(locale, "Locale cannot be null");
		return target == null ? null : target.toString().toLowerCase(locale);
	}

	public static String trim(Object target) {
		return target == null ? null : target.toString().trim();
	}

	public static String pack(String target) {
		if (target == null) {
			return null;
		} else {
			int targetLen = target.length();
			StringBuilder strBuilder = null;

			for(int i = 0; i < targetLen; ++i) {
				char c = target.charAt(i);
				if (!Character.isWhitespace(c) && c > ' ') {
					if (strBuilder != null) {
						strBuilder.append(c);
					}
				} else if (strBuilder == null) {
					strBuilder = new StringBuilder();
					strBuilder.append(target, 0, i);
				}
			}

			return strBuilder == null ? target.toLowerCase() : strBuilder.toString().toLowerCase();
		}
	}

	public static String capitalize(Object target) {
		if (target == null) {
			return null;
		} else {
			StringBuilder result = new StringBuilder(target.toString());
			if (result.length() > 0) {
				result.setCharAt(0, Character.toTitleCase(result.charAt(0)));
			}

			return result.toString();
		}
	}

	public static String unCapitalize(Object target) {
		if (target == null) {
			return null;
		} else {
			StringBuilder result = new StringBuilder(target.toString());
			if (result.length() > 0) {
				result.setCharAt(0, Character.toLowerCase(result.charAt(0)));
			}

			return result.toString();
		}
	}

	private static int findNextWord(char[] buffer, int idx, char[] delimiterChars) {
		int len = buffer.length;
		if (idx >= 0 && idx < len) {
			boolean foundDelimiters = idx == 0;

			for(int i = idx; i < len; ++i) {
				char ch = buffer[i];
				boolean isDelimiter = delimiterChars == null ? Character.isWhitespace(ch) : Arrays.binarySearch(delimiterChars, ch) >= 0;
				if (isDelimiter) {
					foundDelimiters = true;
				} else if (foundDelimiters) {
					return i;
				}
			}

			return -1;
		} else {
			return -1;
		}
	}

	public static String capitalizeWords(Object target) {
		return capitalizeWords(target, (Object)null);
	}

	public static String capitalizeWords(Object target, Object delimiters) {
		if (target == null) {
			return null;
		} else {
			char[] buffer = target.toString().toCharArray();
			char[] delimiterChars = delimiters == null ? null : delimiters.toString().toCharArray();
			if (delimiterChars != null) {
				Arrays.sort(delimiterChars);
			}

			int idx = 0;

			for(idx = findNextWord(buffer, idx, delimiterChars); idx != -1; idx = findNextWord(buffer, idx, delimiterChars)) {
				buffer[idx] = Character.toTitleCase(buffer[idx]);
				++idx;
			}

			return new String(buffer);
		}
	}

	public static String randomAlphanumeric(int count) {
		StringBuilder strBuilder = new StringBuilder(count);
		int anLen = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".length();
		synchronized(RANDOM) {
			for(int i = 0; i < count; ++i) {
				strBuilder.append("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(RANDOM.nextInt(anLen)));
			}

			return strBuilder.toString();
		}
	}

	private StringUtils() {
	}
}
