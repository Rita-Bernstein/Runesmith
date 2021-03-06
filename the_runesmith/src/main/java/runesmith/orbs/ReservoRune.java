package runesmith.orbs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import runesmith.powers.ReservoPower;

public class ReservoRune extends RuneOrb {

    private AbstractPlayer p = AbstractDungeon.player;

    public ReservoRune(boolean upgraded) {
        super("Reservo",
                upgraded,
                0);
        this.showPotentialValue = false;
        this.useMultiBreak = true;
        this.tc = Color.GREEN.cpy();
    }

    public ReservoRune() {
        this(false);
    }

    @Override
    public void onCraft() {
        super.onCraft();
        int amount = 1;
        if (this.upgraded) {
            amount = 2;
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new RetainCardPower(p, amount), amount));
    }

    @Override
    public void onRemove() {
        if (this.upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p, p, "Retain Cards", 1));
        }
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p, p, "Retain Cards", 1));
    }

    @Override
    public void onBreak() {
        int amount = 2;
        if (this.upgraded) {
            amount = 4;
        }
        if (this.upgraded) {
            AbstractDungeon.actionManager.addToTop(new ReducePowerAction(p, p, "Retain Cards", 1));
        }
        AbstractDungeon.actionManager.addToTop(new ReducePowerAction(p, p, "Retain Cards", 1));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new ReservoPower(amount), amount));
        this.activateEffect();
    }

    @Override
    public void onMultiBreak() {
        int amount = 2;
        if (this.upgraded) {
            amount = 4;
        }
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new ReservoPower(amount), amount));
        this.activateEffect();
    }

    @Override
    public AbstractOrb makeCopy() {
        return new ReservoRune(this.upgraded);
    }

    @Override
    protected void renderText(SpriteBatch sb) {
        if (this.upgraded) {
            if(!this.showBreakValue) {
                //render upgrade +
                FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L,
                        "+", this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET,
                        this.tc, this.fontScale);
            }else{
                FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L,
                        "4", this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET,
                        this.tc, this.fontScale);
            }
        }else{
            if(this.showBreakValue) {
                FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L,
                        "2", this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET,
                        this.tc, this.fontScale);
            }
        }
    }
}