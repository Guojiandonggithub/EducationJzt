package org.springside.examples.bootapi.ToolUtils.common.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

public class NumberX {

	public static int getRandom(int max) {
		return new Random().nextInt(max);
	}

	public static int getRandom(int min, int max) {
		int r = getRandom(max - min);
		return r + min;
	}

	public static int bytes2int(byte[] b) {
		int mask = 255;
		int temp = 0;
		int res = 0;
		for (int i = 0; i < b.length; i++) {
			res <<= 8;
			temp = b[i] & mask;
			res |= temp;
		}
		return res;
	}

	public static long bytes2long(byte[] v) {
		return (v[0] << 56) + ((v[1] & 0xFF) << 48) + ((v[2] & 0xFF) << 40)
				+ ((v[3] & 0xFF) << 32) + ((v[4] & 0xFF) << 24)
				+ ((v[5] & 0xFF) << 16) + ((v[6] & 0xFF) << 8)
				+ ((v[7] & 0xFF) << 0);
	}

	public static byte[] int2bytes(int num) {
		return int2bytes(num, 4);
	}

	public static byte[] int2bytes(int num, int len) {
		byte[] b = new byte[len];

		for (int i = 0; i < len; i++)
			b[i] = (byte) (num >>> 24 - i * 8);
		return b;
	}

	public static byte[] long2bytes(long v) {
		byte[] value = new byte[8];
		value[0] = (byte) (int) (v >>> 56);
		value[1] = (byte) (int) (v >>> 48);
		value[2] = (byte) (int) (v >>> 40);
		value[3] = (byte) (int) (v >>> 32);
		value[4] = (byte) (int) (v >>> 24);
		value[5] = (byte) (int) (v >>> 16);
		value[6] = (byte) (int) (v >>> 8);
		value[7] = (byte) (int) (v >>> 0);
		return value;
	}

	public static int getSafeIndex(int index, int length) {
		return index % length + (index < 0 ? length : 0);
	}

	public static int getSafeRange(int n, int min, int max) {
		if (n < min)
			return min;
		if (n > max)
			return max;
		return n;
	}

	public static String getNumberString(Double number) {
		if (number == null)
			return "";
		NumberFormat formatter = new DecimalFormat("#.########");
		String string = formatter.format(number.doubleValue());
		return string;
	}

	public static String getNumberString(double number) {
		return getNumberString(new Double(number));
	}

