package io.bitbucket.gregoryk1.despat.lab3.task1;

public class GiftPack implements Gift {
    private Gift[] gifts;

    public GiftPack(Gift... gifts) {
        this.gifts = gifts;
    }

    public Gift[] getGifts() {
        return gifts;
    }

    @Override
    public Gift clone() throws CloneNotSupportedException {
        Gift[] gifts = new Gift[this.gifts.length];
        for (int i = 0; i < gifts.length; i++)
            gifts[i] = this.gifts[i].clone();
        GiftPack copy = (GiftPack) super.clone();
        copy.gifts = gifts;
        return copy;
    }

    @Override
    public void accept(GiftVisitor v) {
        v.visit(this);
    }
}
