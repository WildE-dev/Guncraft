package com.action35.guncraft.custom_data_types;

public class GunData {

	public byte reloadTime;
	public short shotTime;
	public short magazineSize;
	public short ammo;
	public short damage;
	public long timestamp;
	public short distance;
	
	public GunData() {
		this.reloadTime = 0;
		this.shotTime = 0;
		this.magazineSize = 0;
		this.ammo = 0;
		this.damage = 0;
		this.timestamp = 0;
		this.distance = 0;
	}
	
	public GunData(int reloadTime, int shotTime, int magazineSize, int damage, int distance) {
		this.reloadTime = (byte) reloadTime;
		this.shotTime = (short) shotTime;
		this.magazineSize = (short) magazineSize;
		this.ammo = (short) magazineSize;
		this.damage = (short) damage;
		this.timestamp = 0;
		this.distance = (short) distance;
	}
	
	public GunData(byte reloadTime, short shotTime, short magazineSize, short ammo, short damage, long timestamp, short distance) {
		this.reloadTime = reloadTime;
		this.shotTime = shotTime;
		this.magazineSize = magazineSize;
		this.ammo = ammo;
		this.damage = damage;
		this.timestamp = timestamp;
		this.distance = distance;
	}
	
	public GunData pistol() {
		GunData data = new GunData();
		data.damage = 3;
		data.magazineSize = 17;
		data.ammo = data.magazineSize;
		data.reloadTime = 20;
		data.shotTime = 200;
		data.distance = 25;
		return data;
	}
	
	public GunData rifle() {
		GunData data = new GunData();
		data.damage = 5;
		data.magazineSize = 20;
		data.ammo = data.magazineSize;
		data.reloadTime = 60;
		data.shotTime = 200;
		data.distance = 50;
		return data;
	}
	
	public GunData sniper() {
		GunData data = new GunData();
		data.damage = 15;
		data.magazineSize = 5;
		data.ammo = data.magazineSize;
		data.reloadTime = 80;
		data.shotTime = 2000;
		data.distance = 200;
		return data;
	}
}
