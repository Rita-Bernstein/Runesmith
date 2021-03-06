package runesmith.orbs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class PrismaticRune extends RuneOrb {

    PrismaticRune() {
        this(false);
    }

    public PrismaticRune(boolean upgraded) {
        super("Prismatic",
                upgraded,
                0);
        this.tc = Color.GREEN.cpy();
    }

    @Override
    public void onStartOfTurn() {
        this.activateEndOfTurnEffect();
        addRandomCardToHand();
    }

    @Override
    public void onBreak() {
        addRandomCardToHand(true);
        addRandomCardToHand(true);
        this.activateEffect();
    }

    private void addRandomCardToHand() {
        addRandomCardToHand(false);
    }

    private void addRandomCardToHand(boolean onBreak) {
        AbstractCard tmp = AbstractDungeon.returnTrulyRandomCardInCombat().makeCopy();
        if (upgraded)
            tmp.upgrade();
        tmp.setCostForTurn(0);
        if (onBreak)
            AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(tmp, false));
        else
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(tmp, false));
    }

    @Override
    public AbstractOrb makeCopy() {
        return new PrismaticRune(upgraded);
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
                        "2+", this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET,
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
