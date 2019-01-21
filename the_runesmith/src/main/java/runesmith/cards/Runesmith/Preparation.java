package runesmith.cards.Runesmith;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import runesmith.actions.ApplyElementsPowerAction;
import runesmith.patches.AbstractCardEnum;

public class Preparation extends CustomCard{
	public static final String ID = "Runesmith:Preparation";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String IMG_PATH = "images/cards/Preparation.png"; //<-------------- need some img
	private static final int COST = 1;
	private static final int BASE_AMT = 2;
	private static final int UPG_BASE_AMT = 2;
	private static final int DRAW_AMT = 1;
	
	public Preparation() {
		this(0);
	}
	
	public Preparation(int upgrades) {
		super(
			ID,
			NAME,
			IMG_PATH,
			COST,
			DESCRIPTION,
			CardType.SKILL,
			AbstractCardEnum.RUNESMITH_BEIGE,
			CardRarity.COMMON,
			CardTarget.SELF
		);
		this.baseMagicNumber = this.magicNumber = BASE_AMT;
		this.exhaust = true;
	}
	
	public AbstractCard makeCopy() {
		return new Preparation(this.timesUpgraded);
	}

	@Override
	public void upgrade() {
		upgradeMagicNumber(UPG_BASE_AMT);
		this.timesUpgraded += 1;
		this.upgraded = true;
		this.name = (NAME + "+" + this.timesUpgraded);
		initializeTitle();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(
				new ApplyElementsPowerAction(p,p,magicNumber,magicNumber,magicNumber));
		AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, DRAW_AMT));
	}
	
	@Override
	public boolean canUpgrade() {
		if (this.timesUpgraded >= 4)
			return false;
		return true;
	}
	
}