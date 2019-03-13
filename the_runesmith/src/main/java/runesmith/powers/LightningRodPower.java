package runesmith.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class LightningRodPower extends AbstractPower {

    public static final String POWER_ID = "Runesmith:LightningRodPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String[] ELEMENTS_POWER_ID = {IgnisPower.POWER_ID, TerraPower.POWER_ID, AquaPower.POWER_ID};

    public LightningRodPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/Aqua.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/AquaSmall.png"), 0, 0, 32, 32);
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0)
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    public int onLoseHp(int damageAmount) {
        for (String powerStr : ELEMENTS_POWER_ID)
            damageAmount = elementsLostCheck(damageAmount, powerStr);
        return damageAmount;
    }

    private int elementsLostCheck(int damageAmount, String elementID) {
        AbstractPlayer p = AbstractDungeon.player;
        if (damageAmount > 0 && p.hasPower(elementID)) {
            AbstractPower power = p.getPower(elementID);
            int element = power.amount;
            if (damageAmount > element) {
                power.reducePower(element);
                damageAmount -= element;
            } else {
                power.reducePower(damageAmount);
                damageAmount = 0;
            }
        }
        return damageAmount;
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer)
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

}