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
				// ��ȡ��Ƶ������
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
				// ��ȡ��Ƶ��ʽ
				AudioFormat audioFormat = audioStream.getFormat();
				
				System.out.println("��Ƶ�ļ�: " + file.getPath());
				//System.out.println("��ƵEncoding: " + audioFormat.getEncoding());
				
				// �������wav��ʽ��ת��mp3�ļ����롣MPEG1L3��mp3��ʽ��תΪPCM_SIGNED��wav��ʽ��
				if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
					audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
							audioFormat.getSampleRate(), 16, audioFormat.getChannels(), audioFormat.getChannels() * 2,
							audioFormat.getSampleRate(), false);
	
					audioStream = AudioSystem.getAudioInputStream(audioFormat, audioStream);
				} // ת��mp3�ļ��������
				// ��װ��Ƶ��Ϣ
				DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
				// ��ȡ������������SourceDataLine��ʵ��
				SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
				
				sourceDataLine.open(audioFormat);
				sourceDataLine.start();	
				// ������Ƶ
				while((count = audioStream.read(buf, 0, buf.length)) != -1){
					sourceDataLine.write(buf, 0, count);			
				}
				// ���Ž������ͷ���Դ
				sourceDataLine.drain();
				sourceDataLine.close();
				audioStream.close();
				//System.out.println("���Ž���");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
