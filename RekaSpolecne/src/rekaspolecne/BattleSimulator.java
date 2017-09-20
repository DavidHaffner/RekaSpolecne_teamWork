/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rekaspolecne;

import java.util.Random;

/**
 *
 * @author 
 */
public class BattleSimulator {
    private River river;
    Random rand = new Random();

    public BattleSimulator (River river) {
        this.river = river;
    }

    /* metoda simuluje boje po 2 flotilách až do konečného řešení vítězné otázky,
    tj. dokud nezůstane poslední flotila */
    public River run () {
        /* až zůstane v kolekci river poslední flotila, tak kolekci vrátíme jako output
        tj. právě s 1 elementem */
        while (this.river.getSize()>1) {
            
            // nejdříve vybereme indexy 2 flotil a ty spolu budou bojovat
            int indFlA; // index fleet A
            indFlA = rand.nextInt(this.river.getSize());
            int indFlB; // index fleet B
            indFlB = rand.nextInt(this.river.getSize()); 
            while (indFlA==indFlB) {
                indFlB = rand.nextInt(this.river.getSize());
            }
            Fleet fleetA = river.getFleet(indFlA);
            Fleet fleetB = river.getFleet(indFlB);
            
            duel (fleetA, fleetB);
            
            if (fleetA.sizeFleetShips()==0) {
                river.removeFleet(indFlA);  // potopená flotila je odstraněna
            }            
            if (fleetB.sizeFleetShips()==0) {
                river.removeFleet(indFlB);  // potopená flotila je odstraněna
            }     
        }
        return this.river;
    }
    
    /* metoda simuluje boj 2 flotil: bojujeme tak dlouho, dokud jedna z flotil 
    nebude mít v atributu teamShips nulu, tj. bude bez lodí */
    private void duel (Fleet fleetA, Fleet fleetB) {
        HitEvaluator hitEvaluator = new HitEvaluator();
        
        while (fleetA.sizeTeamShips()>0 && fleetB.sizeTeamShips()>0) {
            int indShA = rand.nextInt(fleetA.sizeTeamShips()); // index ship A
            int indShB = rand.nextInt(fleetB.sizeTeamShips()); // index ship B    
            Ship shipA = fleetA.getTeamShip(indShA);
            Ship shipB = fleetB.getTeamShip(indShB);
            
            if (hitEvaluator.hitEvaluator(shipA, shipB)) { // jestli se A trefí
            shipB.setHp(shipB.getHp()-shipA.getNcannon()); // výstřel A do B
            }
            if (hitEvaluator.hitEvaluator(shipB, shipA)) { // jestli se B trefí
            shipA.setHp(shipA.getHp()-shipB.getNcannon()); // výstřel B do A
            }
            System.out.printf(shipB.toString()); // výstup do konzole
            System.out.printf(shipA.toString());
            System.out.println();
            
            if (shipA.getHp()<1) {
                shipA.setHp(0);  // nechceme záporné HP
                fleetA.addTeamGraveyard(shipA);
                fleetA.removeTeamShip(indShA);
            }
            if (shipB.getHp()<1) {
                shipB.setHp(0);  // nechceme záporné HP
                fleetB.addTeamGraveyard(shipB);
                fleetB.removeTeamShip(indShB);
            }
        }        
    }
}
