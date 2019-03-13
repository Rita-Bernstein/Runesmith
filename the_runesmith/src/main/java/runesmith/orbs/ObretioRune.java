package runesmith.orbs;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import runesmith.actions.runes.ObretioRuneAction;

public class ObretioRune extends RuneOrb {

    public static final int basePotency = 4;
    private static final String ARTIFACT_ID = ArtifactPower.POWER_ID;

    public ObretioRune(int potential) {
        super("Obretio",
                false,
                potential);
    }

    @Override
    public void onEndOfTurn() {
        this.activateEndOfTurnEffect();
        AbstractMonster m = AbstractDungeon.getMonsters().monsters
                .stream()
                .reduce(null, this::getHighestHealthMonsterWithAttackIntent);
        if (m != null) {
            AbstractDungeon.actionManager.addToBottom(new ObretioRuneAction(potential, m));
        } else {
            m = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRng);
            AbstractDungeon.actionManager.addToBottom(new ObretioRuneAction(potential, m));
        }
    }

    private AbstractMonster getHighestHealthMonsterWithAttackIntent(AbstractMonster m1, AbstractMonster m2) {
        int m1Health = 0;
        if (m1 != null && (m1.intent == AbstractMonster.Intent.ATTACK || m1.intent == AbstractMonster.Intent.ATTACK_BUFF || m1.intent == AbstractMonster.Intent.ATTACK_DEBUFF || m1.intent == AbstractMonster.Intent.ATTACK_DEFEND))
            m1Health = m1.currentHealth;
        int m2Health = 0;
        if (m2 != null && (m2.intent == AbstractMonster.Intent.ATTACK || m2.intent == AbstractMonster.Intent.ATTACK_BUFF || m2.intent == AbstractMonster.Intent.ATTACK_DEBUFF || m2.intent == AbstractMonster.Intent.ATTACK_DEFEND))
            m2Health = m2.currentHealth;
        return (m1Health >= m2Health) ? m1 : m2;
    }

    @Override
    public void onBreak() {
        AbstractMonster m = AbstractDungeon.getMonsters().monsters
                .stream()
                .reduce(null, this::getHighestHealthMonsterWithAttackIntent);
        if (m != null) {
            AbstractDungeon.actionManager.addToBottom(new ObretioRuneAction(potential, m));
            AbstractDungeon.actionManager.addToBottom(new ObretioRuneAction(potential, m));
        } else {
            m = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRng);
            AbstractDungeon.actionManager.addToBottom(new ObretioRuneAction(potential, m));
            m = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRng);
            AbstractDungeon.actionManager.addToBottom(new ObretioRuneAction(potential, m));
        }
        this.activateEffect();
    }

    @Override
    public AbstractOrb makeCopy() {
        return new ObretioRune(this.potential);
    }

}