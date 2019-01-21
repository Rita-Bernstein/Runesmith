package runesmith.cards.Runesmith;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import runesmith.actions.RuneChannelAction;
import runesmith.orbs.SpiculumRune;
import runesmith.patches.AbstractCardEnum;

public class SpikedWall extends AbstractRunicCard {
	public static final String ID = "Runesmith:SpikedWall";
	public static final String IMG_PATH = "images/cards/SpikedWall.png"; //<-------- Image needed
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final int COST = 2;
	private static final int BLOCK_AMT = 11;
	private static final int UPGRADE_PLUS_BLOCK = 3;
	private static final int IGNIS_AMT = 2;
	private static final int AQUA_AMT = 1;
	private static final int POTENCY = 3;
	private static final int UPG_POTENCY = 1;
	
	public SpikedWall() {
		super(
			ID,
			NAME,
			IMG_PATH,
			COST,
			DESCRIPTION,
			AbstractCard.CardType.SKILL,
			AbstractCardEnum.RUNESMITH_BEIGE,
			AbstractCard.CardRarity.UNCOMMON,
			AbstractCard.CardTarget.SELF
		);
		this.baseBlock = this.block = BLOCK_AMT;
		this.basePotency = this.potency = POTENCY;
	}
	
	@Override
	public void applyPowers() {
		super.applyPowers();
		if(checkElements(IGNIS_AMT,0,AQUA_AMT)) {
			this.rawDescription = (DESCRIPTION + EXTENDED_DESCRIPTION[0]);
		}else {
			this.rawDescription = (DESCRIPTION);
		}
		initializeDescription();
	}
	
	@Override
	public void onMoveToDiscard(){
		this.rawDescription = DESCRIPTION;
		initializeDescription();
	}
	
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(
			new GainBlockAction(p, p, this.block)
		);
		if (checkElements(IGNIS_AMT,0,AQUA_AMT)) {
			AbstractDungeon.actionManager.addToBottom(
					new RuneChannelAction(
							new SpiculumRune(this.potency)));
		}
	}
	
	public AbstractCard makeCopy() {
		return new SpikedWall();
	}
	
	public void upgrade() {
		if (!this.upgraded) {
		  upgradeName();
		  upgradeBlock(UPGRADE_PLUS_BLOCK);
		  upgradePotency(UPG_POTENCY);
		}
	}
}