package models;

import exceptions.InvalidValueException;
import utils.NumUtil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

class LocalDateAdapter extends XmlAdapter<String, LocalDate> implements Serializable {
    public LocalDate unmarshal(String v)  {
        return LocalDate.parse(v);
    }

    public String marshal(LocalDate v)  {
        return v.toString();
    }
}


@XmlAccessorType(XmlAccessType.FIELD)
public class Pen implements Comparable, Serializable {
    private Long id;
    private String name;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private java.time.LocalDate creationDate;

    private Double width_of_kernel;
    private int amount;
    private Double length_of_kernel;
    private double weight;
    private boolean exists;



    public Pen(){}
    public Pen(String name, Double width_of_kernel, int amount, Double length_of_kernel, double weight, boolean exists){
        //this.id = id;
        this.name = name;

        //this.creationDate = LocalDate.now();
        this.width_of_kernel=width_of_kernel;
        this.amount=amount;
        this.length_of_kernel=length_of_kernel;
        this.weight=weight;
        this.exists=exists;


    }


    public Pen(Long id, String name, LocalDate creationDate, Double width_of_kernel, int amount, Double length_of_kernel, double weight, boolean exists){
        this.id = id;
        this.name = name;
        this.creationDate = LocalDate.now();
        this.width_of_kernel=width_of_kernel;
        this.amount=amount;
        this.length_of_kernel=length_of_kernel;
        this.weight=weight;
        this.exists=exists;


    }


    public void checkFields(){
        if(id != 0L){
            if(!NumUtil.isInRange(id, new BigDecimal(0), NumUtil.LONG_MAX)){
                throw new InvalidValueException("Какие то из чисел не входят в рамки ограничения.");
            }
        }else {
            throw new InvalidValueException("Поле id пустое.");
        }


        if (name.isEmpty()) {
            throw new InvalidValueException("Поле name пустое.");
        }

        if(amount != 0L){
            if(!NumUtil.isInRange(amount, new BigDecimal(0), NumUtil.FLOAT_MAX)){
                throw new InvalidValueException("Какие то из чисел не входят в рамки ограничения.");
            }
        }

        if(!NumUtil.isInRange(width_of_kernel, new BigDecimal(0), NumUtil.DOUBLE_MAX) ||
                !NumUtil.isInRange(weight, new BigDecimal(-13), new BigDecimal(15))
        ){
            throw new InvalidValueException("Какие то из чисел не входят в рамки ограничения.");
        }


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Double getWidth_of_kernel() {
        return width_of_kernel;
    }

    public void setWidth_of_kernel(Double width_of_kernel) {
        this.width_of_kernel = width_of_kernel;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Double getLength_of_kernel() {
        return length_of_kernel;
    }

    public void setLength_of_kernel(Double length_of_kernel) {
        this.length_of_kernel = length_of_kernel;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean getExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{").append("\n");
        sb.append("\t").append("id: ").append(this.id).append("\n");
        sb.append("\t").append("name: ").append(this.name).append("\n");
        sb.append("\t").append("width_of_kernel: ").append(this.width_of_kernel).append("\n");
        sb.append("\t").append("exists: ").append(this.exists).append("\n");
        sb.append("\t").append("length_of_kernel: ").append(this.length_of_kernel).append("\n");
        sb.append("\t").append("amount: ").append(this.amount).append("\n");
        sb.append("\t").append("weight: ").append(this.weight).append("\n");
        sb.append("\t").append("creationDate: ").append(this.creationDate).append("\n");

        sb.append("}").append("\n");
        return sb.toString();
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
            return -1;
        }
        if (!(o instanceof Pen)) {
            throw new ClassCastException();
        }
        Pen fo = (Pen) o;
        if (this.getWidth_of_kernel().equals(fo.getWidth_of_kernel())) {
            return 0;
        } else {
            return this.getWidth_of_kernel() < fo.getWidth_of_kernel() ? -1 : 1;
        }
    }
}