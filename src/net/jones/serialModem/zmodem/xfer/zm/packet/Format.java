package net.jones.serialModem.zmodem.xfer.zm.packet;


import net.jones.serialModem.zmodem.xfer.util.CRC;
import net.jones.serialModem.zmodem.xfer.zm.util.ZModemCharacter;

public enum Format{
	
	BIN32(1, CRC.Type.CRC32, ZModemCharacter.ZBIN32),
	BIN  (1,CRC.Type.CRC16, ZModemCharacter.ZBIN),
	HEX  (2,CRC.Type.CRC16, ZModemCharacter.ZHEX);
	
	
	public static Format fromByte(byte b){
		for(Format ft : values()){
			if(ft.character()==b)
				return ft;
		}
		return null;
	}
	
	private int width;
	private CRC.Type crc;
	private ZModemCharacter character;
	
	private Format(int bw,CRC.Type crct,ZModemCharacter fmt){
		width  = bw;
		crc    = crct;
		character = fmt;
	}
	
	public byte character(){
		return character.value();
	}
	
	public CRC.Type crc(){
		return crc;
	}
	
	public boolean hex(){
		return (this == HEX);
	}
	
	public int width(){
		return width;
	}

}