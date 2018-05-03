package com.sunmi.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;


public class DownloadAndUploadByJava {
	
//	public static final String ip = "10.142.77.86";
	public static final String ip = "172.16.0.7";
	public static final int port = 21;
//	port of FTP to transfer text
	public static final String userName = "cibuild";
	public static final String passWord = "cibuild987";
	public static String windowslocalFilePath = "D:\\test\\";
	public static String linuxlocalFilePath = "D:\\test\\";
	public static String remotePath;
	public static String keyword;

	public static void main(String[] args) throws SocketException, IOException {
//		args = new String[] {"/Image/T2/Sunmi/Release/1.0.13/37/symbol_files","System"};
		try {
			remotePath = args[0];
			keyword = args[1];
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(remotePath == null) {
			System.out.println("remote path can not be null");
			System.exit(-1);
		}
		if(keyword == null) {
			System.out.println("remote file name or it's keyword can not be null");
			System.exit(-1);
		}
		System.out.println("args:"+remotePath+"::::"+keyword);
		//download files by FTP
		//create FTP object
		FTPClient ftpClient = new FTPClient();
		// create connect
		ftpClient.connect(ip, port);
		ftpClient.login(userName, passWord);
		// set file type
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		// set linux environment
		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
		ftpClient.configure(conf);
		// change to work directory
		boolean workdir = ftpClient.changeWorkingDirectory(remotePath);
		if (!workdir) {
			System.out.println("Ftp path is not exist!");
			System.exit(-1);
		}
		// list all files
		FTPFile[] ftpFiles = ftpClient.listFiles();
		System.out.println("ftpFiles:"+ftpFiles);
		for (int i = 0; i < ftpFiles.length; i++) {
			System.out.println(ftpFiles[i].getName());
			if (ftpFiles[i].getName().contains(keyword)) {
				File localFile;
				if (System.getProperty("os.name").startsWith("Win")) {
					windowslocalFilePath += ftpFiles[i].getName();
					System.out.println("windowslocalFilePath:"+windowslocalFilePath);
					localFile = new File(windowslocalFilePath);
				} else {
					//linux environment
					linuxlocalFilePath += ftpFiles[i].getName();
					System.out.println("linuxlocalFilePath:"+linuxlocalFilePath);
					localFile = new File(linuxlocalFilePath);
				}
				System.out.println("Downloading...");
				OutputStream os = new FileOutputStream(localFile);
				ftpClient.retrieveFile(ftpFiles[i].getName(), os);
				System.out.println("Downloaded");
				os.close();
				break;
			}
			if (i == ftpFiles.length - 1) {
				System.out.println("target file is not found");
				System.exit(-1);
			}
		}
	}
}
