package runesmith.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import runesmith.actions.EnhanceCard;
import runesmith.patches.EnhanceCountField;

public class GrindstonePower extends AbstractPower {

    public static final String POWER_ID = "Runesmith:GrindstonePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean isAfterFirstUsed = false;

    public GrindstonePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = true;
        updateDescription();
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/Grindstone.png"), 0, 0, 84, 84);  //<-------- NEED SOME IMG
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/GrindstoneSmall.png"), 0, 0, 32, 32); //<-------- NEED SOME IMG
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, "Runesmith:GrindstonePower"));
        }
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (isAfterFirstUsed) {
            if (card.canUpgrade()) {
                flash();
                card.upgrade();
            } else if (EnhanceCard.canEnhance(card)) {
                flash();
                if(EnhanceCountField.enhanceReset.get(card)){
                    EnhanceCountField.enhanceReset.set(card, false);
                    EnhanceCountField.enhanceCount.set(card, 0);
                }
                EnhanceCard.enhance(card);
            }
        } else
            isAfterFirstUsed = true;
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, "Runesmith:GrindstonePower", 1));
        }
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }

}
