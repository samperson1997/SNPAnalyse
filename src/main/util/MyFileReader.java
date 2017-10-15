package main.util;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MyFileReader extends RandomAccessFile {

	public MyFileReader(String name, String mode) throws FileNotFoundException {
		super(name, mode);
		// TODO Auto-generated constructor stub
	}

	public String readDate() throws IOException {
		int year = this.readUnsignedShort();
		int month = this.readUnsignedByte();
		int day = this.readUnsignedByte();
		return year + "-" + month + "-" + day;
	}

	public String readTime() throws IOException {
		int hour = this.readUnsignedByte();
		int min = this.readUnsignedByte();
		int sec = this.readUnsignedByte();
		int hsec = this.readUnsignedByte();
		return hour + ":" + min + ":" + sec + ":" + hsec;
	}

	// 读取n个字节的字符串
	public String readString(Integer num) throws IOException {
		byte[] temp = new byte[num];
		this.read(temp);
		return new String(temp);
	}

	// 读取n个字节的C字符串
	public String readCString(Integer num) throws IOException {
		byte[] temp = new byte[num - 1];
		this.read(temp);
		this.skipBytes(1);
		return new String(temp);
	}

}
