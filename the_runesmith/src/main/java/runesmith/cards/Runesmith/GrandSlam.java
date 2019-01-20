package runesmith.cards.Runesmith;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import runesmith.actions.cards.GrandSlamAction;
import runesmith.patches.AbstractCardEnum;

public class GrandSlam extends CustomCard {
	public static final String ID = "Runesmith:GrandSlam";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String DESCRIPTION_UPG = cardStrings.UPGRADE_DESCRIPTION;
	public static final String IMG_PATH = "images/cards/GrandSlam.png"; //<-------------- need some img
	private static final int COST = 2;
	private static final int ATTACK_DMG = 13;

	public GrandSlam() {
		super(
			ID,
			NAME,
			IMG_PATH,
			COST,
			DESCRIPTION,
			CardType.ATTACK,
			AbstractCardEnum.RUNESMITH_BEIGE,
			CardRarity.RARE,
			CardTarget.ENEMY
		);
		this.baseDamage = this.damage = ATTACK_DMG;
		this.exhaust = true;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(
			new DamageAction(
				m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_HEAVY
			)
		);
		AbstractDungeon.actionManager.addToBottom(
				new GrandSlamAction());
		if (upgraded) 
			AbstractDungeon.actionManager.addToBottom(
					new GrandSlamAction());
	}

	public AbstractCard makeCopy() {
		return new GrandSlam();
	}

	public void upgrade() {
		if (!this.upgraded) {
		  upgradeName();
		  this.rawDescription = DESCRIPTION_UPG;
		  initializeDescription();
		}
	}
}