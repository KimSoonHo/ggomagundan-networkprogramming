package com.qualcomm.QCARSamples.ImageTargets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

public class FileDownload {
	
	public void DownloadFromUrl(String DownloadUrl, String fileName) {

		   try {
		           File root = android.os.Environment.getExternalStorageDirectory();               

		           File dir = new File (root.getAbsolutePath() + "/ar_xml");
		           if(dir.exists()==false) {
		                dir.mkdirs();
		           }

		           URL url = new URL(DownloadUrl); //you can write here any link
		           File file = new File(dir, fileName);
		           if(file.exists()==true){
		        	   file.delete();
		        	   Log.d("PBS", fileName + " file is Exit");
		           }

		           long startTime = System.currentTimeMillis();
		           Log.d("PBS", "download begining");
		           Log.d("PBS", "download url:" + url);
		           Log.d("PBS", "downloaded file name:" + fileName);

		           /* Open a connection to that URL. */
		           URLConnection ucon = url.openConnection();

		           /*
		            * Define InputStreams to read from the URLConnection.
		            */
		           InputStream is = ucon.getInputStream();
		           BufferedInputStream bis = new BufferedInputStream(is);

		           /*
		            * Read bytes to the Buffer until there is nothing more to read(-1).
		            */
		           ByteArrayBuffer baf = new ByteArrayBuffer(5000);
		           int current = 0;
		           while ((current = bis.read()) != -1) {
		              baf.append((byte) current);
		           }


		           /* Convert the Bytes read to a String. */
		           FileOutputStream fos = new FileOutputStream(file);
		           fos.write(baf.toByteArray());
		           fos.flush();
		           fos.close();
		           Log.d("PBS", "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");

		   } catch (IOException e) {
		       Log.d("PBS", "Error: " + e);
		   }

		}

	

}