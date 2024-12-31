package application;

import java.sql.Date;

public class flowersData {
	private Integer flowerId;
	private String name;
	private String status;
	private Double price;
	private String image;
	private Date date;

	public flowersData(Integer flowerId, String name, String status, Double price, String image, Date date){
		this.flowerId = flowerId;
		this.name = name;
		this.status = status;
		this.price = price;
		this.image = image;
		this.date = date;

	}

	public Integer getFlowerId() {
		return flowerId;
	}
	public String getName() {
		return name;
	}
	public String getStatus() {
		return status;
	}
	public Double getPrice() {
		return price;
	}
	public Date getDate() {
		return date;
	}
	public String getImage() {
		return image;
	}


}