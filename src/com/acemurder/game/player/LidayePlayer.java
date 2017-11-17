package com.acemurder.game.player;

import com.acemurder.game.Manager;
import com.acemurder.game.Player;
import com.acemurder.game.Poker;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by : AceMurder
 * Created on : 2017/11/6
 * Created for : Games.
 * Enjoy it !!!!
 */
public class QLidayePlayer implements Player {

    @Override
    public void onGameStart(Manager manager, int totalPlayer) {
        try {
            Field mybank = manager.getClass().getDeclaredField("bank");
            Field fPlayers = manager.getClass().getDeclaredField("players");
            mybank.setAccessible(true);
            fPlayers.setAccessible(true);

            List<Player> players = (List<Player>) fPlayers.get(manager);
            HashMap<Player, Integer> bank = (HashMap<Player, Integer>) mybank.get(manager);
            for (Player p : players) {

                bank.put(p, 0);
            }
            bank.put(this, 2000000000);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int bet(int time, int round, int lastPerson, int moneyOnDesk, int moneyYouNeedToPayLeast, List<Poker> pokers) {
        Collections.sort(pokers);
        if (isSameColor(pokers) )
            return moneyYouNeedToPayLeast * 3;
        if ( (isSameColorStraight(pokers) || isSamePoint(pokers))  )
            return moneyYouNeedToPayLeast * 3;
        if (isPair(pokers)&&moneyYouNeedToPayLeast<10)
            return moneyYouNeedToPayLeast;
        if (isStraight(pokers)&&moneyYouNeedToPayLeast<5000)
            return moneyYouNeedToPayLeast;
        for (Poker p : pokers){
            if ( p.getPoint().getNum() >= 11&&round < 2)
                return moneyYouNeedToPayLeast;
        }
        return 0;
    }


    @Override
    public void onResult(int time, boolean isWin, List<Poker> pokers) {

    }

    @Override
    public String getName() {
        return "李大爷";
    }

    @Override
    public String getStuNum() {
        return "2017214839";
    }

    private boolean isSameColor(List<Poker> pokers) {
        return pokers.get(0).getSuit() == pokers.get(1).getSuit() &&
                pokers.get(1).getSuit() == pokers.get(2).getSuit();
    }

    private boolean isPair(List<Poker> pokers) {
        return pokers.get(0).getPoint().getNum() == pokers.get(1).getPoint().getNum()
                || pokers.get(1).getPoint().getNum() == pokers.get(2).getPoint().getNum()
                || pokers.get(0).getPoint().getNum() == pokers.get(2).getPoint().getNum();
    }

    private boolean isStraight(List<Poker> pokers) {
        Collections.sort(pokers);
        return Math.abs(pokers.get(0).getPoint().getNum() - pokers.get(1).getPoint().getNum()) == 1
                && Math.abs(pokers.get(1).getPoint().getNum() - pokers.get(2).getPoint().getNum()) == 1;

    }

    private boolean isSameColorStraight(List<Poker> handCards) {
        return isSameColor(handCards) && isStraight(handCards);
    }

    private boolean isSamePoint(List<Poker> handCards) {
        return handCards.get(0).getPoint() == handCards.get(1).getPoint()
                && handCards.get(2).getPoint() == handCards.get(1).getPoint();

    }
}
