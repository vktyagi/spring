package com.example.bean;

import org.springframework.stereotype.Service;

import com.example.route.SubscriberFileRoute;

@Service("fileProcess")
public class FileProcessImpl implements FileProcess {
	@Override
	public String renameFile(String fileName) {
		return fileName + "." + SubscriberFileRoute.currentLoadUUID;
	}
}