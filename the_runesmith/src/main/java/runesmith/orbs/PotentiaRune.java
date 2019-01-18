package runesmith.orbs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import runesmith.powers.PotentialDownPower;
import runesmith.powers.PotentialPower;

public class PotentiaRune extends RuneOrb {
	
	public static final int basePotency = 2;
	
	private AbstractPlayer p = AbstractDungeon.player;
	public PotentiaRune(int potency) {
		super( "Potentia",
				false,
				potency);
		this.useMultiBreak = true;

	}
	
	@Override
	public void onCraft() {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PotentialPower(p, this.potential), this.potential));
	}
	
	@Override
	public void onBreak() {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PotentialPower(p, -potential), -potential));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PotentialPower(p, potential), potential));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PotentialPower(p, potential), potential));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PotentialDownPower(p, potential), potential));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PotentialDownPower(p, potential), potential));
	}

	@Override
	public void onMultiBreak() {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PotentialPower(p, potential), potential));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PotentialPower(p, potential), potential));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PotentialDownPower(p, potential), potential));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PotentialDownPower(p, potential), potential));
	}
	
	@Override
	public AbstractOrb makeCopy() { return new PotentiaRune(potential); }
}