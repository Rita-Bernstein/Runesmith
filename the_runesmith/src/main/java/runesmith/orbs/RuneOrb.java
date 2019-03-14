package runesmith.orbs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;
import runesmith.powers.ArcReactorPower;
import runesmith.powers.PotentialPower;
import runesmith.relics.PocketReactor;

import java.util.List;
import java.util.stream.Collectors;


public abstract class RuneOrb extends AbstractOrb {
//    private float vfxTimer = 1.0F;
//    private float vfxIntervalMin = 0.2F;
//    private float vfxIntervalMax = 0.7F;
//    private static final float ORB_WAVY_DIST = 0.04F;
//    private static final float PI_4 = 12.566371F;

    public boolean upgraded;
    public boolean useMultiBreak = false;
    boolean showPotentialValue = true;
    protected int potential;
    private String[] descriptions;
    private static int runeCount = 0;

    public RuneOrb(String ID, boolean upgraded, int potential) {
        this.ID = ID;
        this.img = ImageMaster.loadImage("images/orbs/" + ID + ".png");
        if (potential == 0) {
            this.showPotentialValue = false;
        }
        this.upgraded = upgraded;
        this.potential = potential;

        this.descriptions = CardCrawlGame.languagePack.getOrbString(this.ID).DESCRIPTION;
        this.name = CardCrawlGame.languagePack.getOrbString(this.ID).NAME;
        if (this.upgraded) {
            this.name = this.name + "+";
        }

        updateDescription();
    }

    public int getPotential() {
        return potential;
    }

    public void setPotential(int potential) {
        this.potential = potential;
    }

    public static RuneOrb getFirstRune(AbstractPlayer p) {
        return getFirstRune(p, false);
    }

    public static RuneOrb getFirstRune(AbstractPlayer p, Boolean checkDud) {
        return (RuneOrb) p.orbs
                .stream()
                .filter(o -> (o instanceof RuneOrb) && !(o instanceof DudRune && checkDud))
                .findFirst().orElse(null);
    }

    public static List<RuneOrb> getAllRunes(AbstractPlayer p) {
        return getAllRunes(p, false);
    }

    public static List<RuneOrb> getAllRunes(AbstractPlayer p, Boolean checkNonDud) {
        return p.orbs
                .stream()
                .filter(o -> (o instanceof RuneOrb) && !(o instanceof DudRune && checkNonDud))
                .map(RuneOrb.class::cast)
                .collect(Collectors.toList());
    }

    public static List<RuneOrb> getAllRunes(AbstractPlayer p, RuneOrb checkForRune) {
        return p.orbs
                .stream()
                .filter(o -> o.getClass().equals(checkForRune.getClass()))
                .map(RuneOrb.class::cast)
                .collect(Collectors.toList());
    }

    public void activateEffect() {
        float speedTime = 0.6F / AbstractDungeon.player.orbs.size();
        if (Settings.FAST_MODE) {
            speedTime = 0.0F;
        }
//		    AbstractDungeon.effectList.add(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.PLASMA));    
        AbstractDungeon.actionManager.addToTop(new VFXAction(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.PLASMA), speedTime));
    }

    void activateEndOfTurnEffect() {
        float speedTime = 0.6F / AbstractDungeon.player.orbs.size();
        if (Settings.FAST_MODE) {
            speedTime = 0.0F;
        }
//		    AbstractDungeon.effectList.add(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.PLASMA));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.PLASMA), speedTime));
    }

    @Override
    public void updateDescription() {
        String tmpDesc;
        if (!this.upgraded) tmpDesc = this.descriptions[0];
        else tmpDesc = this.descriptions[1];
        tmpDesc = tmpDesc.replace("{2Pot}", this.potential * 2 + "");
        tmpDesc = tmpDesc.replace("{Pot2}", this.potential / 2 + "");
        this.description = tmpDesc.replace("{Pot}", this.potential + "");
    }

    public static RuneOrb getRandomRune(Random rng, int playerPotency){
        return getRandomRune(rng,playerPotency,false);
    }

    public static RuneOrb getRandomRune(Random rng, int playerPotency, boolean noDud) {
        int selectedRune;
        int potency;
        int range = noDud ? 9: 10;
        selectedRune = rng.random(range);
        switch (selectedRune) {
            case 0:
                potency = potencyCal(SpiculumRune.basePotency, playerPotency);
                return new SpiculumRune(potency);
            case 1:
                potency = potencyCal(ObretioRune.basePotency, playerPotency);
                return new ObretioRune(potency);
            case 2:
                potency = potencyCal(FirestoneRune.basePotency, playerPotency);
                return new FirestoneRune(potency);
            case 3:
                potency = potencyCal(IncendiumRune.basePotency, playerPotency);
                return new IncendiumRune(potency);
            case 4:
                return new IndustriaRune();
            case 5:
                potency = potencyCal(MagmaRune.basePotency, playerPotency);
                return new MagmaRune(potency);
            case 6:
                potency = potencyCal(PotentiaRune.basePotency, playerPotency);
                return new PotentiaRune(potency);
            case 7:
                return new PrismaticRune();
            case 8:
                potency = potencyCal(ProtectioRune.basePotency, playerPotency);
                return new ProtectioRune(potency);
            case 9:
                return new ReservoRune();
        }
        return new DudRune();
    }

    private static int potencyCal(int basePotency, int playerPotency) {
        int potency = basePotency + playerPotency;
        if (AbstractDungeon.player.hasRelic(PocketReactor.ID))
            potency -= 2;
        return (potency>=0) ? potency : 0;
    }

    public void onStartOfTurn() {
    }

    public void onCraft() {
        runeCount++;
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower(ArcReactorPower.POWER_ID)) {
            int decAmount = ((TwoAmountPower)p.getPower(ArcReactorPower.POWER_ID)).amount2;
//            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p, p, PotentialPower.POWER_ID, decAmount));
//            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PotentialPower(p, -decAmount), -decAmount));
//            int decAmount = (int)Math.round(p.getPower(PotentialPower.POWER_ID).amount/2.0);
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PotentialPower(p, -decAmount), -decAmount));
        }
    }

    public void onBreak() {
    }

    public void onMultiBreak() {
    }

    public void onRemove() {
    }

    @Override
    public void onEvoke() {
    }

    @Override
    public void applyFocus() {
    }

    @Override
    public void triggerEvokeAnimation() {
        CardCrawlGame.sound.play("DUNGEON_TRANSITION", 0.1F);
        AbstractDungeon.effectsQueue.add(new PlasmaOrbActivateEffect(this.cX, this.cY));
    }

    @Override
    protected void renderText(SpriteBatch sb) {
        if (this.showPotentialValue) {
            FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L,
                    Integer.toString(this.potential), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET, Color.WHITE.cpy(), this.fontScale);
        }
    }

    @Override
    public void playChannelSFX() {
        CardCrawlGame.sound.play("AUTOMATON_ORB_SPAWN", 0.1F);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.c);
        sb.draw(img, this.cX - 48.0F + this.bobEffect.y / 4.0F, this.cY - 48.0F + this.bobEffect.y / 4.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, false, false);

        renderText(sb);
        this.hb.render(sb);
    }

    public static int getRuneCount() {
        return runeCount;
    }

    public static void runeCountDown() {
        runeCount--;
    }

    public static void runeCountReset() {
        runeCount = 0;
    }

    public static int getMaxRune(AbstractPlayer p) {
        return 7;
    }

}
