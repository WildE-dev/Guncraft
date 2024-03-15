package com.action35.guncraft.custom_data_types;

import java.util.UUID;

import org.bukkit.persistence.PersistentDataType;

public interface CustomDataTypes {
	public static final PersistentDataType<byte[],UUID> UUID = new UUIDDataType();
	public static final PersistentDataType<byte[],GunData> GUNDATA = new GunDataType();
}
