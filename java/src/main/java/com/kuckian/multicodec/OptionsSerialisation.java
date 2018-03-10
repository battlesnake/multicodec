package com.kuckian.multicodec;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class OptionsSerialisation {

	public static final char specDelim = '+';

	public static final char argDelim = ';';
	public static final char argEnter = '(';
	public static final char argExit = ')';
	public static final char argAssign = '=';

	public static String serialiseScheme(CodingScheme layers) {
		return serialiseLayers(layers.getLayers());
	}

	public static String serialiseLayers(List<CodingLayer> layers) {
		return layers.stream().map(layer -> serialiseLayer(layer)).collect(Collectors.joining("" + argDelim));
	}

	public static String serialiseLayer(CodingLayer layer) {
		return serialiseLayer(layer.codec.getName(), layer.options);
	}

	public static String serialiseLayer(String name, Object options) {
		Map<String, String> args = options == null ? null : serialiseOptions(options);
		return args.isEmpty() ? name
				: name + '('
						+ args.entrySet().stream().filter((kv) -> kv.getValue() != null).map(
								(kv) -> (kv.getValue() == "" ? kv.getKey() : kv.getKey() + argAssign + kv.getValue()))
								.collect(Collectors.joining(";"))
						+ ')';
	}

	public static String serialisePrimitiveOption(Object o) {
		if (boolean.class.equals(o.getClass())) {
			return (Boolean) o ? "" : null;
		} else {
			return o.toString();
		}
	}

	public static String serialiseOption(Class<?> type, Object o) {
		if (o == null) {
			return null;
		} else if (type.isEnum()) {
			/* ENUM NAME NOT SANITISED / VALIDATED */
			return ((Enum<?>) o).name();
		} else if (type.isPrimitive()) {
			return serialisePrimitiveOption(o);
		} else {
			throw new Error("Unsupported type");
		}
	}

	public static Map<String, String> serialiseOptions(Object options) {
		Map<String, String> args = new HashMap<>();
		if (options == null) {
			return args;
		}
		for (Field field : options.getClass().getDeclaredFields()) {
			String name = field.getName();
			Class<?> type = field.getType();
			if (Modifier.isTransient(type.getModifiers())) {
				continue;
			}
			field.setAccessible(true);
			Object value;
			try {
				value = field.get(options);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new Error("Codec options serialisation failed", e);
			}
			args.put(name, serialiseOption(type, value));
		}
		return args;
	}

	public static CodingScheme deserialiseScheme(String s, Direction direction) throws InvalidCodingSchemeException {
		return new CodingScheme(deserialiseLayers(s, direction));
	}

	public static List<CodingLayer> deserialiseLayers(String s, Direction direction)
			throws InvalidCodingSchemeException {
		List<CodingLayer> layers = new ArrayList<>();
		for (String t : s.split(Pattern.quote("" + specDelim))) {
			layers.add(deserialiseLayer(t, direction));
		}
		return layers;
	}

	public static Class<?> getOptionsType(String name, Direction direction) {
		switch (direction) {
		case DECODE:
			return CodecRegistry.getCodec(name).getDecoderOptionsClass();
		case ENCODE:
			return CodecRegistry.getCodec(name).getEncoderOptionsClass();
		default:
			throw new Error("Invalid direction");
		}
	}

	private static class Pair {
		public final String left;
		public final String right;

		public Pair(String s, char delim) {
			int idx = s.indexOf(delim);
			if (idx == -1) {
				left = s;
				right = "";
			} else {
				left = s.substring(0, idx);
				right = s.substring(idx + 1);
			}
		}
	}

	public static CodingLayer deserialiseLayer(String s, Direction direction) throws InvalidCodingSchemeException {
		/* Name */
		Pair nameArgs = new Pair(s, argEnter);
		String name = nameArgs.left;
		if (!nameArgs.right.isEmpty() && !nameArgs.right.endsWith("" + argExit)) {
			throw new InvalidCodingSchemeException("Parameter list not terminated correctly");
		}
		String args = nameArgs.right.isEmpty() ? "" : nameArgs.right.substring(0, nameArgs.right.length() - 1);
		/* Args */
		Class<?> optionsType = getOptionsType(name, direction);
		Object options = deserialiseOptions(optionsType, args);
		return new CodingLayer(CodecRegistry.getCodec(name), options);
	}

	public static Object deserialiseOptions(Class<?> type, String data) throws InvalidCodingSchemeException {
		if (type == null) {
			if (!data.isEmpty()) {
				throw new InvalidCodingSchemeException("Parameters specified for code which accepts no parameters");
			}
			return null;
		}
		Map<String, String> args = new HashMap<>();
		if (!data.isEmpty()) {
			for (String t : data.split(Pattern.quote("" + argDelim))) {
				Pair keyVal = new Pair(t, argAssign);
				args.put(keyVal.left, keyVal.right);
			}
		}
		return deserialiseOptions(type, args);
	}

	public static Object deserialiseOptions(Class<?> type, Map<String, String> data)
			throws InvalidCodingSchemeException {
		Object options;
		try {
			options = type.newInstance();
		} catch (IllegalAccessException | InstantiationException e) {
			throw new Error("Codec options deserialisation failed", e);
		}
		for (Map.Entry<String, String> kv : data.entrySet()) {
			String key = kv.getKey();
			String valueString = kv.getValue();
			Field field;
			try {
				field = type.getField(key);
			} catch (NoSuchFieldException e) {
				throw new InvalidCodingSchemeException("Unrecognised parameter: \"" + key + "\"", e);
			} catch (SecurityException e) {
				throw new Error("Codec options deserialisation failed", e);
			}
			field.setAccessible(true);
			Object value = deserialiseValue(valueString, field);
			try {
				field.set(options, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new Error("Codec options deserialisation failed", e);
			}
		}
		return options;
	}

	public static Object deserialiseValue(String s, Field field) throws InvalidCodingSchemeException {
		Class<?> type = field.getType();
		if (type.isEnum()) {
			return Enum.valueOf(type.asSubclass(Enum.class), s.toUpperCase());
		} else if (boolean.class.equals(type)) {
			if (s != "") {
				throw new InvalidCodingSchemeException("Invalid boolean value");
			}
			return true;
		} else if (byte.class.equals(type)) {
			if (s != "") {
				throw new InvalidCodingSchemeException("Invalid byte value");
			}
			return Byte.parseByte(s);
		} else if (short.class.equals(type)) {
			if (s != "") {
				throw new InvalidCodingSchemeException("Invalid short value");
			}
			return Short.parseShort(s);
		} else if (int.class.equals(type)) {
			if (s != "") {
				throw new InvalidCodingSchemeException("Invalid int value");
			}
			return Integer.parseInt(s);
		} else if (long.class.equals(type)) {
			if (s != "") {
				throw new InvalidCodingSchemeException("Invalid long value");
			}
			return Long.parseLong(s);
		} else if (float.class.equals(type)) {
			if (s != "") {
				throw new InvalidCodingSchemeException("Invalid float value");
			}
			return Float.parseFloat(s);
		} else if (double.class.equals(type)) {
			if (s != "") {
				throw new InvalidCodingSchemeException("Invalid double value");
			}
			return Double.parseDouble(s);
		} else {
			throw new Error("Unsupported type");
		}
	}

}
