package com.jialiujia.mvpbase.util.adb;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * MvpBase
 * Created by Administrator on 2018/1/17.
 */

public class ShellUtils {
	private static final String COMMAND_SH = "sh";
	private static final String COMMAND_SU = "su";
	private static final String COMMAND_EXIT = "exit\n";
	private static final String COMMAND_LINE_END = "\n";

	private ShellUtils() {
		throw new AssertionError();
	}

	/**
	 * 检查root权限
	 * @return 是否有权限
	 */
	public static boolean checkRootPermission() {
		return execCommand("echo root", true, false).getResult() == 0;
	}

	/**
	 * 执行单条命令，默认有返回信息
	 * @param command   命令字符串
	 * @param isRoot    是否需要root权限
	 * @return          返回CommandResult
	 */
	public static CommandResult execCommand(String command, boolean isRoot) {
		return execCommand(new String[]{command}, isRoot, true);
	}

	/**
	 * 执行列表命令，默认有返回信息
	 * @param commands  命令字符串列表
	 * @param isRoot    是否需要root权限
	 * @return          返回CommandResult
	 */
	public static CommandResult execCommand(List<String> commands, boolean isRoot) {
		return execCommand(commands == null ? null : commands.toArray(new String[]{}),
				isRoot, true);
	}

	/**
	 * 执行数组命令，默认有返回信息
	 * @param commands  命令字符串数组
	 * @param isRoot    是否需要root权限
	 * @return          返回CommandResult
	 */
	public static CommandResult execCommand(String[] commands, boolean isRoot) {
		return execCommand(commands, isRoot, true);
	}

	/**
	 * 执行单条命令
	 * @param command   命令字符串
	 * @param isRoot    是否需要root权限
	 * @param isNeedResultMsg   是否返回结果信息
	 * @return          返回CommandResult
	 */
	public static CommandResult execCommand(String command, boolean isRoot,
	                                        boolean isNeedResultMsg) {
		return execCommand(new String[] {command}, isRoot, isNeedResultMsg);
	}

	/**
	 * 执行列表命令
	 * @param commands  命令字符串列表
	 * @param isRoot    是否需要root权限
	 * @param isNeedResultMsg   是否返回结果信息
	 * @return          返回CommandResult
	 */
	public static CommandResult execCommand(List<String> commands, boolean isRoot,
	                                        boolean isNeedResultMsg) {

		return execCommand(commands == null ? null : commands.toArray(new String[]{}),
				isRoot, isNeedResultMsg);
	}


	/**
	 * 执行数组命令
	 * @param commands  命令字符串数组
	 * @param isRoot    是否需要root权限
	 * @param isNeedResultMsg   是否返回结果信息
	 * @return          返回CommandResult
	 */
	public static CommandResult execCommand(String[] commands, boolean isRoot,
	                                        boolean isNeedResultMsg) {

		int result = -1;
		if (commands == null || commands.length == 0) {
			return new CommandResult(result, null, null);
		}

		Process process = null;
		BufferedReader successResult = null;
		BufferedReader errorResult = null;
		StringBuilder msgBuilder;
		String sucMsg = null, errMsg = null;

		DataOutputStream dos = null;

		try {
			process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
			dos = new DataOutputStream(process.getOutputStream());
			for (String command : commands) {
				if (command == null || command.isEmpty()) {
					continue;
				}
				// 防止os.writeBytes写入中文字符
				dos.write(command.getBytes("UTF-8"));
				dos.writeBytes(COMMAND_LINE_END);
				dos.flush();
			}
			dos.flush();
			dos.writeBytes(COMMAND_EXIT);

			result = process.waitFor();
			if (isNeedResultMsg) {
				msgBuilder = new StringBuilder();
				successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
				errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				String s;

				msgBuilder.delete(0, msgBuilder.length());
				while ((s = successResult.readLine()) != null) {
					msgBuilder.append(s);
				}
				sucMsg = msgBuilder.toString();

				msgBuilder.delete(0, msgBuilder.length());
				while ((s = errorResult.readLine()) != null) {
					msgBuilder.append(s);
				}
				errMsg = msgBuilder.toString();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				if (dos != null) {
					dos.close();
				}
				if (successResult != null) {
					successResult.close();
				}
				if (errorResult != null) {
					errorResult.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (process != null) {
				process.destroy();
			}
		}

		return new CommandResult(result, sucMsg, errMsg);
	}

	public static class CommandResult {
		private int result;
		private String successMsg;
		private String errorMsg;

		public CommandResult(int result) {
			this.result = result;
			this.successMsg = "";
			this.errorMsg = "";
		}

		public CommandResult(int result, String successMsg, String errorMsg) {
			this.result = result;
			this.successMsg = successMsg;
			this.errorMsg = errorMsg;
		}

		public int getResult() {
			return result;
		}

		public String getSuccessMsg() {
			return successMsg;
		}

		public String getErrorMsg() {
			return errorMsg;
		}
	}
}
