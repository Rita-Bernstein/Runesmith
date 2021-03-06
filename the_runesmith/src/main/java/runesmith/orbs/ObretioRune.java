package runesmith.orbs;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import runesmith.actions.runes.ObretioRuneAction;

public class ObretioRune extends RuneOrb {

    public static final int basePotency = 4;

    public ObretioRune(int potential) {
        super("Obretio",
                false,
                potential);
    }

    @Override
    public void onEndOfTurn() {
        this.activateEndOfTurnEffect();
        AbstractDungeon.actionManager.addToBottom(new ObretioRuneAction(potential));
    }

    @Override
    public void onBreak() {
        AbstractDungeon.actionManager.addToTop(new ObretioRuneAction(potential));
        AbstractDungeon.actionManager.addToTop(new ObretioRuneAction(potential));
        this.activateEffect();
    }

    @Override
    public AbstractOrb makeCopy() {
        return new ObretioRune(this.potential);
    }

}