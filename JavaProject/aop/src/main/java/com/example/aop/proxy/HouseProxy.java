package com.example.aop.proxy;


//实际代理执行者(房屋中介拿到了 房东 授的权再加工)
public class HouseProxy implements HouseSubject {

    private HouseSubject houseSubject;//要代理的权限

    public HouseProxy(HouseSubject houseSubject) {
        this.houseSubject = houseSubject;
    }

    //对外提供的服务
    @Override
    public void rentHouse() {
        System.out.println("开始进行代理");
        houseSubject.rentHouse();
        System.out.println("结束代理....");
    }

    //对外提供的服务
    @Override
    public void saleHouse() {
        System.out.println("开始进行代理");
        houseSubject.saleHouse();
        System.out.println("结束代理....");
    }
}
