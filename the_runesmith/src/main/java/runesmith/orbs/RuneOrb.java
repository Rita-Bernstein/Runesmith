package runesmith.orbs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;



public abstract class RuneOrb extends AbstractOrb {
	private float vfxTimer = 1.0F;
	private float vfxIntervalMin = 0.2F;
	private float vfxIntervalMax = 0.7F;
	private static final float ORB_WAVY_DIST = 0.04F;
	private static final float PI_4 = 12.566371F;
	
	public boolean upgraded = false;
	public boolean showPotentialValue = true;
	public int potential = 0;
	private String[] descriptions;
	
	public RuneOrb(String ID, boolean upgraded, int potential) {
		this.ID = ID;
		this.img = ImageMaster.loadImage("images/orbs/"+ID+".png");
		if(potential == 0) {
			this.showPotentialValue = false;
		}
		this.upgraded = upgraded;
		this.potential = potential;
		
		
		this.descriptions = CardCrawlGame.languagePack.getOrbString(this.ID).DESCRIPTION;
		this.name = CardCrawlGame.languagePack.getOrbString(this.ID).NAME;
		if (this.upgraded) { this.name = this.name + "+"; }
		
		updateDescription();
	}
	
	public void activateEffect() {
		float speedTime = 0.6F / AbstractDungeon.player.orbs.size();
			if (Settings.FAST_MODE) {
				speedTime = 0.0F;
		    }
		    AbstractDungeon.effectList.add(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.PLASMA));    
		    // AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.PLASMA), speedTime));
	}
	
	@Override
	public void updateDescription() {
		String tmpDesc;
		if(this.upgraded)		tmpDesc = this.descriptions[0];
		else tmpDesc = this.descriptions[1];
		this.description = tmpDesc.replace("{pot}", this.potential+"");
	}
	
	public void onStartOfTurn() {
		
	}
	
	public void onCardUse(AbstractCard c) {}

	public void onCardDraw(AbstractCard c) {}

	public void onBreak() {}

	public void atTurnStartPostDraw() {}
	 
	@Override
	public void onEvoke() {
		
	}
	 
	@Override
	public void applyFocus()
	{
	}
	
	@Override
	public void triggerEvokeAnimation()
	{
		CardCrawlGame.sound.play("DUNGEON_TRANSITION", 0.1F);
		AbstractDungeon.effectsQueue.add(new PlasmaOrbActivateEffect(this.cX, this.cY));
	}
	 
	@Override
	protected void renderText(SpriteBatch sb)
	{
		if (!this.showEvokeValue && this.showPotentialValue) {
			FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, 
				Integer.toString(this.potential), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET, Color.PURPLE.cpy(), this.fontScale);
		}
	}
	
	@Override
	public void playChannelSFX()
	{
		CardCrawlGame.sound.play("AUTOMATON_ORB_SPAWN", 0.1F);
	}
	
	@Override
	public void render(SpriteBatch sb)
	{
		sb.setColor(this.c);
		sb.draw(img, this.cX - 48.0F + this.bobEffect.y / 4.0F, this.cY - 48.0F + this.bobEffect.y / 4.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, false, false);
	    
	    renderText(sb);
	    this.hb.render(sb);
	}	
}