package com.example.aop.proxy;


//原 HouseSubjectClass
public class RealHouseSubjectClass implements HouseSubject {
    public void rentHouse() {
        System.out.println("我是房东, 我要出租房子....");
    }

    @Override
    public void saleHouse() {
        System.out.println("我是房东需要出售房子");
    }

}
