package com.hqk27;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class MediaPlayer {
	static void playMusic(String path) {
		File file = new File(path);
		if (file.isFile()) {
			new PlayerThread(file).start();
		}
	}
	
	static class PlayerThread extends Thread {
		File file;
		public PlayerThread(File file) {
			this.file = file;
		}
		@Override
		public void run() {
			try {
				int count;
				byte buf[] = new byte[2048];
				// 获取音频输入流
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
				// 获取音频格式
				AudioFormat audioFormat = audioStream.getFormat();
				
				System.out.println("音频文件: " + file.getPath());
				//System.out.println("音频Encoding: " + audioFormat.getEncoding());
				
				// 如果不是wav格式，转换mp3文件编码。MPEG1L3（mp3格式）转为PCM_SIGNED（wav格式）
				if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
					audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
							audioFormat.getSampleRate(), 16, audioFormat.getChannels(), audioFormat.getChannels() * 2,
							audioFormat.getSampleRate(), false);
	
					audioStream = AudioSystem.getAudioInputStream(audioFormat, audioStream);
				} // 转换mp3文件编码结束
				// 封装音频信息
				DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
				// 获取虚拟扬声器（SourceDataLine）实例
				SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
				
				sourceDataLine.open(audioFormat);
				sourceDataLine.start();	
				// 播放音频
				while((count = audioStream.read(buf, 0, buf.length)) != -1){
					sourceDataLine.write(buf, 0, count);			
				}
				// 播放结束，释放资源
				sourceDataLine.drain();
				sourceDataLine.close();
				audioStream.close();
				//System.out.println("播放结束");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
