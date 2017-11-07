package main.java.model;

public class Head {

	private String fileName;
	private short version;
	private String tagName;
	private int tagNum;
	/*指向文件目录种类*/
	private Short elementType;
	/*指向文件目录大小*/
	private int elementSize;
	/*指向文件目录数量*/
	private int elementNum;
	/*目录大小*/
	private int dataSize;
	/*目录开始位置偏移量*/
	private int dataOffset;

	public int getElementNum() {
		return elementNum;
	}

	public void setElementNum(int elementNum) {
		this.elementNum = elementNum;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public short getVersion() {
		return version;
	}

	public void setVersion(short version) {
		this.version = version;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public int getTagNum() {
		return tagNum;
	}

	public void setTagNum(int tagNum) {
		this.tagNum = tagNum;
	}

	public Short getElementType() {
		return elementType;
	}

	public void setElementType(Short elementType) {
		this.elementType = elementType;
	}

	public int getElementSize() {
		return elementSize;
	}

	public void setElementSize(int elementSize) {
		this.elementSize = elementSize;
	}

	public int getDataSize() {
		return dataSize;
	}

	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

	public int getDataOffset() {
		return dataOffset;
	}

	public void setDataOffset(int dataOffset) {
		this.dataOffset = dataOffset;
	}
}


/*
 * file_head结构
 <head>
 <!-- byte 0-3 -->
 <filemame>ABIF</filemame>
 <!-- byte 4-5 -->
 <version>101</version>
 <!-- byte 6-30 -->
 <dirEntry>
 <!-- byte 6-9 -->
 <tagName>tdir</tagName>
 <!-- byte 10-13 -->
 <tagNumber>1</tagNumber>
 <!-- byte 14-15 -->
 <elementType>1023</elementType>
 <!-- byte 16-17 -->
 <elementSize>28</elementSize>
 <!-- byte 18-21 -->
 <elementNums>127</elementNums>
 <!-- byte 22-25 -->
 <dataSize>4032</dataSize>
 <!-- byte 26-29 -->
 <dataOffset>216198</dataOffset>
 <!-- byte 30 reserved -->
 <dataHandle>0</dataHandle>
 </dirEntry>
 <!-- byte 31-124 -->
 <unuse>0</unuse>
 </head>
 */