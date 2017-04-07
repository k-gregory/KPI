package io.bitbucket.gregoryk1.despat.lab3.task1;

/**
 * Created by grego on 08.04.2017.
 */
public class Application {
    public static void main(String ... args) throws CloneNotSupportedException {
        Gift maleGiftPackPrototype = new GiftPack(
                new SweetRoll("eggs"),
                new CarModel("Audi")
        );
        Gift femaleGiftPackPrototype = new GiftPack(
                new SweetRoll("mayonnaise"),
                new Doll()
        );

        GiftVisitor ivan = new MaleGiftVisitor("Ivan", 3);
        GiftVisitor angryIvan = new MaleGiftVisitor("Angry Ivan", 1);
        GiftVisitor nastya = new FemaleGiftVisitor("Nastya", 2);

        maleGiftPackPrototype.clone().accept(angryIvan);
        System.out.println();

        femaleGiftPackPrototype.clone().accept(ivan);
        System.out.println();

        maleGiftPackPrototype.clone().accept(ivan);
        System.out.println();

        femaleGiftPackPrototype.clone().accept(nastya);
    }
}
