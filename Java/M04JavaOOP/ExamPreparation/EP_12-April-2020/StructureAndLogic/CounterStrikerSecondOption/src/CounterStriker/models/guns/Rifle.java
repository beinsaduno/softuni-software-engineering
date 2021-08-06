package CounterStriker.models.guns;

public class Rifle extends GunImpl{

    private static final int COUNT_OF_SHOTS = 10;


    public Rifle(String name, int bulletsCount) {
        super(name, bulletsCount);
    }

    @Override
    public int fire() {
        if (super.getBulletsCount() < COUNT_OF_SHOTS) {
            return super.fire();
        }
        super.setBulletsCount(super.getBulletsCount() - COUNT_OF_SHOTS);
        return COUNT_OF_SHOTS;
    }
}
