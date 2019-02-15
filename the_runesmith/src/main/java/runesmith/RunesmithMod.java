package runesmith;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import elementalist_mod.ElementalistMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import runesmith.cards.Runesmith.*;
import runesmith.character.player.RunesmithCharacter;
import runesmith.helpers.KeywordWithProper;
import runesmith.helpers.PotencyVariable;
import runesmith.patches.CardStasisStatus;
import runesmith.patches.ElementsGainedCountField;
import runesmith.patches.EnhanceCountField;
import runesmith.patches.PlayerClassEnum;
import runesmith.powers.AquaPower;
import runesmith.powers.IgnisPower;
import runesmith.powers.TerraPower;
import runesmith.relics.*;
import runesmith.ui.ElementsCounter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static runesmith.patches.AbstractCardEnum.RUNESMITH_BEIGE;


@SpireInitializer
public class RunesmithMod implements PostExhaustSubscriber,
        PostBattleSubscriber,
        PostDungeonInitializeSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber,
        EditRelicsSubscriber,
        EditCardsSubscriber,
        EditStringsSubscriber,
        OnCardUseSubscriber,
        EditKeywordsSubscriber,
        OnPowersModifiedSubscriber,
        OnPlayerLoseBlockSubscriber,
        PostDrawSubscriber,
        PreMonsterTurnSubscriber,
        OnStartBattleSubscriber{

    public static final Logger logger = LogManager.getLogger(RunesmithMod.class.getName());

    private static final String RUNESMITH_BUTTON = "images/character/runesmithButton.png";
    private static final String RUNESMITH_PORTRAIT = "images/character/runesmithPortrait.png";
    public static Color BEIGE = new Color(175f / 255f, 145f / 255f, 100f / 255f, 1f);

    private static final String CARD_STRING = "localization/RuneSMod_Cards.json";
    private static final String RELIC_STRING = "localization/RuneSMod_Relics.json";
    private static final String POWER_STRING = "localization/RuneSMod_Powers.json";
    private static final String POTION_STRING = "localization/RuneSMod_Potions.json";
    private static final String UI_STRING = "localization/RuneSMod_UI.json";
    private static final String KEYWORD_STRING = "localization/RuneSMod_Keywords.json";
    private static final String EVENT_STRING = "localization/RuneSMod_Events.json";

    private List<AbstractCard> cardsToAdd = new ArrayList<>();
    private List<CustomRelic> relicsToAddOnlyThisClass = new ArrayList<>();
    private List<CustomRelic> relicsToAddAllClass = new ArrayList<>();

    private static boolean renderElementsCounter = false;
    private static ElementsCounter elementsCounter;
    private static boolean elementalistEnabled = false;
    private static Texture ELEMENTS_GREEN_MASK = ImageMaster.loadImage("images/ui/elements/GMask.png");

    public RunesmithMod() {
        BaseMod.subscribe(this);
        logger.info("creating the color : RUNESMITH_BEIGE");
        BaseMod.addColor(
                RUNESMITH_BEIGE,
                BEIGE,
                "images/cardui/512/bg_attack_beige.png", //attackBg
                "images/cardui/512/bg_skill_beige.png", //skillBg
                "images/cardui/512/bg_power_beige.png", //powerBg
                "images/cardui/512/card_beige_orb.png", //energyOrb
                "images/cardui/1024/bg_attack_beige.png", //attackBgPortrait
                "images/cardui/1024/bg_skill_beige.png", //skillBgPortrait
                "images/cardui/1024/bg_power_beige.png", //powerBgPortrait
                "images/cardui/1024/card_beige_orb.png", //energyOrbPortrait
                "images/cardui/description_beige_orb.png"); //cardEnergyOrb
    }

    public static void initialize() {
        RunesmithMod mod = new RunesmithMod();
    }

    @Override
    public void receiveEditCharacters() {
        logger.info("begin editing characters");
        BaseMod.addCharacter(new RunesmithCharacter(CardCrawlGame.playerName),
                RUNESMITH_BUTTON,
                RUNESMITH_PORTRAIT,
                PlayerClassEnum.RUNESMITH_CLASS);
        logger.info("done editing characters");
    }

    public void loadAudio() {
        HashMap<String, Sfx> map = (HashMap<String, Sfx>) ReflectionHacks.getPrivate(CardCrawlGame.sound, SoundMaster.class, "map");
        map.put("RUNESMITH_HAMMER", new Sfx("audio/HammerDoubleHit.ogg", false));
    }

    @Override
    public void receiveEditStrings() {
        logger.info("start editing strings");

        String relicStrings, cardStrings, powerStrings, potionStrings, relic, card, power, potion, ui, uiStrings, event, eventStrings;

        logger.info("lang == eng");
        card = CARD_STRING;
        relic = RELIC_STRING;
        power = POWER_STRING;
        potion = POTION_STRING;
        ui = UI_STRING;
        event = EVENT_STRING;
		
		/*if (Settings.language == Settings.GameLanguage.ZHS) {
		  logger.info("lang == zhs");
		  card = CARD_STRING_ZH;
		  relic = RELIC_STRING_ZH;
		  power = POWER_STRING_ZH;
		  potion = POTION_STRING_ZH;
		} else if (Settings.language == Settings.GameLanguage.JPN) {
		  logger.info("lang == jpn");
		  card = CARD_STRING_JP;
		  relic = RELIC_STRING_JP;
		  power = POWER_STRING_JP;
		  potion = POTION_STRING_JP;
		} else if (Settings.language == Settings.GameLanguage.ZHT) {
		  logger.info("lang == zht");
		  card = CARD_STRING_ZHT;
		  relic = RELIC_STRING_ZHT;
		  power = POWER_STRING_ZHT;
		  potion = POTION_STRING_ZHT;
		} else if (Settings.language == Settings.GameLanguage.KOR) {
		  logger.info("lang == kor");
		  card = CARD_STRING_KR;
		  relic = RELIC_STRING_KR;
		  power = POWER_STRING_KR;
		  potion = POTION_STRING_KR;
		} else if(Settings.language == Settings.GameLanguage.FRA) {
		  logger.info("lang == fra");
		  card = CARD_STRING_FR;
		  relic = RELIC_STRING_FR;
		  power = POWER_STRING_FR;
		  potion = POTION_STRING_FR;
		} else {
		  logger.info("lang == eng");
		  card = CARD_STRING;
		  relic = RELIC_STRING;
		  power = POWER_STRING;
		  potion = POTION_STRING;
		}*/

        relicStrings = Gdx.files.internal(relic).readString(
                String.valueOf(StandardCharsets.UTF_8)
        );
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);

        cardStrings = Gdx.files.internal(card).readString(
                String.valueOf(StandardCharsets.UTF_8)
        );
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);

        powerStrings = Gdx.files.internal(power).readString(
                String.valueOf(StandardCharsets.UTF_8)
        );
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);

        potionStrings = Gdx.files.internal(potion).readString(
                String.valueOf(StandardCharsets.UTF_8)
        );

        uiStrings = Gdx.files.internal(ui).readString(
                String.valueOf(StandardCharsets.UTF_8)
        );
        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);

        eventStrings = Gdx.files.internal(event).readString(
                String.valueOf(StandardCharsets.UTF_8)
        );
        BaseMod.loadCustomStrings(EventStrings.class, eventStrings);

        String orbStrings = Gdx.files.internal("localization/RuneSMod_Orbs.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(OrbStrings.class, orbStrings);
//		BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);

        logger.info("done editing strings");
    }

    @Override
    public void receiveEditKeywords() {
        logger.info("Setting up custom keywords");

        String keywordsPath;
	    /*switch (Settings.language) {
	      case ZHT:
	        keywordsPath = KEYWORD_STRING_ZHT;
	        break;
	      case ZHS:
	        keywordsPath = KEYWORD_STRING_ZHS;
	        break;
	      case KOR:
	        keywordsPath = KEYWORD_STRING_KR;
	        break;
	      case JPN:
	        keywordsPath = KEYWORD_STRING_JP;
	        break;
	      case FRA:
	      keywordsPath = KEYWORD_STRING_FR;
	      break;
	      default:
	        keywordsPath = KEYWORD_STRING;
	        break;
	    }*/
        keywordsPath = KEYWORD_STRING;

        Gson gson = new Gson();
        Keywords keywords;
        keywords = gson.fromJson(loadJson(keywordsPath), Keywords.class);
        for (KeywordWithProper key : keywords.keywords) {
            logger.info("Loading keyword : " + key.NAMES[0]);
            BaseMod.addKeyword("runesmith", key.PROPER_NAME, key.NAMES, key.DESCRIPTION);
        }
        logger.info("Keywords setting finished.");
    }

    private static String loadJson(String jsonPath) {
        return Gdx.files.internal(jsonPath).readString(String.valueOf(StandardCharsets.UTF_8));
    }

    class Keywords {
        KeywordWithProper[] keywords;
    }

    @Override
    public boolean receivePreMonsterTurn(AbstractMonster abstractMonster) {
        ElementsGainedCountField.elementsCount.set(AbstractDungeon.player, 0);
        return true;
    }

    public static void renderElementsCounter(SpriteBatch sb, float current_x){
//        AbstractPlayer p = AbstractDungeon.player;
        if(elementalistEnabled){
            if (getRenderElementalistOrbs()) {
                elementsCounter.setYOffset(95.0F * Settings.scale);
            } else{
                elementsCounter.setYOffset(0.0F);
            }
        }
        elementsCounter.update();
        elementsCounter.render(sb, current_x);
    }

    private static boolean getRenderElementalistOrbs(){
        return ElementalistMod.elementalEnergyIsEnabled();
    }

    public static boolean getElementsRender(){
        AbstractPlayer p = AbstractDungeon.player;
        if (CardCrawlGame.dungeon != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if(renderElementsCounter) {
                return true;
            }else if ((p.hasPower(IgnisPower.POWER_ID) || p.hasPower(TerraPower.POWER_ID) || p.hasPower(AquaPower.POWER_ID))) {
                renderElementsCounter = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        AbstractPlayer p = AbstractDungeon.player;
        renderElementsCounter = p instanceof RunesmithCharacter;
    }

    @Override
    public void receivePostDraw(AbstractCard arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void receivePowersModified() {
        // TODO Auto-generated method stub

    }

    @Override
    public void receiveCardUsed(AbstractCard c) {
        if (CardStasisStatus.isStasis.get(c) || EnhanceCountField.enhanceCount.get(c) > 0) {
            if (EnhanceCountField.enhanceCount.get(c) > 0) {
                EnhanceCountField.enhanceReset.set(c, true);
            }
//			AdditionalCardDescriptions.modifyDescription(c);
            c.initializeDescription();
        }
//		logger.info("reset enhancement");
    }

    @Override
    public void receiveEditCards() {
        logger.info("begin editting cards");
        BaseMod.addDynamicVariable(new PotencyVariable());

        loadCardsToAdd();

        logger.info("add cards for the Runesmith");

        for (AbstractCard card : cardsToAdd) {
            BaseMod.addCard(card);
        }

        logger.info("done editing cards");
    }

    private void loadCardsToAdd() {
        cardsToAdd.clear();
        cardsToAdd.add(new Strike_RS());
        cardsToAdd.add(new Defend_RS());
        cardsToAdd.add(new CraftFirestone());
        cardsToAdd.add(new Fortify());
        cardsToAdd.add(new ChiselStab());
        cardsToAdd.add(new EarthShield());
        cardsToAdd.add(new RuneHurl());
        cardsToAdd.add(new HeatedChisel());
        cardsToAdd.add(new Terraform());
        cardsToAdd.add(new CraftProtectio());
        cardsToAdd.add(new CraftIncendium());
        cardsToAdd.add(new Prism());
        cardsToAdd.add(new UnstableHammer());
//        cardsToAdd.add(new HammerSlam());
        cardsToAdd.add(new EnergizedChisel());
        cardsToAdd.add(new MakeshiftArmor());
        cardsToAdd.add(new HammerThrow());
        cardsToAdd.add(new ShiftingStrike());
        cardsToAdd.add(new Grindstone());
        cardsToAdd.add(new Repurpose());
        cardsToAdd.add(new HammerTime());
        cardsToAdd.add(new Firestorm());
        cardsToAdd.add(new StaticCage());
        cardsToAdd.add(new Duplicate());
        cardsToAdd.add(new CraftIndustria());
//        cardsToAdd.add(new Shatterune());
        cardsToAdd.add(new LithiumIon());
        cardsToAdd.add(new RunicBullets());
        cardsToAdd.add(new RunicBlueprint());
        cardsToAdd.add(new CraftMagma());
        cardsToAdd.add(new ArcReactor());
        cardsToAdd.add(new UnlimitedPower());
        cardsToAdd.add(new CraftMedicinae());
        cardsToAdd.add(new OneForEveryone());
        cardsToAdd.add(new ElementalConversion());
        cardsToAdd.add(new ConvertIgnis());
        cardsToAdd.add(new ConvertTerra());
        cardsToAdd.add(new ConvertAqua());
        cardsToAdd.add(new Rearm());
        cardsToAdd.add(new FieryHammer());
        cardsToAdd.add(new Empowerment());
        cardsToAdd.add(new Runesonance());
        cardsToAdd.add(new DuctTape());
        cardsToAdd.add(new CraftReservo());
        cardsToAdd.add(new CraftFerro());
        cardsToAdd.add(new CraftSpiculum());
        cardsToAdd.add(new CraftPotentia());
        cardsToAdd.add(new ChaoticBlast());
        cardsToAdd.add(new MetallurgicalResearch());
        cardsToAdd.add(new GrandSlam());
        cardsToAdd.add(new WhiteBalance());
        cardsToAdd.add(new LastStand());
        cardsToAdd.add(new ConstructBifrost());
        cardsToAdd.add(new GoWithTheFlow());
        cardsToAdd.add(new PerfectChisel());
        cardsToAdd.add(new DoubleUp());
        cardsToAdd.add(new Refinement());
        cardsToAdd.add(new Permafrost());
//		cardsToAdd.add(new ThatsALotOfDamage());
        cardsToAdd.add(new CreatorForm());
        cardsToAdd.add(new Reinforce());
        cardsToAdd.add(new HeatExchange());
        cardsToAdd.add(new Supernova());
        cardsToAdd.add(new GenerateForcefield());
        cardsToAdd.add(new Preparation());
        cardsToAdd.add(new Malloc());
        cardsToAdd.add(new FrozenChisel());
        cardsToAdd.add(new NanitesCloud());
        cardsToAdd.add(new Overpower());
        cardsToAdd.add(new Firewall());
        cardsToAdd.add(new HammerAndChisel());
        cardsToAdd.add(new Discharge());
        cardsToAdd.add(new Attraction());
        cardsToAdd.add(new PoweredAnvil());
        cardsToAdd.add(new ReplicatingBarrier());
//		cardsToAdd.add(new TimeMachine_old());
//		cardsToAdd.add(new TimeTravel_old());
        cardsToAdd.add(new TimeMachine());
        cardsToAdd.add(new TimeTravel());
        cardsToAdd.add(new Electrocute());
        cardsToAdd.add(new FissionHammer());
        cardsToAdd.add(new Accelerate());
        cardsToAdd.add(new Augmentation());
        cardsToAdd.add(new ChargedHammer());
//        cardsToAdd.add(new Devolution());
    }

    @Override
    public void receiveEditRelics() {
        logger.info("Begin editing relics.");

        loadRelicsToAdd();

        for (CustomRelic relic : relicsToAddOnlyThisClass)
            BaseMod.addRelicToCustomPool(relic, RUNESMITH_BEIGE);
        for (CustomRelic relic : relicsToAddAllClass)
            BaseMod.addRelic(relic, RelicType.SHARED);

    }

    private void loadRelicsToAdd() {
        relicsToAddOnlyThisClass.clear();
        relicsToAddAllClass.clear();

        relicsToAddOnlyThisClass.add(new BrokenRuby());
        relicsToAddOnlyThisClass.add(new MiniCore());
        relicsToAddOnlyThisClass.add(new Locket());
        relicsToAddOnlyThisClass.add(new EmergencyProvisions());
        relicsToAddOnlyThisClass.add(new CoreCrystal());
        relicsToAddOnlyThisClass.add(new PocketReactor());

        relicsToAddAllClass.add(new Nanobots());
        relicsToAddAllClass.add(new AutoHammer());
        relicsToAddAllClass.add(new UraniumAnvil());
    }

    @Override
    public void receivePostInitialize() {
        this.loadAudio();
        elementsCounter = new ElementsCounter(ELEMENTS_GREEN_MASK);
        try{
            initializeElementalist();
        }catch (ClassNotFoundException | NoClassDefFoundError e){
            logger.info("Runesmith | Elementalist not found");
        }
    }

    private void initializeElementalist() throws ClassNotFoundException, NoClassDefFoundError{
        Class<ElementalistMod> elementalist = ElementalistMod.class;
        elementalistEnabled = true;
    }

    @Override
    public void receivePostDungeonInitialize() {
        // TODO Auto-generated method stub

    }

    @Override
    public void receivePostBattle(AbstractRoom arg0) {
        //Reset Elements gained count.
        ElementsGainedCountField.elementsCount.set(AbstractDungeon.player, 0);
        renderElementsCounter = false;
    }

    @Override
    public void receivePostExhaust(AbstractCard arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public int receiveOnPlayerLoseBlock(int arg0) {
        AbstractPlayer p = AbstractDungeon.player;
        logger.info("current block is: " + p.currentBlock);
        int blockLoss = arg0;
        if (p.hasPower("Runesmith:PermafrostPower")) {
            int lostByPermafrost = (int) Math.round(p.currentBlock / 2.0);
            blockLoss = Math.min(blockLoss, lostByPermafrost);
            if (blockLoss == lostByPermafrost)
                p.getPower("Runesmith:PermafrostPower").flash();
        }
        return blockLoss;
    }


}
