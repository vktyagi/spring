package com.example.bean;

import org.springframework.stereotype.Service;

import com.example.route.FileProcesserRoute;

@Service("fileProcess")
public class FileProcessImpl implements FileProcess {
	@Override
	public String renameFile(String fileName) {
		return fileName + "." + FileProcesserRoute.currentLoadUUID;
	}

	@Override
	public String doneRenameFile(String fileName) {
	    return fileName.split(".dat")[0]+".done";
	}
	
}