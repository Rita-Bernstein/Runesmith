package runesmith.cards.Runesmith;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import runesmith.actions.ApplyElementsPowerAction;
import runesmith.patches.AbstractCardEnum;

import static runesmith.patches.CardTagEnum.RS_CHISEL;

public class EnergizedChisel extends CustomCard {
    public static final String ID = "Runesmith:EnergizedChisel";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "images/cards/EnergizedChisel.png"; //<-------------- need some img
    private static final int COST = 1;
    private static final int ATTACK_DMG = 3;
    private static final int BLOCK_AMT = 3;
    private static final int WEAK_AMT = 1;
    private static final int UPGRADE_PLUS_ALL = 1;
    private static final int ELEMENT_AMT = 1;

    public EnergizedChisel() {
        super(
                ID,
                NAME,
                IMG_PATH,
                COST,
                DESCRIPTION,
                CardType.ATTACK,
                AbstractCardEnum.RUNESMITH_BEIGE,
                CardRarity.COMMON,
                CardTarget.ENEMY
        );
        this.baseDamage = ATTACK_DMG;
        this.baseBlock = BLOCK_AMT;
        this.baseMagicNumber = this.magicNumber = WEAK_AMT;
        this.tags.add(RS_CHISEL);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(
                        m,
                        new DamageInfo(p, this.damage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL
                )
        );
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(p, p, this.block)
        );
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p,
                new WeakPower(m, this.magicNumber, false), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyElementsPowerAction(p, p, ELEMENT_AMT, 0, ELEMENT_AMT));
//		AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, 
//				new IgnisPower(p, ELEMENT_AMT),ELEMENT_AMT));
//		AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, 
//				new AquaPower(p, ELEMENT_AMT),ELEMENT_AMT));
    }

    public AbstractCard makeCopy() {
        return new EnergizedChisel();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_ALL);
            upgradeBlock(UPGRADE_PLUS_ALL);
            upgradeMagicNumber(UPGRADE_PLUS_ALL);
        }
    }
}
