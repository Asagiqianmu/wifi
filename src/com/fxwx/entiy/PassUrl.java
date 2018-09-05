package com.fxwx.entiy;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t7_pass_url")
public class PassUrl {
	
	@Id
	private int id;// 主键
	
	@Column("auth_url")
	private String authUrl;// 放行url
	
	@Column("url_param")
	private String urlParam;// 放行url所需参数
	
	@Column("system_type")
	private String solraysType;//系统类型
	
	@Column("offline_url")
	private String offlineUrl;//下线跳转地址
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the authUrl
	 */
	public String getAuthUrl() {
		return authUrl;
	}

	/**
	 * @param authUrl the authUrl to set
	 */
	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	/**
	 * @return the urlParam
	 */
	public String getUrlParam() {
		return urlParam;
	}

	/**
	 * @param urlParam the urlParam to set
	 */
	public void setUrlParam(String urlParam) {
		this.urlParam = urlParam;
	}
	

	/**
	 * @return the solraysType
	 */
	public String getSolraysType() {
		return solraysType;
	}

	/**
	 * @param solraysType the solraysType to set
	 */
	public void setSolraysType(String solraysType) {
		this.solraysType = solraysType;
	}

	
	public String getOfflineUrl() {
		return offlineUrl;
	}

	public void setOfflineUrl(String offlineUrl) {
		this.offlineUrl = offlineUrl;
	}

	@Override
	public String toString() {
		return "PassUrl [id=" + id + ", authUrl=" + authUrl + ", urlParam="
				+ urlParam + ", solraysType=" + solraysType + ", offlineUrl="
				+ offlineUrl + "]";
	}

	

	
	

}
