package runesmith.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

@SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
public class ElementsGainedCountField {
    public static SpireField<Integer> elementsCount = new SpireField<Integer>(() -> 0);
}
