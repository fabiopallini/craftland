package com.craftland.engine.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class Storage
{
	private String fileName;
	private File file;
	private FileOutputStream fos;
	private FileInputStream fin;
	private BufferedReader bufferedReader;
	private String data;
	private byte[] bytes;
	
	public Storage(String fileName)
	{
		this.fileName = fileName;
		file = new File(Render.context.getFilesDir(), this.fileName);
	}

	public void write(byte[] data, boolean append) {
		try {
			fos = new FileOutputStream(file, append);
			fos.write(data);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] read() {
		int size = (int) file.length();
		bytes = new byte[size];
		try {
			fin = new FileInputStream(file);
			fin.read(bytes, 0, bytes.length);
			fin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	// string data
	public void write(String data, boolean append) {
		try {
			fos = new FileOutputStream(file, append);
			fos.write(data.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String read_str() {
		data = new String();
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				data += line;
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	public void delete()
	{
		file.delete();
	}
}
