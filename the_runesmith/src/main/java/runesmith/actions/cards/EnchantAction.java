package runesmith.actions.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import runesmith.RunesmithMod;
import runesmith.actions.EnhanceCard;

public class EnchantAction extends AbstractGameAction {

    private boolean isUpgraded;

    public EnchantAction(boolean isUpgraded) {
        this.duration = Settings.ACTION_DUR_MED;
        this.actionType = ActionType.WAIT;
        this.isUpgraded = isUpgraded;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            AbstractPlayer p = AbstractDungeon.player;

            upgradeThenEnhanceAllCardsInGroup(p.hand);

            this.isDone = true;
        }
    }

    private void upgradeThenEnhanceAllCardsInGroup(CardGroup cardGroup) {
        cardGroup.group.forEach(c -> {
            if (isUpgraded) {
                if (c.canUpgrade()) {
                    c.upgrade();
                }
                if (EnhanceCard.canEnhance(c)) {
                    c.superFlash(RunesmithMod.BEIGE.cpy());
                    EnhanceCard.enhance(c);
                    c.applyPowers();
                }
            } else {
                if (c.canUpgrade()) {
                    c.superFlash();
                    c.upgrade();
                    c.applyPowers();
                }
            }
        });
    }

}
