package End;

//枚举类: 特殊的类(class),不可再继承,自动继承于 Enum类
public enum colorEnum {
    //枚举实例化对象
    RED(0, "红色"), BLACK(1, "黑色"),
    GREEN(2, "绿色"), WHITE(3.1, "白色"), etc;

    public String color;
    public double diyOrdinal;//序数

    //枚举的构造方法 默认是私有的
    private colorEnum(double diyOrdinal, String color) {
        this.diyOrdinal = diyOrdinal;
        this.color = color;
    }

    colorEnum() {
    }

    @Override
    public String toString() {
        return "color='" + color + '\'' +
                ", diyOrdinal=" + diyOrdinal;
    }

    public static void main(String[] args) {
        colorEnum[] testEnums = colorEnum.values();//以数组形式返回枚举类型的所有成员

        for (int i = 0; i < testEnums.length; i++) {
            System.out.println(testEnums[i] + " " + "\t下标:" + testEnums[i].ordinal() + " ");
        }
        System.out.println("==================================================");

        colorEnum testEnum1 = colorEnum.valueOf("WHITE");//将 普通字符串 转换为 对应的枚举实例
        System.out.println(testEnum1);

        //对比方法(Enum里有)
        System.out.println(BLACK.compareTo(GREEN));
    }


    public static void main1(String[] args) {
        colorEnum testEnum = colorEnum.BLACK;//设置为对应的枚举实例
        switch (testEnum) {
            case RED:
                System.out.println("RED");
                break;
            case BLACK:
                System.out.println("BLACK");
                break;
            case GREEN:
                System.out.println("GREEN");
                break;
            default:
                System.out.println("etc");
                break;
        }
    }


}
