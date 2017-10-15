package main.model;

public class Directory {
	
	private String tagName;
	private int tagNum;
	private short elementType;
	private short elementSize;
	private int elementNum;
	private int itemSize;
	private int itemOffset;
	private String rs;
	public String getRs() {
		return rs;
	}
	public void setRs(String rs) {
		this.rs = rs;
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
	public short getElementType() {
		return elementType;
	}
	public void setElementType(short elementType) {
		this.elementType = elementType;
	}
	public short getElementSize() {
		return elementSize;
	}
	public void setElementSize(short elementSize) {
		this.elementSize = elementSize;
	}
	public int getElementNum() {
		return elementNum;
	}
	public void setElementNum(int elementNum) {
		this.elementNum = elementNum;
	}
	public int getItemSize() {
		return itemSize;
	}
	public void setItemSize(int itemSize) {
		this.itemSize = itemSize;
	}
	public int getItemOffset() {
		return itemOffset;
	}
	public void setItemOffset(int itemOffset) {
		this.itemOffset = itemOffset;
	}

}
