package DownLoad;

import java.io.File;
import java.util.ArrayList;

import Config.LOG;
import GetComic.Chapter;
import GetComic.GetPicture;
import GetComic.SaveImg;
import UI.FrameComic;

public class DLThread implements Runnable{
	private String html = null;
	private String title = null;
	private String path = null;
	private FrameComic fc = null;
	private int len = 0;
	private Chapter chapter = null;
	
	public DLThread(Chapter chapter,String path)
	{
		this.chapter = chapter;
		this.html = chapter.getHtml();
		this.title = chapter.getTitle();
		this.path = path;
		processTitle();
	}
	
	public void setFC(FrameComic fc)
	{
		this.fc = fc;
	}
	
	@Override
	public void run() {
		//System.out.println(Thread.currentThread().getName() + "��ʼ����" + title);
		File dir = new File(path + "/" + title);
		if(dir.exists() && dir.isDirectory())
		{
			dir.delete();
		}
		
		if(!dir.mkdir())
		{
			LOG.log("�����½��ļ���ʧ�ܣ���������Ƿ�����/��ַ����/�ļ����Ѵ���:" + dir.getAbsolutePath());
			return;
		}
		
		ArrayList<String> PicPath = new GetPicture(html).getPicturePath();
		
		if(PicPath.isEmpty())
		{
			LOG.log("����ͼƬ��ַʧ�ܣ���Ҫ��������:" + html);
			return;
		}
		
		len = PicPath.size();
		int index = 1;
		int result = 0;
		for(String path : PicPath)
		{
			//System.out.println(Thread.currentThread().getName() + "��ʼ���ص�" + index + "P");
			
			fc.UpdateDLinfo(chapter, index, len);
			result = (new SaveImg(path, dir.getAbsolutePath() + "/", index + ".jpg")).SavePicture();
			if(0 == result)
			{
				LOG.log("����ͼƬ��ַʧ�ܣ���Ҫ��������:" + path);
				return;
			}
			index ++;
		}
		
		fc.FinishDl(chapter);
	}
	//һЩ����ַ��ŵ����ƿ��ܻᵼ�´����ļ���ʧ�ܣ�������Ҫ����Ԥ����
	private void processTitle()
	{
		title = title.replaceAll("[\\^%&',.;=?$]+", "");
		System.out.println(title);
	}
}