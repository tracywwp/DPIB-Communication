package com.spdb.ib.dpib.tftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.spdb.ib.dpib.logs.LogManager;

/**
 * 命令调用
 * 
 * @author 
 * 
 */
public class Commander {

	/**
	 * 执行命令
	 * 
	 * @param cmd
	 * @return
	 */
	public static String[] exec(String cmd) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			LogManager.error("----------------调用命令:" + cmd + "异常------------------");
			return null;
		}
		return process(process);
	}
	/**
	 * 执行命令
	 * @param cmdArray
	 * @return
	 */
	public static String[] exec(String[] cmdArray) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmdArray);
		} catch (IOException e) {
			StringBuffer cmds = new StringBuffer();
			handleArgs(cmdArray,cmds);
			LogManager.error("----------------调用命令:" + cmds.toString() + " 异常------------------");
			return null;
		}
		return process(process);
	}
	/**
	 * 执行命令
	 * @param cmdArray
	 * @param envp
	 * @return
	 */
	public static String[] exec(String[] cmdArray, String[] envp) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmdArray, envp);
		} catch (IOException e) {
			StringBuffer cmds = new StringBuffer();
			handleArgs(cmdArray,cmds);
			handleArgs(envp,cmds);
			LogManager.error("----------------调用命令:" + cmds.toString() + " 异常------------------");
			return null;
		}
		return process(process);
	}
	/**
	 * 执行命令
	 * @param cmdArray
	 * @param envp
	 * @param dir
	 * @return
	 */
	public static String[] exec(String[] cmdArray, String envp[], File dir) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmdArray, envp, dir);
		} catch (IOException e) {
			StringBuffer cmds = new StringBuffer();
			handleArgs(cmdArray,cmds);
			handleArgs(envp,cmds);
			cmds.append(dir.getName());
			LogManager.error("----------------调用命令:" + cmds.toString() + " 异常------------------");
			return null;
		}
		return process(process);
	}
	/**
	 * 执行命令
	 * @param cmd
	 * @param envp
	 * @return
	 */
	public static String[] exec(String cmd, String envp[]) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmd, envp);
		} catch (IOException e) {
			StringBuffer cmds = new StringBuffer();
			cmds.append(cmd).append(";");
			handleArgs(envp,cmds);
			LogManager.error("----------------调用命令:" + cmds.toString() + " 异常------------------");
			return null;
		}
		return process(process);
	}
	/**
	 * 执行命令
	 * @param cmd
	 * @param envp
	 * @param dir
	 * @return
	 */
	public static String[] exec(String cmd, String envp[], File dir) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmd, envp, dir);
		} catch (IOException e) {
			StringBuffer cmds = new StringBuffer();
			cmds.append(cmd).append(";");
			handleArgs(envp,cmds);
			cmds.append(dir.getName());
			LogManager.error("----------------调用命令:" + cmds.toString() + " 异常------------------");
			return null;
		}
		return process(process);
	}
	/**
	 * process handle
	 * @param process
	 * @return
	 */
	private static String[] process(Process process) {
		List list = new ArrayList();
		String line = null;
		InputStream inputStream = null;
		InputStream errorInputStream = null;
		BufferedReader inputStreamBufferedReader = null;
		BufferedReader errorInputStreamBufferedReader = null;
		try {
			inputStream = process.getInputStream();
			inputStreamBufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			while (null != (line = inputStreamBufferedReader.readLine())) {
				list.add(line);
				LogManager.info(line);
			}
			errorInputStream = process.getErrorStream();
			errorInputStreamBufferedReader = new BufferedReader(
					new InputStreamReader(errorInputStream));
			while (null != (line = errorInputStreamBufferedReader.readLine())) {
				// error log
				list.add(line);
				LogManager.warn("----------------调用命令子进程errorStream信息：" + line + "------------");
			}
			int status = process.waitFor();
			if (0 != status) {
				// not exit normally
				LogManager.warn("----------------调用命令子进程未正常退出------------");
				return null;
			}

		} catch (IOException e) {
			LogManager.error("----------------调用命令子进程IOException异常------------");
			return null;
		} catch (InterruptedException e) {
			LogManager.error("----------------调用命令子进程InterruptedException异常------------");
			return null;
		} finally {
			try {
				if (null != inputStreamBufferedReader) {
					inputStreamBufferedReader.close();
				}
				if (null != errorInputStreamBufferedReader) {
					errorInputStreamBufferedReader.close();
				}
			} catch (IOException e) {
				LogManager.error("----------------调用命令关闭输入流异常------------");
			}
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * 参数字符串拼接
	 * @param args
	 * @param argStr
	 */
	private static void handleArgs(String[] args,StringBuffer argStr){
		for(int i=0;i<args.length;i++){
			argStr.append(args[i]).append(";");
		}
	}
	
}
