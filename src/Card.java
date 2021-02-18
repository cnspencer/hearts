import javafx.scene.image.Image;

/**
 * @author C. N. Spencer
 *
 */

public class Card {
    private Numbers cardNum;
    private Suits cardType;
    private int value;
    private Image image;
//    private Image back = new Image("/GameData/back.png");
    private boolean isSelected = false;
    Card(Numbers cardNum, Suits cardType) {
        this.cardNum = cardNum;
        this.cardType = cardType;
        this.value = cardNum.ordinal();
        this.image = new Image("GameData/" + this.cardNum.toString().toUpperCase() + "of" + this.cardType.toString().toUpperCase() + ".jpg", 100.1, 145.7, true, true, true);
        switch (cardNum) {
            case ACE:
                this.value = 14;
            case TWO:
                this.value = 2;
            case THREE:
                this.value = 3;
            case FOUR:
                this.value = 4;
            case FIVE:
                this.value = 5;
            case SIX:
                this.value = 6;
            case SEVEN:
                this.value = 7;
            case EIGHT:
                this.value = 8;
            case NINE:
                this.value = 9;
            case TEN:
                this.value = 10;
            case JACK:
                this.value = 11;
            case QUEEN:
                this.value = 12;
            case KING:
                this.value = 13;
        }
    }

//    public Image showBack() {
//        return this.back;
//    }

    public Image showFront() {
        return this.image;
    }

    public int getValue() {
        return this.value;
    }

    public Suits getSuit() {
        return this.cardType;
    }

    public Numbers getNumber() {
        return this.cardNum;
    }

    public int getScore() {
        if (this.cardNum.equals(Numbers.QUEEN)) {
            if (this.cardType.equals(Suits.SPADES)) {
                return 13;
            }
        } else if (this.cardType.equals(Suits.HEARTS)) {
            return 1;
        }
        return 0;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean select) {
        this.isSelected = select;
    }

    public void toggleSelected() {
        this.isSelected = !this.isSelected;
    }

    @Override
    public String toString() {
        return this.cardNum.toString() + " of " + this.cardType.toString();
    }
}