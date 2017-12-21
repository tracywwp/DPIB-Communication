package com.spdb.ib.dpib.tftp;

import java.io.File;

import com.spdb.ib.dpib.logs.LogManager;

/**
 * TFTP操作
 * @author 
 *
 */
public class TFTPUtil {
	/**
	 * copy cmd
	 */
	private final static String CP = "cp ";
	/**
	 * tftclient cmd
	 */
	private final static String TFT_CLIENT = "tftclient -d";
	/**
	 * tftclient up param
	 */
	private final static String UP = "up";
	/**
	 * tftclient down param
	 */
	private final static String DOWN = "down";
	/**
	 * tftclient -h param
	 */
	private final static String H = " -h";
	/**
	 * tftclient -h default hostNo
	 */
	private final static String DEFAULT_HOST_NO = "1";
	/**
	 * tftclient -r param
	 */
	private final static String R = " -r";
	/**
	 * space
	 */
	private final static String SPACE = " ";
	/**
	 * tftp上传文件至核心系统[可由tparam指定路由参数]
	 * @param tftpHostNo tfpt服务器标识[如不指明，使用"1"]
	 * @param fileName 文件名称
	 * @param tftpPath tftp路径
	 * @param localPath 本地路径
	 * @param remotePath 远端路径
	 * @param tparam "-t" 路由参数[可选]，如：-tWHSJ指向的是结构性存款报送系统
	 * @return
	 */
	public static boolean uploadFileToSomeSys(String tftpHostNo,String fileName,String tftpPath,String localPath,String remotePath,String tparam){
		makeFileDirs(tftpPath);
		makeFileDirs(localPath);
		
		//copy file to tftpPath
		String copyCmd = CP + localPath + fileName + SPACE + tftpPath + fileName;
		String[] copyRes = Commander.exec(copyCmd);
		if(null == copyRes){
			//cp failed
			LogManager.error("----------------copy file failed, cmd:" + copyCmd + "----------------------");
			return false;
		}
		String uploadCmd = null;
		if(null != tftpHostNo && !"".equals(tftpHostNo.trim())){
			uploadCmd = TFT_CLIENT + UP + H + tftpHostNo + R + remotePath + fileName + SPACE + fileName;
		}else{
			uploadCmd = TFT_CLIENT + UP + H + DEFAULT_HOST_NO + R + remotePath + fileName + SPACE + fileName;
		}
		
		if(null != tparam && !"".equals(tparam.trim())){
			uploadCmd += SPACE + tparam;
		}
		String[] uploadRes = runCommand(uploadCmd);
		if(null == uploadRes){
			//tftp upload failed
			LogManager.error("----------------tftclient cmd execution failed, cmd:" + uploadCmd + "----------------------");
			return false;
		}
		return true;
	}

	/**
	 * tftp从核心下载到本地
	 * @param fileName 文件名称
	 * @param tftpPath tftp路径
	 * @param localPath 本地路径
	 * @param remotePath 远端路径
	 * @return
	 */
	public static boolean downloadFileFromCore(String tftpHostNo,String fileName,String tftpPath,String localPath,String remotePath){
		makeFileDirs(tftpPath);
		makeFileDirs(localPath);
		
		String downloadCmd = null;
		if(null != tftpHostNo && !"".equals(tftpHostNo.trim())){
			downloadCmd = TFT_CLIENT + DOWN + H + tftpHostNo + R + remotePath + fileName + SPACE + fileName;
		}else{
			downloadCmd = TFT_CLIENT + DOWN + H + DEFAULT_HOST_NO + R + remotePath + fileName + SPACE + fileName;
		}
		System.out.println(downloadCmd);
		String[] downloadRes = runCommand(downloadCmd);
		if(null == downloadRes){
			//tftp download failed
			LogManager.error("----------------tftclient download cmd execution failed, cmd:" + downloadCmd + "----------------------");
			return false;
		}
		String copyCmd = CP  + tftpPath + fileName + SPACE + localPath + fileName;
		String[] copyRes = Commander.exec(copyCmd);
		if(null == copyRes){
			//cp failed
			LogManager.error("----------------copy file failed, cmd:" + copyCmd + "----------------------");
			return false;
		}
		return true;
	}
	/**
	 * 执行命令.[如果命令执行失败，再尝试运行三次，每次间隔2秒]
	 * @param cmd
	 * @return
	 */
	public static String[] runCommand(String cmd){
		String[] result = Commander.exec(cmd);
		int count = 0;
		while(null == result && count < 3){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			result = Commander.exec(cmd);
			count++;
		}
		return result;
	}
	
	/**
	 * 创建路径
	 * @param path
	 */
	private static void makeFileDirs(String path){
		File dir = new File(path);
		if(null != dir && !dir.exists()){
			dir.mkdirs();
		}
	}
	
	public static void main(String[] args) {
		downloadFileFromCore("5","b.txt","/dsfapp/tftp/data/","C:/Users/T-wuwp/Desktop/","");
//		uploadFileToSomeSys("7","wwl.txt","","","",null);
//		uploadFileToSomeSys("7","wwl.txt","","","","-tWHSJ");
		//String[] downloadRes = runCommand("tftp -i 10.112.12.92");
		//System.out.println(downloadRes);
	}
}