	public static int getInt(String s, int defaultValue) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			System.err.println("NumberX.getInt(): " + e);
		}
		return defaultValue;
	}

	public static double getDouble(String s, double defaultValue) {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			System.err.println("NumberX.getDouble(): " + e);
		}
		return defaultValue;
	}

	public static int power(int n, int m) {
		int rt = 1;
		for (int i = 0; i < m; i++)
			rt *= n;
		return rt;
	}

	public static double sum(double[] values) {
		if (values == null)
			return 0.0D;
		double rt = 0.0D;
		for (int i = 0; i < values.length; i++)
			rt += values[i];
		return rt;
	}

	public static double[] radio(double[] values) {
		if (values == null)
			return null;
		double sum = sum(values);
		int n = values.length;
		double[] rt = new double[n];
		for (int i = 0; i < n; i++)
			rt[i] = (sum == 0.0D ? (0.0D / 0.0D) : values[i] / sum);
		return rt;
	}

	public static boolean hasZero(double[] values) {
		if (values == null)
			return false;
		for (int i = 0; i < values.length; i++)
			if (values[i] == 0.0D)
				return true;
		return false;
	}

	public static double[] zeros(double[] values) {
		if (values == null)
			return null;
		int n = values.length;
		double[] rt = new double[n];
		for (int i = 0; i < n; i++)
			rt[i] = (values[i] == 0.0D ? 0.0D : 1.0D);
		return rt;
	}

	public static double[] to_double(int[] values) {
		if (values == null)
			return null;
		double[] rt = new double[values.length];
		for (int i = 0; i < values.length; i++)
			rt[i] = values[i];
		return rt;
	}

	public static int[] to_int(double[] values) {
		if (values == null)
			return null;
		int[] rt = new int[values.length];
		for (int i = 0; i < values.length; i++)
			rt[i] = (int) values[i];
		return rt;
	}

	public static double[] multiply(double[] values, double[] radios) {
		if ((values == null) || (radios == null))
			return null;
		if (values.length != radios.length)
			return null;
		int n = values.length;
		double[] rt = new double[n];
		for (int i = 0; i < n; i++)
			values[i] *= radios[i];
		return rt;
	}

	public static double[] adjustSum(double[] v0, double[] v1) {
		if ((v0 == null) || (v1 == null))
			return null;
		if ((v0.length != v1.length) || (!hasZero(v1)))
			return v1;
		int n = v0.length;

		double sum1 = sum(v0) - sum(v1);
		double[] zeros = zeros(v1);
		double[] v2 = multiply(v0, zeros);
		double[] radio = radio(v2);
		double[] rt = new double[n];
		for (int i = 0; i < n; i++)
			v1[i] += sum1 * radio[i];
		return rt;
	}

	public static double[] adjustRound(double[] values, int index) {
		if (values == null)
			return null;
		int n = values.length;
		double[] rt = new double[n];
		double sum0 = Math.round(sum(values));
		for (int i = 0; i < n; i++)
			rt[i] = Math.round(values[i]);
		double sum1 = sum(rt);
		if (sum0 != sum1) {
			double delta = sum0 - sum1;
			int safeIndex = getSafeIndex(index, values.length);
			rt[safeIndex] += Math.round(delta);
		}
		return rt;
	}

	public static boolean isDigit(String value) {
		if (value == null)
			return false;
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++)
			if (!Character.isDigit(chars[i]))
				return false;
		return true;
	}

	public static int[] Excel2Ints(String code) {
		int[] rt = { -1, -1 };
		if ((code == null) || (code.length() < 2)) {
			System.err.println("NumberX.Excel2Ints() 输入参数不对：" + code);
			return rt;
		}
		char[] chars = code.toCharArray();
		if (!Character.isLetter(chars[0])) {
			System.err.println("NumberX.Excel2Ints() 输入参数不对：" + code);
			return rt;
		}
		int colLen = Character.isLetter(chars[1]) ? 2 : 1;
		String col = code.substring(0, colLen);
		String row = code.substring(colLen);

		if (!isDigit(row)) {
			System.err.println("NumberX.Excel2Ints() 输入参数不对：" + code);
			return rt;
		}

		int col_i = AZ2Int(col);
		int row_i = getInt(row, 0);
		if (row_i == 0) {
			System.err.println("NumberX.Excel2Ints() 输入参数不对：" + code);
			return rt;
		}

		return new int[] { col_i, row_i - 1 };
	}

	public static int AZ2Int(String AZ) {
		int range = 26;
		if ((AZ == null) || ((AZ.length() != 1) && (AZ.length() != 2))) {
			System.err.println("NumberX.AZtoInt() 列代号不符合要求：" + AZ);
			return -1;
		}
		AZ = AZ.toUpperCase();
		if (AZ.length() == 1)
			return AZ.charAt(0) - 'A';
		char c0 = AZ.charAt(0);
		char c1 = AZ.charAt(1);
		return (c0 - 'A' + 1) * range + (c1 - 'A');
	}

	public static String Int2AZ(int i) {
		int range = 26;
		if ((i < 0) || (i > 255)) {
			System.err.println("列标号出界[0-255]：" + i);
			return null;
		}
		if (i < range)
			return String.valueOf((char) (65 + i));
		return String.valueOf((char) (65 + i / range - 1)
				+ (char) (65 + i % range));
	}
}