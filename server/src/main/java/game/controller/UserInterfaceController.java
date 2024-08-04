package game.controller;

import game.model.FinalPanelModel;
import shared.Model.MyPolygon;
import shared.Model.TimedLocation;
import game.model.charactersModel.*;
import game.model.entities.Ability;


import game.model.entities.Skill;

import server.DataBase;


import java.awt.*;
import java.awt.geom.Point2D;
import static shared.Model.EntityType.*;

public abstract class UserInterfaceController {


    public static void createEpsilonView(String id, Image image){
        DataBase dataBase = DataBase.getDataBase();
        dataBase.createEntity(id, epsilon);
    }

    public static void createSquarantineView(String id){
        DataBase dataBase = DataBase.getDataBase();
        dataBase.createEntity(id, squarantine);
    }

    public static void createTrigorathView(String id){
        DataBase dataBase = DataBase.getDataBase();
        dataBase.createEntity(id, trigorath);
    }

    public static void creatBulletView(String id){
        DataBase dataBase = DataBase.getDataBase();
        dataBase.createEntity(id, bullet);
    }


    public static void createCollectibleView(String id){
        DataBase dataBase = DataBase.getDataBase();
        dataBase.createEntity(id, collectible);
    }

    public static void createFinalPanelView(String id, Point2D location, Dimension size){
        DataBase dataBase = DataBase.getDataBase();
        dataBase.createPanel(id, location, size);
    }


    public static void eliminateGeoShapeView(String id){
        DataBase dataBase = DataBase.getDataBase();
        dataBase.eliminateEntity(id);
        dataBase.getCreatedEntities().remove(id);
    }

    public static void removeFinalPanelView(String id){
        DataBase dataBase = DataBase.getDataBase();
        dataBase.eliminateEntity(id);
        dataBase.getCreatedPanels().remove(id);

    }

    public static void eliminateBulletView(String id){
//        DataBase dataBase = DataBase.getDataBase();
//        dataBase.eliminateEntity(id);
//        dataBase.getCreatedEntities().remove(id);

    }

    public static void createOmenoctView(String id){
        DataBase dataBase = DataBase.getDataBase();
        dataBase.createEntity(id, simplePolygon);
    }

//
//
//    public static void createBabyEpsilonView(String id){ new BabyEpsilonView(id); }
//
//    public static void createSmileyAOEView(String id){ new SmileyAOE(id); }
//
//
//
//    public static void createGeoShapeView(String id, Image image){
//        new GeoShapeView(id, image);
//    }
//
//    public static void createGeoShapeView(String id, Image image, int zOrder){
//        new GeoShapeView(id, image, zOrder);
//    }
//
//    public static void createNecropickView(String id, Image image){
//        new NecropickView(id, image);
//    }
//
//    public static void createArchmireView(String id, Image image){
//        new ArchmireView(id, image);
//    }
//
//    public static void createLaserView(String id){
//        new LaserView(id);
//    }
//
//    public static void createGeoShapeView(String id){ new GeoShapeView(id); }
//
//
//
//
//
//
//
//
//    public static void updateNecropick(String id){
//        NecropickModel m = findNecropickModel(id);
//        NecropickView v = findNecropickView(id);
//        assert m != null;
//        assert v != null;
//        v.showNextLocation = m.isHovering();
//    }
//



    public static Skill findSkill(String name) {
        for (Skill.SkillType type : Skill.SkillType.values()) {
            for (Skill skill : Skill.values()) {
                if (skill.getType().equals(type) && skill.getName().equals(name)) return skill;
            }
        }
        return null;
    }

    public static void fireSkill() {
        if (Skill.activeSkill != null) {
            Skill.activeSkill.fire();
        }
    }

    public static void fireAbility() {
        if (Ability.activeAbility != null) {
            Ability.activeAbility.fire();
            Ability.activeAbility = null;
        }
    }

    public static boolean isGameRunning() {
        return GameLoop.getINSTANCE().isRunning();
    }

    public static boolean isGameOn() {
        return GameLoop.getINSTANCE().isOn();
    }




}