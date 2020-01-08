package com.bebit.scala.templates.cassandra;


import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(
        name = "dummy",
        readConsistency = "ONE",
        writeConsistency = "ONE",
        caseSensitiveKeyspace = false,
        caseSensitiveTable = false)
public class Dummy {

    @PartitionKey(0)
    @Column(name = "service_id")
    private String serviceId;

    @PartitionKey(1)
    @Column(name = "data_id")
    private String dataId;

    @Column(name = "acquisition_day")
    private String acquisitionDay;

    @Column(name = "price")
    private String price;

    @Column(name = "comment")
    private String comment;


    Dummy(){}

    Dummy(String serviceId, String dataId, String acquisitionDay, String price, String comment){
        this.serviceId = serviceId;
        this.dataId = dataId;
        this.acquisitionDay = acquisitionDay;
        this.price = price;
        this.comment = comment;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getAcquisitionDay() {
        return acquisitionDay;
    }

    public void setAcquisitionDay(String acquisitionDay) {
        this.acquisitionDay = acquisitionDay;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
