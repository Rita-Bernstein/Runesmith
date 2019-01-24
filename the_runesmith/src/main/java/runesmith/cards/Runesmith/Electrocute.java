package runesmith.cards.Runesmith;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import runesmith.actions.StasisCardInDeckAction;
import runesmith.patches.AbstractCardEnum;

public class Electrocute extends CustomCard {
	public static final String ID = "Runesmith:Electrocute";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String IMG_PATH = "images/cards/Electrocute.png"; //<-------------- need some img
	private static final int COST = 1;
	private static final int ATTACK_DMG = 10;
	private static final int UPGRADE_PLUS_DMG = 3;
	private static final int SELF_DMG_AMT = 4;
	private static final int UPG_SELF_DMG_AMT = -1;
	private static final int STASIS_AMT = 1;

	public Electrocute() {
		super(
			ID,
			NAME,
			IMG_PATH,
			COST,
			DESCRIPTION,
			CardType.ATTACK,
			AbstractCardEnum.RUNESMITH_BEIGE,
			CardRarity.UNCOMMON,
			CardTarget.ENEMY
		);
		this.baseDamage = this.damage = ATTACK_DMG;
		this.baseMagicNumber = this.magicNumber = SELF_DMG_AMT;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		float speedTime = (Settings.FAST_MODE)  ? 0.0F : 0.2F;
		AbstractDungeon.actionManager.addToBottom(
			new DamageAction(
				m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.NONE
			)
		);
		AbstractDungeon.actionManager.addToTop(new VFXAction(new com.megacrit.cardcrawl.vfx.combat.LightningEffect(m.drawX, m.drawY), speedTime));
		AbstractDungeon.actionManager.addToTop(new com.megacrit.cardcrawl.actions.utility.SFXAction("ORB_LIGHTNING_EVOKE"));
		
		AbstractDungeon.actionManager.addToBottom(
				new StasisCardInDeckAction(STASIS_AMT));
	}

	public AbstractCard makeCopy() {
		return new Electrocute();
	}

	public void upgrade() {
		if (!this.upgraded) {
		  upgradeName();
		  upgradeDamage(UPGRADE_PLUS_DMG);
		  upgradeMagicNumber(UPG_SELF_DMG_AMT);
		}
	}
}