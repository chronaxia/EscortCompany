package com.miaxis.escortcompany.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Escort {
	private String finger0;
	private String finger1;
	private Integer id;
	@Id
	private String escode;
	private String esname;
	private String idcard;
	private Integer esstatus;
	private String comno;
	private String compname;
	private String phoneno;
	private byte[] photo;
	private String opuser;
	private String opdate;
	private String remark;
	private String comid;
	private String opusername;
	private String isCollect;
	private String password;
	@Generated(hash = 984396528)
	public Escort(String finger0, String finger1, Integer id, String escode,
			String esname, String idcard, Integer esstatus, String comno, String compname,
			String phoneno, byte[] photo, String opuser, String opdate, String remark,
			String comid, String opusername, String isCollect, String password) {
		this.finger0 = finger0;
		this.finger1 = finger1;
		this.id = id;
		this.escode = escode;
		this.esname = esname;
		this.idcard = idcard;
		this.esstatus = esstatus;
		this.comno = comno;
		this.compname = compname;
		this.phoneno = phoneno;
		this.photo = photo;
		this.opuser = opuser;
		this.opdate = opdate;
		this.remark = remark;
		this.comid = comid;
		this.opusername = opusername;
		this.isCollect = isCollect;
		this.password = password;
	}
	@Generated(hash = 295686502)
	public Escort() {
	}
	public String getFinger0() {
		return this.finger0;
	}
	public void setFinger0(String finger0) {
		this.finger0 = finger0;
	}
	public String getFinger1() {
		return this.finger1;
	}
	public void setFinger1(String finger1) {
		this.finger1 = finger1;
	}
	public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEscode() {
		return this.escode;
	}
	public void setEscode(String escode) {
		this.escode = escode;
	}
	public String getEsname() {
		return this.esname;
	}
	public void setEsname(String esname) {
		this.esname = esname;
	}
	public String getIdcard() {
		return this.idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public Integer getEsstatus() {
		return this.esstatus;
	}
	public void setEsstatus(Integer esstatus) {
		this.esstatus = esstatus;
	}
	public String getComno() {
		return this.comno;
	}
	public void setComno(String comno) {
		this.comno = comno;
	}
	public String getCompname() {
		return this.compname;
	}
	public void setCompname(String compname) {
		this.compname = compname;
	}
	public String getPhoneno() {
		return this.phoneno;
	}
	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}
	public byte[] getPhoto() {
		return this.photo;
	}
	public void setPhoto(byte[] photo) {
		this.photo = photo;
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
	public String getComid() {
		return this.comid;
	}
	public void setComid(String comid) {
		this.comid = comid;
	}
	public String getOpusername() {
		return this.opusername;
	}
	public void setOpusername(String opusername) {
		this.opusername = opusername;
	}
	public String getIsCollect() {
		return this.isCollect;
	}
	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
