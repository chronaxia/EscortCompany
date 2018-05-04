package com.miaxis.escortcompany.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Car {
	private Integer id;
	@Id
	private String carcode;
	private String plateno;
	private Integer compid;
	private String compname;
	private String opuser;
	private String opdate;
	private String remark;
	private String compno;
	private String carphoto;
	private String opusername;
	private String rfid;
	@Generated(hash = 1202529320)
	public Car(Integer id, String carcode, String plateno, Integer compid,
			String compname, String opuser, String opdate, String remark, String compno,
			String carphoto, String opusername, String rfid) {
		this.id = id;
		this.carcode = carcode;
		this.plateno = plateno;
		this.compid = compid;
		this.compname = compname;
		this.opuser = opuser;
		this.opdate = opdate;
		this.remark = remark;
		this.compno = compno;
		this.carphoto = carphoto;
		this.opusername = opusername;
		this.rfid = rfid;
	}
	@Generated(hash = 625572433)
	public Car() {
	}
	public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCarcode() {
		return this.carcode;
	}
	public void setCarcode(String carcode) {
		this.carcode = carcode;
	}
	public String getPlateno() {
		return this.plateno;
	}
	public void setPlateno(String plateno) {
		this.plateno = plateno;
	}
	public Integer getCompid() {
		return this.compid;
	}
	public void setCompid(Integer compid) {
		this.compid = compid;
	}
	public String getCompname() {
		return this.compname;
	}
	public void setCompname(String compname) {
		this.compname = compname;
	}
	public String getOpuser() {
		return this.opuser;
	}
	public void setOpuser(String opuser) {
		this.opuser = opuser;
	}
	public String getOpdate() {
		return this.opdate;
	}
	public void setOpdate(String opdate) {
		this.opdate = opdate;
	}
	public String getRemark() {
		return this.remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCompno() {
		return this.compno;
	}
	public void setCompno(String compno) {
		this.compno = compno;
	}
	public String getCarphoto() {
		return this.carphoto;
	}
	public void setCarphoto(String carphoto) {
		this.carphoto = carphoto;
	}
	public String getOpusername() {
		return this.opusername;
	}
	public void setOpusername(String opusername) {
		this.opusername = opusername;
	}
	public String getRfid() {
		return this.rfid;
	}
	public void setRfid(String rfid) {
		this.rfid = rfid;
	}
	
	
}
