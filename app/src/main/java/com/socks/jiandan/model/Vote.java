package com.socks.jiandan.model;

/**
 * Created by zhaokaiqiang on 15/4/10.
 */
public class Vote {

	public static final String URL_VOTE = "http://jandan.net/index" +
			".php?acv_ajax=true&option=%s&ID=%s";

	public static final String XX = "0";
	public static final String OO = "1";

	public static final String RESULT_XX_SUCCESS = "-1";
	public static final String RESULT_OO_SUCCESS = "1";
	public static final String RESULT_HAVE_VOTED = "0";

	private String result;
	private String message;

	public enum aa {
		AAA(0x1),
		BBB(0x2),
		CCC(0x4),
		DDD(0x8);

		private aa(int i) {
		}
	}

	public Vote() {
	}

	public Vote(String result, String message) {
		this.result = result;
		this.message = message;
	}

	public static String getXXUrl(String id) {
		return String.format(URL_VOTE, XX, id);
	}

	public static String getOOUrl(String id) {
		return String.format(URL_VOTE, OO, id);
	}

	/**
	 * 从请求结果中获取一个Vote对象
	 *
	 * @param response
	 * @return
	 */
	public static Vote getInstance(String response) {
		String[] results = response.split("\\|");
		return new Vote(results[2], results[1]);
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Vote{" +
				"result=" + result +
				", message='" + message + '\'' +
				'}';
	}
}
