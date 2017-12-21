package com.spdb.ib.dpib.tftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.spdb.ib.dpib.logs.LogManager;

/**
 * FTP操作类
 * 
 * @author T-wuwp
 * @version 1.0
 */
public class FTPTransfer {
	private static FTPClient ftpClient = null;
	private static String ip;
	private static int port;
	private static String userName;
	private static String password;

	public FTPTransfer() {
		ip = DsfUtils.getPath("IP");
		port = Integer.parseInt(DsfUtils.getPath("PORT"));
		userName = DsfUtils.getPath("USERNAME");
		password = DsfUtils.getPath("PASSOWRD");
	}

	public FTPTransfer(String ip, int port, String username, String password) {
		this.ip = ip;
		this.port = port;
		this.userName = username;
		this.password = password;
	}

	/**
	 * 连接到FTP服务器
	 * 
	 * @return true 连接服务器成功，false 连接服务器失败
	 */
	public boolean connect() {
		boolean isSuccess = false;
		if (ftpClient == null) {
			ftpClient = new FTPClient();
		}

		if (ftpClient.isConnected()) {
			return isSuccess = true;
		}

		try {
			ftpClient.setControlEncoding("GBK");// 设置通讯的字符集
			ftpClient.setDataTimeout(60 * 60 * 1000);// 超时断开判断,单位毫秒
			// 连接到FTP
			if (this.port == 0) {
				ftpClient.connect(this.ip);
			} else {
				ftpClient.connect(this.ip, this.port);
			}

			// 登录FTP服务器
			if (ftpClient.login(userName, password)) {

				// 发出链接请求后，应马上获取replyCode，检验链接是否成功
				int replyCode = ftpClient.getReplyCode();
				if (!FTPReply.isPositiveCompletion(replyCode)) {
					ftpClient.disconnect();
					LogManager.info("FTP链接请求失败！");
					return isSuccess = false;
				}
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);// 设置是进行二进制传输

			} else {
				LogManager.info("FTP 登陆失败，用户名或密码错误！用户名：" + userName + "密码："
						+ password);
				return isSuccess = false;
			}

			isSuccess = true;
			if (isSuccess) {
				LogManager.info("FTP连接链接请求成功======");
			}
		} catch (ConnectException e) {
			e.printStackTrace();
			LogManager.error("连接登录FTP服务器IO异常！\n" + e.getMessage() + ",IP:" + ip
					+ ",端口:" + port + ",用户名:" + userName + ",密码:" + password);
			isSuccess = false;
		} catch (IOException e) {
			e.printStackTrace();
			LogManager.error("连接登录FTP服务器IO异常！\n" + e.getMessage() + ",IP:" + ip
					+ ",端口:" + port + ",用户名:" + userName + ",密码:" + password);
			isSuccess = false;
		}
		return isSuccess;
	}

	/**
	 * 关闭FTP or Tftp连接，释放资源
	 */
	public void closeConnect() {
		try {
			if (ftpClient != null) {
				ftpClient.logout();
				ftpClient.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogManager.error("关闭FTP连接出现异常======" + e.getMessage());
		}
	}

	/**
	 * 从FTP服务器上下载文件，下载单个文件
	 * 
	 * @param remotePath
	 *            FTP上的路径
	 * @param remoteFile
	 *            要FTP下载的文件名
	 * @param localPath
	 *            本地保存路径
	 * @param localFile
	 *            本地保存文件名
	 * @return 下载成功返回true, 下载失败返回false
	 */
	public boolean downLoadOneFile(String remotePath, String remoteFile,
			String localPath, String localFile) {

		boolean isSuccess = false;
		if (ftpClient == null || !ftpClient.isConnected()) {
			if (!this.connect()) {
				LogManager.info("连接FTP服务器失败！");
				return isSuccess;
			}
		}
		BufferedOutputStream bos = null;
		// 下载过程中的本地文件的临时文件名
		File tmpFileF = new File(localPath
				+ ((localFile == null || localFile.equals("")) ? remoteFile
						: localFile) + ".tmp");
		// 本地文件的File类型
		File localFileF = new File(localPath
				+ ((localFile == null || localFile.equals("")) ? remoteFile
						: localFile));
		try {
			// 将FTP的当前目录改到指定目录
			if (!ftpClient.changeWorkingDirectory(remotePath)) {
				LogManager.info("在FTP服务器下找不到该路径:" + remotePath);
				return isSuccess;
			}

			if (!new File(localPath).exists()) {
				new File(localPath).mkdirs();
			}

			bos = new BufferedOutputStream(new FileOutputStream(tmpFileF));
			isSuccess = ftpClient.retrieveFile(remoteFile, bos);// 开始下载
			LogManager.info("下载指定文件夹下文件成功=======" + remotePath + remoteFile);
		} catch (IOException e) {
			e.printStackTrace();
			LogManager.error("载指定文件夹下文件IO异常！\n" + e.getMessage());
			isSuccess = false;
		} finally {
			try {
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				e.printStackTrace();
				LogManager.error("关闭文件输出流异常！" + e.getMessage());
			}
		}
		if (isSuccess) {
			// 待文件下载完成，再把文件名改回来
			if (localFileF.exists()) {
				localFileF.delete();
			}
			if (!tmpFileF.renameTo(localFileF)) {
				isSuccess = false;
				LogManager.debug("下载成功，但改文件名时出错：" + tmpFileF.getAbsolutePath()
						+ "改为" + localFileF.getAbsolutePath());
			}
		}
		return isSuccess;
	}

	/**
	 * 在FTP服务器上指定目录下模糊查找文件，下载文件名相匹配的文件（采用正则表达式）。
	 * 
	 * @param remotePath
	 *            FTP上的路径
	 * @param remoteFile
	 *            要FTP下载的文件名
	 * @param localPath
	 *            本地保存路径
	 * @param localFile
	 *            本地保存文件名,如果为空就用remoteFile替代
	 * @return 返回下载的文件列表
	 */
	public List<String> downLoadFile(String remotePath, String remoteFile,
			String localPath, String localFile) {
		List<String> resultFileNameList = new ArrayList<String>();// 返回结果列表
		// 判断是否还处于连接状态，如果不是连接状态，则连接登录FTP服务器
		if (ftpClient == null || !ftpClient.isConnected()) {
			if (!connect()) {
				LogManager.info("连接FTP服务器失败！");
				return null;
			}
		}
		try {
			FTPFile[] ftpFileList = null;// 存放FTP上指定目录下的文件列表
			// 获取FTP指定目录下的文件列表
			if (ftpClient.changeWorkingDirectory(remotePath)) {
				Pattern pattern = Pattern.compile(remoteFile);
				ftpClient.enterLocalPassiveMode();
				ftpFileList = ftpClient.listFiles();
				if (ftpFileList.length <= 0) {
					// 用于处理某些服务器（SunOS Release 4.1.4）Server OS or FTP
					// 版本过旧导至listFiles报异常的问题
					FTPClientConfig conf = new FTPClientConfig(
							FTPClientConfig.SYST_UNIX);
					ftpClient.configure(conf);
					ftpFileList = ftpClient.listFiles();
				}
				resultFileNameList = new ArrayList<String>();
				for (FTPFile ftpFile : ftpFileList) {
					if (ftpFile == null || ftpFile.getName() == null) {
						continue;
					}
					// 匹配文件名
					String ftpFileName = ftpFile.getName();
					if (pattern.matcher(ftpFileName).matches()) {
						// 如果下载文件成功，则添加到返回列表中
						String downLocalFile = localFile;
						if (downLocalFile == null || downLocalFile.equals("")) {
							downLocalFile = ftpFileName;
						}
						if (downLoadOneFile(remotePath, ftpFileName, localPath,
								downLocalFile)) {
							resultFileNameList.add(localPath + downLocalFile);
						}
					}
				}
				LogManager
						.info("下载指定文件夹下文件成功=======" + remotePath + remoteFile);
			} else {
				LogManager.info("changeWorkingDirectory " + remotePath
						+ " remotePath faile");
			}
		} catch (IOException e) {
			LogManager.error(e.getMessage());
			return null;
		}
		if (resultFileNameList.size() <= 0) {
			return null;
		}
		return resultFileNameList;
	}

	/**
	 * 在FTP服务器上指定目录下模糊查找文件，下载文件名相匹配的文件（采用正则表达式）。
	 * 
	 * @param remotePath
	 *            FTP上的路径
	 * @param remoteFile
	 *            要FTP下载的文件名
	 * @param localPath
	 *            本地保存路径
	 * @return 返回下载的文件列表
	 */
	public List<String> downLoadFile(String remotePath, String remoteFile,
			String localPath) {
		return this.downLoadFile(remotePath, remoteFile, localPath, null);
	}

	/**
	 * 在FTP服务器上指定目录下下载所有文件
	 * 
	 * @param remotePath
	 *            FTP or Tftp上的路径
	 * @param localPath
	 *            本地保存路径
	 * @return
	 */
	public List<String> downLoadFile2(String remotePath, String localPath) {
		List<String> resultFileNameList = new ArrayList<String>();// 返回结果列表
		// 判断是否还处于连接状态，如果不是连接状态，则连接登录FTP服务器
		if (ftpClient == null || !ftpClient.isConnected()) {
			if (!connect()) {
				LogManager.info("连接FTP or Tftp服务器失败！");
				return null;
			}
		}

		try {
			FTPFile[] ftpFileList = null;// 存放FTP上指定目录下的文件列表
			// 获取FTP指定目录下的文件列表
			if (ftpClient.changeWorkingDirectory(remotePath)) {
				ftpFileList = ftpClient.listFiles();
				if (ftpFileList.length <= 0) {
					// 用于处理某些服务器（SunOS Release 4.1.4）Server OS or FTP
					// 版本过旧导至listFiles报异常的问题
					FTPClientConfig conf = new FTPClientConfig(
							FTPClientConfig.SYST_UNIX);
					ftpClient.configure(conf);
					ftpFileList = ftpClient.listFiles();
				}
				resultFileNameList = new ArrayList<String>();
				LogManager.error("路径:" + remotePath
						+ "=================================================");
				LogManager.error("集合大小:" + ftpFileList.length);
				for (FTPFile ftpFile : ftpFileList) {
					if (ftpFile != null) {
						LogManager.error("文件名:" + remotePath
								+ ftpFile.getName());
					} else {
						LogManager.error("文件名:" + remotePath + "null");
					}
				}
				LogManager
						.error("=====================================================================");
				for (FTPFile ftpFile : ftpFileList) {
					if (ftpFile == null || ftpFile.getName() == null) {
						continue;
					}
					// 匹配文件名
					String ftpFileName = ftpFile.getName();
					// 如果下载文件成功，则添加到返回列表中
					String downLocalFile = ftpFileName;
					if (downLocalFile == null || downLocalFile.equals("")) {
						downLocalFile = ftpFileName;
					}
					if (downLoadOneFile(remotePath, ftpFileName, localPath,
							downLocalFile)) {
						resultFileNameList.add(localPath + downLocalFile);
					}
				}
				LogManager.info("下载文件成功=======  服务器路径:" + remotePath);
			} else {
				LogManager.info("changeWorkingDirectory " + remotePath
						+ " remotePath faile");
			}

		} catch (IOException e) {
			// log.error(PublicPrompt.Error_FtpFileLists + e.getMessage());
			return null;
		}
		if (resultFileNameList.size() <= 0) {
			return null;
		}
		return resultFileNameList;
	}

	/**
	 * FTP or Tftp上传单个文件
	 * 
	 * @param localFileName
	 *            本地文件
	 * @param remotePath
	 *            远程服务文件路径
	 * @param remoteFileName
	 *            远程上传保存文件名
	 * @return
	 */
	public int UploadOneFile(String localFileName, String remotePath,
			String remoteFileName) {
		// 判断是否还处于连接状态，如果不是连接状态，则连接登录FTP服务器
		if (ftpClient == null || !ftpClient.isConnected()) {
			if (!connect()) {
				LogManager.info("连接FTP or Tftp服务器失败！");
				LogManager.info("文件上传失败!文件路径：" + localFileName);
				return 0;
			}
		}
		FileInputStream fis = null;
		try {
			File srcFile = new File(localFileName);
			if (!srcFile.exists()) {
				LogManager.info(localFileName + "文件上传失败， 本地文件不存在=======");
				return 0;
			}
			fis = new FileInputStream(srcFile);
			String srcFileName = srcFile.getName();
			if (null == remotePath.trim() || remotePath.trim().equals("")) {
				remotePath = DsfUtils.getPath("DEFAULTREMOTEFILEPATH");
			}
			// 设置上传目录
			boolean f = ftpClient.changeWorkingDirectory(remotePath);
			if (!f) {
				ftpClient.makeDirectory(remotePath);
				f = ftpClient.changeWorkingDirectory(remotePath);
			}
			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding("GBK");
			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			if (null == remoteFileName.trim()) {
				ftpClient.storeFile(srcFileName, fis);
			} else {
				ftpClient.storeFile(remoteFileName, fis);
			}
			if (f) {
				LogManager.info("文件上传成功!文件路径：" + localFileName + " 服务器路径:"
						+ remotePath);
				return 1;
			} else {
				LogManager.info("文件上传失败!文件路径：" + localFileName + " 服务器路径:"
						+ remotePath);
				return 0;
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP or Tftp客户端出错！", e);
		} finally {
			try {
				if (null != fis) {
					fis.close();
				}
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("关闭FTP or Tftp连接发生异常！", e);
			}
		}
	}

	/**
	 * FTP or Tftp上传多个文件
	 * 
	 * @param localFileName
	 *            本地文件
	 * @param remotePath
	 *            远程服务文件路径
	 * @param remoteFileName
	 *            远程上传保存文件名
	 * @return
	 */
	public void UploadSomeFile(String[] localFileName, String remotePath,
			String[] remoteFileName) {
		try {
			if (null == localFileName || localFileName.length <= 0) {
				LogManager.info("需要上传的文件不能为空，请必须传入至少一个文件=========");
			}
			for (int i = 0; i < localFileName.length; i++) {
				UploadOneFile(localFileName[i], remotePath, remoteFileName[i]);
			}
			LogManager.info("上传文件发生成功==========");
		} catch (Exception e) {
			LogManager.error("上传文件发生异常==========");
		}
	}

	public boolean whetherReadFile(String filePath) {
		try {
			boolean f = false;
			// 可使用内存
			long totalMemory = Runtime.getRuntime().totalMemory();
			File file = new File(filePath);
			if (file.exists() && file.isFile()) {
				long fileLength = file.length();
				f = fileLength < totalMemory ? true : false;
				if (f) {
					LogManager.info("文件大小适合，可以解析文件======");
				} else {
					LogManager.info("文件太大，不需要解析文件======");
				}
				return f;
			} else {
				LogManager.info("文件不存在或文件类型不是文件,不需要解析文件======");
				return false;
			}
		} catch (Exception e) {
			LogManager.error("文件获取大小发生异常=======");
			return false;
		}
	}

	public boolean removeAll(String pathname) {
		if (ftpClient == null || !ftpClient.isConnected()) {
			if (!connect()) {
				LogManager.info("连接FTP or Tftp服务器失败！");
				LogManager.info("文件删除失败!文件名：" + pathname);
				return false;
			}
		}
		try {
			// FTPFile file=ftpClient.list(pathname)
			System.out.println(ftpClient.list(pathname));
			// boolean f = ftpClient.deleteFile(pathname);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("关闭FTP or Tftp连接发生异常！", e);
			}
		}
	}

	public static void main(String[] args) {
		FTPTransfer f = new FTPTransfer();
		f.downLoadFile("/dsfapp/tftp/data/", "b.txt",
				"C:/Users/T-wuwp/Desktop/");
		// TFtpOperationUtil.UploadSomeFile(new
		// String[]{"D://ftp//hello.txt","D://ftp//hello1.txt"}, "", new
		// String[]{"",""});
		// f.downLoadOneFile("/dsfapp/tftp/data/", "hello.txt",
		// "C:/Users/T-wuwp/Desktop/", "hello.txt");

		// File f = new File(
		// "C:/Users/T-wuwp/Desktop/《电子商务托管系统分行前置系统开发项目》概要设计说明书.doc");
		// System.out.println(f.length());
		// System.out
		// .println(whetherReadFile("C:/Users/T-wuwp/Desktop/《电子商务托管系统分行前置系统开发项目》概要设计说明书.doc"));
		// System.out.println(removeAll("/dsfapp/tftp/data/a.txt"));
	}

}
